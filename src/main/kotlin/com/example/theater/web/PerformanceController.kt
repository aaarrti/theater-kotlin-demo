package com.example.theater.web


import com.example.theater.data.PerformanceRepository
import com.example.theater.domain.Performance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView


@Controller
@RequestMapping
class PerformanceController() {


    @Autowired
    lateinit var performanceRepository: PerformanceRepository

    @GetMapping
    fun performancesHomePage(): ModelAndView {
        val res = performanceRepository.findAll()
        return ModelAndView("home", "performances", res)
    }


    @RequestMapping("/performances/add")
    fun addPerformance() = ModelAndView("add", "performance", Performance(0, ""))


    @PostMapping("/performances/save")
    fun savePerformance(performance: Performance): String {
        performanceRepository.save(performance)
        return "redirect:/"
    }


}