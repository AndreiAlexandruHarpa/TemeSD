package com.sd.laborator.services

import com.sd.laborator.interfaces.PrimeNumberGenerator
import com.sd.laborator.interfaces.ServiceChain
import org.springframework.stereotype.Service

@Service
class PrimeNumberGeneratorService: PrimeNumberGenerator, ServiceChain {
    private val primeNumbersIn1To100: Set<Int> = setOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97)
    private var nextService: ServiceChain ?= null
    private lateinit var response: Any

    override fun generatePrimeNumber(): Int {
        return primeNumbersIn1To100.elementAt((0 until primeNumbersIn1To100.count()).random())
    }


    override fun generateListOfPrimeNumbers(count: Int, numberOfLists: Int): List<List<Int>> {
        val primeNumbers: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())
        while (primeNumbers.count() != numberOfLists) {
            while (primeNumbers[primeNumbers.count()].count() != count) {
                primeNumbers[primeNumbers.count()].add(generatePrimeNumber())
            }
        }
        return primeNumbers
    }

    override fun handleRequest(vararg request: Any) {
        response = generateListOfPrimeNumbers(request[0].toString().toInt(), request[1].toString().toInt())
        nextService?.handleRequest(response)
    }

    override fun setNextServiceChain(nextServiceChain: ServiceChain) {
        nextService = nextServiceChain
    }

    override fun getServiceResponse():Any{
        return response
    }

}