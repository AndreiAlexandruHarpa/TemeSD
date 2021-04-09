package org.example.microservices

import org.example.controllers.RabbitMqController
import org.example.interfaces.LibraryDAO
import org.example.interfaces.LibraryPrinter
import org.example.model.Book
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

@Controller
class LibraryPrinterMicroservice {
    @Autowired
    private lateinit var libraryDAO: LibraryDAO

    @Autowired
    private lateinit var libraryPrinter: LibraryPrinter

    @Autowired
    private lateinit var rabbitMqController: RabbitMqController

    @Autowired
    private lateinit var amqpTemplate: AmqpTemplate

    private var myQueue: Queue<String> = LinkedList<String>()

    @RequestMapping("/print", method = [RequestMethod.GET])
    @ResponseBody
    fun customPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String): String {
        sendMessage("/print&$format")
        while(myQueue.isEmpty())
            Thread.sleep(10)

        val book = myQueue.elementAt(0)
        myQueue.clear()

        return book
    }

    @RequestMapping("/find-and-print", method = [RequestMethod.GET])
    @ResponseBody
    fun customFind(@RequestParam(required = false, name = "author", defaultValue = "") author: String,
                   @RequestParam(required = false, name = "title", defaultValue = "") title: String,
                   @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String,
                   @RequestParam(required = false, name = "format", defaultValue = "json")format: String): String? {
        var query = "/find-and-print"

        query += when {
            author != "" -> "&$format&author=$author"
            title != "" -> "&$format&title=$title"
            publisher != "" -> "&$format&publisher=$publisher"
            else -> "&json&author=Jules Verne"
        }

        sendMessage(query)

        while(myQueue.isEmpty())
            Thread.sleep(10)

        val book = myQueue.elementAt(0)
        myQueue.clear()

        return book
    }

    @RabbitListener(queues = ["\${libraryapp.rabbitmq.queue}"])
    fun fetchMessage(msg: String) {
        val processedMessage = msg.split("&")

        when(processedMessage[0]) {
            "miss", "old-timestamp" -> {
                val printOption = processedMessage[1]
                val printFormat = processedMessage[2]

                if(printOption == "/print") {
                    val books:String = when(printFormat) {
                        "json" -> libraryPrinter.printJSON(libraryDAO.getBooks() as Set<Book>)
                        "html" -> libraryPrinter.printHTML(libraryDAO.getBooks() as Set<Book>)
                        else -> libraryPrinter.printRaw(libraryDAO.getBooks() as Set<Book>)
                    }
                    sendMessage("add&$printOption&$printFormat&$books")
                    myQueue.add(books)
                } else {
                    val filterData = processedMessage[3].split("=")
                    val books: String? = when(printFormat) {
                        "json" -> {
                            when (filterData[0]) {
                                "author" -> libraryDAO.findAllByAuthor(filterData[1])
                                    ?.let { libraryPrinter.printJSON(it) }
                                "title" -> libraryDAO.findAllByTitle(filterData[1])
                                    ?.let { libraryPrinter.printJSON(it) }
                                else -> libraryDAO.findAllByPublisher(filterData[1])
                                    ?.let { libraryPrinter.printJSON(it) }
                            }
                        }
                        "html" -> {
                            when (filterData[0]) {
                                "author" -> libraryDAO.findAllByAuthor(filterData[1])
                                    ?.let { libraryPrinter.printHTML(it) }
                                "title" -> libraryDAO.findAllByTitle(filterData[1])
                                    ?.let { libraryPrinter.printHTML(it) }
                                else -> libraryDAO.findAllByPublisher(filterData[1])
                                    ?.let { libraryPrinter.printHTML(it) }
                            }
                        }
                        else -> {
                            when (filterData[0]) {
                                "author" -> libraryDAO.findAllByAuthor(filterData[1])
                                    ?.let { libraryPrinter.printRaw(it) }
                                "title" -> libraryDAO.findAllByTitle(filterData[1])
                                    ?.let { libraryPrinter.printRaw(it) }
                                else -> libraryDAO.findAllByPublisher(filterData[1])
                                    ?.let { libraryPrinter.printRaw(it) }
                            }
                        }
                    }
                    sendMessage("add&$printOption&$printFormat&${filterData[0]}=${filterData[1]}&$books")
                    myQueue.add(books)
                }
            }
            "hit" -> {
                myQueue.add(processedMessage[1])
            }
        }
    }

    private fun sendMessage(msg: String) {
        println("message: ")
        println(msg)
        rabbitMqController.setRoutingKey("libraryapp.routingkey1")
        this.amqpTemplate.convertAndSend(rabbitMqController.getExchange(),
        rabbitMqController.getRoutingKey(), msg)
    }
}