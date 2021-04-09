package org.example.microservices

import org.example.interfaces.LibraryDAO
import org.example.interfaces.LibraryPrinter
import org.example.model.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class LibraryPrinterMicroservice {
    @Autowired
    private lateinit var libraryDAO: LibraryDAO

    @Autowired
    private lateinit var libraryPrinter: LibraryPrinter

    @RequestMapping("/print", method = [RequestMethod.GET])
    @ResponseBody
    fun customPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String): String {
        return when(format) {
            "html" -> libraryPrinter.printHTML(libraryDAO.getBooks() as Set<Book>)
            "json" -> libraryPrinter.printJSON(libraryDAO.getBooks() as Set<Book>)
            "raw" -> libraryPrinter.printRaw(libraryDAO.getBooks() as Set<Book>)
            else -> "Not implemented"
        }
    }

    @RequestMapping("/find-and-print", method = [RequestMethod.GET])
    @ResponseBody
    fun customFind(@RequestParam(required = false, name = "author", defaultValue = "") author: String,
                   @RequestParam(required = false, name = "title", defaultValue = "") title: String,
                   @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String,
                   @RequestParam(required = false, name = "format", defaultValue = "json")format: String): String? {
        val books: Set<Book>?
        if (author != "")
            books = this.libraryDAO.findAllByAuthor(author)
        else if (title != "")
            books = this.libraryDAO.findAllByTitle(title)
        else
            books = this.libraryDAO.findAllByPublisher(publisher)

        if(format == "json")
            return books?.let { this.libraryPrinter.printJSON(it) }
        else if(format == "html")
            return books?.let {this.libraryPrinter.printHTML(it) }
        else if(format == "raw")
            return books?.let {this.libraryPrinter.printRaw(it)}
        else
            return books?.let {this.libraryPrinter.printJSON(it)}
    }
}