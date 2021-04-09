package org.example.interfaces

import org.example.model.Cache

interface CachingDAO {
    fun exists(query: String): Set<Cache?>
    fun addToCache(cache: Cache)
    fun createTable()
    fun deleteTable()
    fun deleteByQuery(query: String)
}