package com.wahook_java.wahook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoutePage {
    
    @GetMapping(value = "/index", produces = "text/html")
    public String welcomePage(){
        return "Home/index";
    }

    
}
