package org.example.interfaces

import org.example.model.Book

interface LibraryDAO {
    fun createLibraryTable()
    fun getBooks(): Set<Book?>
    fun addBook(book: Book)
    fun findAllByAuthor(author: String): Set<Book>?
    fun findAllByTitle(title: String): Set<Book>?
    fun findAllByPublisher(publisher: String): Set<Book>?
}