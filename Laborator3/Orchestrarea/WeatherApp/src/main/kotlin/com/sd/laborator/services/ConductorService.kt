package com.sd.laborator.services

import com.sd.laborator.interfaces.LocationSearchInterface
import com.sd.laborator.interfaces.TimeInterface
import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.pojo.WeatherForecastData
import org.springframework.stereotype.Service

@Service
class ConductorService(
    private val locationSearchService: LocationSearchInterface,
    private val timeService: TimeInterface,
    private val weatherForecastService: WeatherForecastInterface) {


    fun getResponse(location: String): String {

        val locationId = locationSearchService.getLocationId(location)

        if (locationId == -1) {
            return "Nu s-au putut gasi date meteo pentru cuvintele cheie \"$location\"!"
        }

        val date: String = timeService.getCurrentTime()
        val rawForecastData: WeatherForecastData = weatherForecastService.getForecastData(locationId, date)

        return rawForecastData.toString()
    }
}
