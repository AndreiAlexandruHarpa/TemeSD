package org.example.interfaces

import org.example.model.Book

interface JSONPrinter {
    fun printJSON(books: Set<Book>): String
}