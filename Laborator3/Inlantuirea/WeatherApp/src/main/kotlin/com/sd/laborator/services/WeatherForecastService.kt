package com.sd.laborator.services

import com.sd.laborator.interfaces.ServiceChain
import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.pojo.WeatherForecastData
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.net.URL
import kotlin.math.roundToInt

@Service
class WeatherForecastService: WeatherForecastInterface, ServiceChain {
    var nextService: ServiceChain ?= null
    private lateinit var response: Any


    override fun setNextServiceChain(nextServiceChain: ServiceChain) {
        nextService = nextServiceChain
    }

    override fun getServiceResponse(): Any {
        return response
    }

    override fun handleRequest(vararg request: Any) {
        response = getForecastData(request[0].toString().toInt(), request[1].toString())
        nextService?.handleRequest(response)
    }

    override fun getForecastData(locationId: Int, date: String): WeatherForecastData {
        // ID-ul locaţiei nu trebuie codificat, deoarece este numeric
        val forecastDataURL = URL("https://www.metaweather.com/api/location/$locationId/")

        // preluare conţinut răspuns HTTP la o cerere GET către URL-ul de mai sus
        val rawResponse: String = forecastDataURL.readText()

        // parsare obiect JSON primit
        val responseRootObject = JSONObject(rawResponse)
        val weatherDataObject = responseRootObject.getJSONArray("consolidated_weather").getJSONObject(0)

        // construire şi returnare obiect POJO care încapsulează datele meteo
        return WeatherForecastData(
            location = responseRootObject.getString("title"),
            date = date,
            weatherState = weatherDataObject.getString("weather_state_name"),
            weatherStateIconURL =
                "https://www.metaweather.com/static/img/weather/png/${weatherDataObject.getString("weather_state_abbr")}.png",
            windDirection = weatherDataObject.getString("wind_direction_compass"),
            windSpeed = weatherDataObject.getFloat("wind_speed").roundToInt(),
            minTemp = weatherDataObject.getFloat("min_temp").roundToInt(),
            maxTemp = weatherDataObject.getFloat("max_temp").roundToInt(),
            currentTemp = weatherDataObject.getFloat("the_temp").roundToInt(),
            humidity = weatherDataObject.getFloat("humidity").roundToInt()
        )
    }
}