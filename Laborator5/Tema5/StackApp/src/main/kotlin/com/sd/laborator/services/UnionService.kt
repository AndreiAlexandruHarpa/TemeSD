package com.sd.laborator.services

import com.sd.laborator.interfaces.CartesianProductOperation
import com.sd.laborator.interfaces.ServiceChain
import com.sd.laborator.interfaces.UnionOperation
import org.springframework.stereotype.Service

@Service
class UnionService: UnionOperation, ServiceChain {
    private var nextService: ServiceChain ?= null
    private lateinit var response: Any

    override fun handleRequest(vararg request: Any) {

        val multime1: MutableSet<Pair<Int, Int>> = mutableSetOf()
        request[0].toString().split("(").toString().forEach() {
            multime1.add(Pair(it.toString().trim(')').split(",")[0].toInt(),
                it.toString().trim(')').split(",")[1].toInt()))
        }

        println(multime1)


        val multime2: MutableSet<Pair<Int, Int>> = mutableSetOf()
        request[1].toString().split("(").toString().forEach() {
            multime2.add(Pair(it.toString().trim(')').split(",")[0].toInt(),
                it.toString().trim(')').split(",")[1].toInt()))
        }
        response = executeOperation(multime1, multime2)
        nextService?.handleRequest(response)
    }

    override fun setNextServiceChain(nextServiceChain: ServiceChain) {
        nextService = nextServiceChain
    }

    override fun getServiceResponse():Any{
        return response
    }

    override fun executeOperation(A: Set<Pair<Int, Int>>, B: Set<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        return A union B
    }

}