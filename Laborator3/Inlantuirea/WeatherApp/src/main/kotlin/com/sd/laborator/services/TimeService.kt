package com.sd.laborator.services

import com.sd.laborator.interfaces.ServiceChain
import com.sd.laborator.interfaces.TimeInterface
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class TimeService : TimeInterface, ServiceChain{
    var nextService: ServiceChain ?= null
    private var response: String = ""

    override fun setNextServiceChain(nextServiceChain: ServiceChain) {
        nextService = nextServiceChain
    }

    override fun getServiceResponse(): Any {
        return response
    }

    override fun handleRequest(vararg request: Any) {
        response = getCurrentTime()
        nextService?.handleRequest(request[0], response)
    }

    override
    fun getCurrentTime():String {
        val formatter =  SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return formatter.format(Date())
    }
}