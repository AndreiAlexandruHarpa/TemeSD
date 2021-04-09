package org.example.interfaces

import org.example.model.Book

interface RawPrinter {
    fun printRaw(books: Set<Book>): String
}