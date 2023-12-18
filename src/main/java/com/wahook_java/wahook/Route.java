package com.wahook_java.wahook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import com.wahook_java.wahook.Controller.TestController;
import com.wahook_java.wahook.Service.ResponseHelper;

@RestController
@Service

public class Route {
    @Autowired
    private final ResponseHelper response;
    private final TestController testController;

    public Route(TestController testController, 
                ResponseHelper response

    ){
        this.response = response;
        this.testController = testController;
    }

    @GetMapping("/")
    public Object home(
        @RequestHeader  Map <String, ?> header,
        @RequestParam   Map <String, ?> body
    ){  
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hai sayang kamu lagi ngapain ?");
        data.put("versi", "1.0.0");
        return response.jsonBerhasil(data,  "/", "", "", body, header);
    }

    @GetMapping("/test")
    public Object test(
        @RequestHeader Map<String, ?> header,
        @RequestParam  Map<String, ?> body
    ){
        return testController.testku(header, body);
    }

    @GetMapping("/testsql")
    public Object testsql(
        @RequestHeader  Map<String, ?>  header,
        @RequestParam   Map<String, ?>  body
    ){
        return testController.testsql(header, body);
    }

    @GetMapping("/testAsyncSQL")
    public Object testAsyncSQL(
        @RequestHeader  Map<String, ?>  header,
        @RequestParam   Map<String, ?>  body
    ){
        return testController.testAsyncSQL(header, body);
    }

    @PostMapping("/test_upload_minio")
    public Object testUploadMinio(
        @RequestParam("file")   MultipartFile   file,
        @RequestParam           Map<String, ?>  body,
        @RequestHeader          Map<String, ?>  header
    ){
        
        return testController.testUploadMinio(file, body, header);
    }

    @PostMapping("/testJson")
    public Object testJson(
        // @RequestParam           Map<String, ?>  body
        @RequestHeader          Map<String, ?>  header,
        @RequestBody            Map<String, ?>  body

    ){
        return testController.testRequestJson(body, body);
    }

    @PostMapping("/testLinkMinio")
    public Object testLinkMinio(
        @RequestHeader          Map<String, ?>  header,
        @RequestBody           Map<String, ?>  body

    ){  

        return testController.testLinkMinio(body, header);
    
    }
}
