package com.example.theater.web

import com.example.theater.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView
import javax.websocket.server.PathParam
import kotlin.reflect.full.declaredMemberFunctions

@Controller
@RequestMapping("/reports")
class ReportsController {

    @Autowired
    lateinit var service: ReportingService

    private fun getListOfReports() = service::class.declaredMemberFunctions.map { it.name }


    @RequestMapping
    fun home() = ModelAndView("reports", mapOf("reports" to getListOfReports()))

    @RequestMapping("/getReport")
    fun getReport(@PathParam("report") report: String): ModelAndView{
        val reportFunc = service::class.declaredMemberFunctions.firstOrNull { it.name == report }
        val result = reportFunc?.call(service) ?: ""
        return ModelAndView("reports", mapOf("reports" to getListOfReports(), "result" to result))
    }


}

