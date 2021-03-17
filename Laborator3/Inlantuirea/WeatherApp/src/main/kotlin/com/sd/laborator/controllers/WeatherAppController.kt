package com.sd.laborator.controllers


import com.sd.laborator.interfaces.ServiceChain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class WeatherAppController {
    @Autowired
    private lateinit var locationSearchService: ServiceChain

    @Autowired
    private lateinit var weatherForecastService: ServiceChain

    @Autowired
    private lateinit var timeService: ServiceChain


    @RequestMapping("/getforecast/{location}", method = [RequestMethod.GET])
    @ResponseBody
    fun getForecast(@PathVariable location: String): String {
        locationSearchService.setNextServiceChain(timeService)
        timeService.setNextServiceChain(weatherForecastService)

        // pe baza ID-ului de locaţie, se interoghează al doilea serviciu care returnează datele meteo
        // încapsulate într-un obiect POJO
        locationSearchService.handleRequest(location)

        // fiind obiect POJO, funcţia toString() este suprascrisă pentru o afişare mai prietenoasă
        return weatherForecastService.getServiceResponse().toString()
    }
}