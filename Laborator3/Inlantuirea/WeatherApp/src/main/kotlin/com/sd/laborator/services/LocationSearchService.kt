package com.sd.laborator.services

import com.sd.laborator.interfaces.LocationSearchInterface
import com.sd.laborator.interfaces.ServiceChain
import org.springframework.stereotype.Service
import java.net.URL
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class LocationSearchService : LocationSearchInterface, ServiceChain {
    var nextService: ServiceChain ?= null
    private var response: String = ""

    override fun handleRequest(vararg request: Any) {
        val locationId: Int = getLocationId(request[0].toString())

        if (locationId == -1) {
            response = "Nu s-au putut gasi date meteo pentru cuvintele cheie \"${request[0]}\"!"
        } else {
            nextService?.handleRequest(locationId)
        }
    }

    override fun setNextServiceChain(nextServiceChain: ServiceChain) {
        nextService = nextServiceChain
    }

    override fun getServiceResponse():String {
        return response
    }

    override fun getLocationId(locationName: String): Int {
        // codificare parametru URL (deoarece poate conţine caractere speciale)
        val encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8.toString())

        // construire obiect de tip URL
        val locationSearchURL = URL("https://www.metaweather.com/api/location/search/?query=$encodedLocationName")

        // preluare raspuns HTTP (se face cerere GET şi se preia conţinutul răspunsului sub formă de text)
        val rawResponse: String = locationSearchURL.readText()

        // parsare obiect JSON
        val responseRootObject = JSONObject("{\"data\": ${rawResponse}}")
        val responseContentObject = responseRootObject.getJSONArray("data").takeUnless { it.isEmpty }
            ?.getJSONObject(0)
        return responseContentObject?.getInt("woeid") ?: -1
    }
}