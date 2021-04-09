package org.example.model

class Cache(private var timestamp: Int, private var query: String, private var result: String) {
    var cacheTimestamp: Int
        get() {
            return timestamp
        }
        set(timestamp) {
            this.timestamp = timestamp
        }

    var cacheQuery: String
        get() {
            return query
        }
        set(query) {
            this.query = query
        }

    var cacheResult: String
        get(){
            return result
        }
        set(result) {
            this.result = result
        }
}