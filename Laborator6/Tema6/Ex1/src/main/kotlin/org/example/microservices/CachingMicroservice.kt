package org.example.microservices

import org.example.controllers.RabbitMqController
import org.example.interfaces.CachingDAO
import org.example.model.Cache
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class CachingMicroservice {
    @Autowired
    private lateinit var cachingDAO: CachingDAO

    @Autowired
    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    private lateinit var rabbitMqController: RabbitMqController

    @RabbitListener(queues = ["\${libraryapp.rabbitmq.queue1}"])
    fun fetchMessage(msg: String) {
        val processedMessage = msg.split("&")
        val option = processedMessage[0]
        val currentTimestamp = (System.currentTimeMillis() / 3600000L).toInt()

        val result :String? = when(option) {
            "/print", "/find-and-print" -> {
                val cache = cachingDAO.exists(msg)
                if(cache.isEmpty()){
                    "miss&$msg"
                } else {
                    if(currentTimestamp - cache.elementAt(0)?.cacheTimestamp!! >= 1) {
                        cachingDAO.deleteByQuery(msg)
                        "old-timestamp&$msg"
                    } else {
                        "hit&${cache.elementAt(0)?.cacheResult!!}"
                    }
                }

            }
            "add" -> {
                if(processedMessage[1] == "/print") {
                    val query = "${processedMessage[1]}&${processedMessage[2]}"
                    cachingDAO.addToCache(Cache(currentTimestamp, query, processedMessage[3]))
                    null
                } else {
                    val query = "${processedMessage[1]}&${processedMessage[2]}&${processedMessage[3]}"
                    cachingDAO.addToCache(Cache(currentTimestamp, query, processedMessage[4]))
                    null
                }
            }
            else -> null
        }
        if(result != null)
            sendMessage(result)
    }

    fun sendMessage(msg: String) {
        println("message: ")
        println(msg)
        rabbitMqController.setRoutingKey("libraryapp.routingkey")
        this.amqpTemplate.convertAndSend(rabbitMqController.getExchange(),
        rabbitMqController.getRoutingKey(), msg)
    }
}