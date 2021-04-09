package org.example.services

import org.example.interfaces.CachingDAO
import org.example.model.Cache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.SQLException
import java.util.regex.Pattern

class CacheRowMapper : RowMapper<Cache> {
    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): Cache {
        return Cache(rs.getInt("timestamp"), rs.getString("query"),
            rs.getString("result")
        )
    }
}

@Service
class CachingDAOService: CachingDAO {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    var pattern: Pattern = Pattern.compile("\\W")

    override fun createTable() {
        jdbcTemplate.execute("""CREATE TABLE IF NOT EXISTS cache(
                                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                timestamp INTEGER,
                                                query VARCHAR(50),
                                                result VARCHAR(100),
                                                UNIQUE (timestamp , query, result))
                                """)
    }

    override fun deleteTable() {
        jdbcTemplate.execute("DROP TABLE cache")
    }

    override fun deleteByQuery(query: String) {
        jdbcTemplate.update("Delete FROM cache WHERE query ='$query'")
    }
    override fun exists(query: String): Set<Cache?> {
        val data: MutableList<Cache?> = jdbcTemplate.query("SELECT * FROM cache WHERE query='$query'", CacheRowMapper())
        return data.toSet()
    }

    override fun addToCache(cache: Cache) {
        jdbcTemplate.update("""INSERT INTO cache(timestamp, query, result) VALUES(?, ?, ?)""",
                            cache.cacheTimestamp, cache.cacheQuery, cache.cacheResult)
    }
}