package com.sd.laborator.components

import com.sd.laborator.interfaces.LibraryDAO
import com.sd.laborator.interfaces.LibraryPrinter
import com.sd.laborator.model.Book
import com.sd.laborator.model.Content
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class LibraryAppComponent {
    @Autowired
    private lateinit var libraryDAO: LibraryDAO

    @Autowired
    private lateinit var libraryPrinter: LibraryPrinter

    @Autowired
    private lateinit var connectionFactory: RabbitMqConnectionFactoryComponent
    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    fun initTemplate() {
        this.amqpTemplate = connectionFactory.rabbitTemplate()
    }

    fun sendMessage(msg: String) {
        this.amqpTemplate.convertAndSend(connectionFactory.getExchange(),
                                         connectionFactory.getRoutingKey(),
                                         msg)
    }

    @RabbitListener(queues = ["\${libraryapp.rabbitmq.queue}"])
    fun recieveMessage(msg: String) {
        // the result needs processing
        val processedMsg = (msg.split(",").map { it.toInt().toChar() }).joinToString(separator="")
        val messages = processedMsg.split(",")
        try {
            var result: String ?= null
            if(messages.size == 2) {
                var parameter = messages[0].split(":")[1]
                val books = customFind(parameter)
                println(parameter)
                println(books)
                parameter = messages[1].split(":")[1]
                result = customPrint(parameter, books)
            } else if(messages.size == 4){
                if (addBook(createBook(messages)))
                    result = "Carte adaugata cu succes!"
                else
                    result = "Eroare la adaugarea cartii!"
            } else {
                val parameter = messages[0].split(":")[1]
                result = customPrint(parameter, libraryDAO.getBooks())
            }
                if (result != null) sendMessage(result)
        } catch (e: Exception) {
            println(e)
        }
    }

    fun createBook(data: List<String>): Book {

        val author = data[0].split(":")[1]
        val name = data[1].split(":")[1]
        val publisher = data[2].split(":")[1]
        val continut = data[3].split(":")[1]
        val content = Content(author, continut, name, publisher)

        return Book(content)
    }

    fun customPrint(format: String, books: Set<Book> ?= null): String? {
        return when(format) {
            "html" -> books?.let { libraryPrinter.printHTML(it) }
            "json" -> books?.let { libraryPrinter.printJSON(it) }
            "xml" -> books?.let { libraryPrinter.printXML(it) }
            "raw" -> books?.let { libraryPrinter.printRaw(it) }
            else -> "Not implemented"
        }
    }

    fun customFind(searchParameter: String): Set<Book>? {
        val (field, value) = searchParameter.split("=")
        return when(field) {
            "author" -> this.libraryDAO.findAllByAuthor(value)
            "title" -> this.libraryDAO.findAllByTitle(value)
            "publisher" -> this.libraryDAO.findAllByPublisher(value)
            "html" -> this.libraryDAO.getBooks()
            else -> null
        }
    }

    fun addBook(book: Book): Boolean {
        return try {
            this.libraryDAO.addBook(book)
            true
        } catch (e: Exception) {
            false
        }
    }

}