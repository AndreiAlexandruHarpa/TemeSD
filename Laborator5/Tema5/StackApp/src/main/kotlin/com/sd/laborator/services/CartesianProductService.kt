package com.sd.laborator.services

import com.sd.laborator.interfaces.CartesianProductOperation
import com.sd.laborator.interfaces.ServiceChain
import org.springframework.stereotype.Service

@Service
class CartesianProductService: CartesianProductOperation, ServiceChain {
    private var nextService: ServiceChain ?= null
    private lateinit var response: Any

    override fun executeOperation(A: Set<Int>, B: Set<Int>): Set<Pair<Int, Int>> {
        var result: MutableSet<Pair<Int, Int>> = mutableSetOf()
        A.forEach { a -> B.forEach { b -> result.add(Pair(a, b)) } }
        return result.toSet()
    }

    override fun handleRequest(vararg request: Any) {
        val multime1: MutableSet<Int> = mutableSetOf()
        request[0].toString().split(",").forEach() {
            multime1.add(it.toString().toInt())
        }

        println(multime1)


        val multime2: MutableSet<Int> = mutableSetOf()
        request[1].toString().split(",").forEach() {
            multime2.add(it.toString().toInt())
        }
        println(multime2)
        response = executeOperation(multime1, multime2)
        nextService?.handleRequest(response)
    }

    override fun setNextServiceChain(nextServiceChain: ServiceChain) {
        nextService = nextServiceChain
    }

    override fun getServiceResponse():Any{
        return response
    }
}