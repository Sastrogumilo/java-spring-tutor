package com.wahook_java.wahook.Controller;

import org.springframework.stereotype.Component;

import com.wahook_java.wahook.Service.DBService;

@Component
public class HomePageController {

    private DBService koneksi;
    public HomePageController(DBService dbService) {
        this.koneksi = dbService;
        System.out.println("HomePageController");
    }

    public Object testData(){

        Object data = koneksi.query("SELECT * FROM sinarmas_dpmall.user_member WHERE status = 1");
        return data;

    }
}
