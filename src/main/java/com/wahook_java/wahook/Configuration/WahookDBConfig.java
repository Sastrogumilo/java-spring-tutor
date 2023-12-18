package com.wahook_java.wahook.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "datasourceconfig.wahook")
public class WahookDBConfig {
    private String url;
    private String username;
    private String password;

    /**
     * Entah kenapa bisa jalan, padahal di tutorialnya gak ada ini
     * contoh jika anda ganti nama method yang getUsername menjadi getUser
     * padahal nama variabelnya tetap username, maka akan balikan null,
     * kalau di chatGPT disuruh menggunakan 'JavaBeans naming conventions'.
     * misal variabelnya user tapi methodnya getUsername, maka 
     * dapat di akali dengan:
        @JsonProperty("username")
        private String user;
     * 
     * tapi methodnya tetap getUsername
     * 
     */

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
