package com.wahook_java.wahook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wahook_java.wahook.Controller.HomePageController;

@Controller
public class RoutePage {

    private HomePageController  homePageController;

    public RoutePage(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
    
    @GetMapping(value = "/index", produces = "text/html")
    public String welcomePage(){
        return "Home/index";
    }

    @GetMapping(value = "/test", produces = "text/html")
    public String testPage(Model model){
        model.addAttribute("data", (Object) homePageController.testData());
        return "Home/test";
    }
}
