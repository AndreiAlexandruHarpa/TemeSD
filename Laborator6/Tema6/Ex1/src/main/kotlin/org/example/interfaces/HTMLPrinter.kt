package org.example.interfaces

import org.example.model.Book

interface HTMLPrinter {
    fun printHTML(books: Set<Book>): String
}