package com.sd.laborator.interfaces

interface ServiceChain {
    fun setNextServiceChain(nextServiceChain: ServiceChain)
    fun handleRequest(vararg request: Any)
    fun getServiceResponse(): Any
}