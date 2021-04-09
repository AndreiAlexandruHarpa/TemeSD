package com.sd.laborator.interfaces

interface PrimeNumberGenerator {
    fun generatePrimeNumber(): Int
    fun generateListOfPrimeNumbers(count: Int, numberOfLists: Int): List<List<Int>>
}