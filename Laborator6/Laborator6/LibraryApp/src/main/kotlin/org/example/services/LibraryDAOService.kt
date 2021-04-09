package org.example.services

import org.example.interfaces.LibraryDAO
import org.example.model.Book
import org.example.model.Content
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.SQLException
import java.util.regex.Pattern


class LibraryRowMapper : RowMapper<Book> {
    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): Book {
        return Book(Content(rs.getString("author"), rs.getString("text"),
            rs.getString("title"), rs.getString("publisher")))
    }
}


@Service
class LibraryDAOService: LibraryDAO {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    var pattern: Pattern = Pattern.compile("\\W")

    override fun createLibraryTable() {
        jdbcTemplate.execute("""CREATE TABLE IF NOT EXISTS books(
                                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                author VARCHAR(30),
                                                title VARCHAR(40),
                                                publisher VARCHAR(40),
                                                text VARCHAR(200),
                                                UNIQUE (author, title, publisher, text)
          )""")
        var book = Book(Content("Roberto Ierusalimschy","Preface. When Waldemar, Luiz, and I started the development of Lua, back in 1993, we could hardly imagine that it would spread as it did. ...","Programming in LUA","Teora"))
        addBook(book)
        book = Book(Content("Jules Verne","Nemaipomeniti sunt francezii astia! - Vorbiti, domnule, va ascult! ....","Steaua Sudului","Corint"))
        addBook(book)
        book = Book(Content("Jules Verne","Cuvant Inainte. Imaginatia copiilor - zicea un mare poet romantic spaniol - este asemenea unui cal nazdravan, iar curiozitatea lor e pintenul ce-l fugareste prin lumea celor mai indraznete proiecte.","O calatorie spre centrul pamantului","Polirom"))
        addBook(book)
        book = Book(Content("Jules Verne","Partea intai. Naufragiatii vazduhului. Capitolul 1. Uraganul din 1865. ...","Insula Misterioasa","Teora"))
        addBook(book)
        book = Book(Content("Jules Verne","Capitolul I. S-a pus un premiu pe capul unui om. Se ofera premiu de 2000 de lire ...","Casa cu aburi","Albatros"))
        addBook(book)

    }

    override fun getBooks(): Set<Book?> {
        val result: MutableList<Book?> = jdbcTemplate.query("SELECT * FROM books", LibraryRowMapper())

        return result.toSet()
    }

    override fun addBook(book: Book) {
        if(!pattern.matcher(book.author).find()) {
            println("SQL Inejction for add book")
            return
        }
        jdbcTemplate.update("INSERT INTO books(author, title, publisher, text) VALUES (?, ?, ?, ?)",
            book.author, book.name, book.publisher, book.content)
    }

    override fun findAllByAuthor(author: String): Set<Book>? {
        if(!pattern.matcher(author).find()) {
            println("SQL Injection for book author")
            return null
        }
        val result: MutableList<Book> = jdbcTemplate.query("SELECT * FROM books WHERE author = '$author'", LibraryRowMapper())
        return result.toSet()
    }

    override fun findAllByTitle(title: String): Set<Book>? {
        if(!pattern.matcher(title).find()) {
            println("SQL Injection for book title")
            return null
        }
        val result: MutableList<Book> = jdbcTemplate.query("SELECT * FROM books WHERE title = '$title'", LibraryRowMapper())
        return result.toSet()
    }

    override fun findAllByPublisher(publisher: String): Set<Book>? {
        if(pattern.matcher(publisher).find()) {
            println("SQL Injection for book publisher")
            return null
        }
        val result: MutableList<Book> = jdbcTemplate.query("SELECT * FROM books WHERE publisher = '$publisher'", LibraryRowMapper())
        return result.toSet()
    }
}