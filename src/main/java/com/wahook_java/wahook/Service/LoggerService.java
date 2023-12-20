package com.wahook_java.wahook.Service;

import org.springframework.stereotype.Service;

import serilogj.Log;
import serilogj.LoggerConfiguration;
import serilogj.events.LogEventLevel;

import org.springframework.beans.factory.annotation.Value;

// import serilogj.events.LogEventLevel;
// import static serilogj.sinks.coloredconsole.ColoredConsoleSinkConfigurator.*;
import static serilogj.sinks.seq.SeqSinkConfigurator.*;

//Ndak bisa Dari serilogj sendiri private static, jadinya Confignya dari sini
//bukan di folder config

@Service
public class LoggerService {
    
    @Value("${matadewa.link}")
    private String matadewaLink;

    @Value("${matadewa.apikey}")
    private String matadewaApiKey;

    @Value("${env}")
    private String env;

    @Value("${appname}")
    private String appname;

    public LoggerService() {
        
    }

    public void log(Object fn, Object response, Object id_controller, Object id_cust, Object request){

        Log .setLogger(new LoggerConfiguration()
            // .writeTo(coloredConsole())
            .writeTo(seq(matadewaLink, matadewaApiKey))
            .setMinimumLevel(LogEventLevel.Verbose)
            .createLogger());

        String nama = appname;  
        if(env.equals("dev")){
            nama = nama+"Dev";
        }else{
            nama = nama+"Prod";
        }
        

        Log.information("Project {nama}, Api {controller}, Id_controller {id_controller}, User {idcust}, Request {request}, Log {log}", 
                                                nama, 
                                                fn, 
                                                id_controller, 
                                                id_cust, 
                                                request, 
                                                response);
    }
}
