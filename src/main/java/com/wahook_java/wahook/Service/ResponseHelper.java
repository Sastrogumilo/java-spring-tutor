package com.wahook_java.wahook.Service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ResponseHelper {

    private LoggerService logger;

    public ResponseHelper(LoggerService logger){
        this.logger = logger;
    }

    public ResponseEntity<Object> jsonBerhasil(Object value, String route, Object id_controller, Object id_cust, Object req_body, Object req_header) {

        String message  = "OK" ;
        Map<String, Object> data = new HashMap<>();
        data.put("response", value);
        data.put("metadata", Map.of("status", 200, "message", message));

        // Assuming ResponseBerhasil is defined somewhere
        // return ResponseEntity.ok(new ResponseBerhasil(value, data.get("metadata")));

        Map<String, Object> request_log = Map.of("body", req_body, "header", req_header);

        logger.log( route != null ? route : "", 
                    value != null ? value : "", 
                    id_controller != null ? id_controller : "", 
                    id_cust != null ? id_cust : "",
                    request_log != null ? request_log : "");
        // If ResponseBerhasil is not defined, return the map directly
        return ResponseEntity.ok(data);
    }

    public ResponseEntity<Object> jsonBadRequest(Object value, Object route, Object id_controller, Object id_cust, Object req_body, Object req_header) {
        Map<String, Object> data = new HashMap<>();
        data.put("response", new String[0]);
        data.put("metadata", Map.of("status", HttpStatus.BAD_REQUEST.value(), "message", value));

        // Assuming ResponseGagal is defined somewhere
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseGagal(new String[0], data.get("metadata")));

        Map<String, ?> request_log = Map.of("body", req_body, "header", req_header);

        logger.log( route != null ? route : "", 
                    value != null ? value : "", 
                    id_controller != null ? id_controller : "", 
                    id_cust != null ? id_cust : "",
                    request_log != null ? request_log : "");

        // If ResponseGagal is not defined, return the map directly
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(data);
    }
}

