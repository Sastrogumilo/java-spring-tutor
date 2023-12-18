package com.wahook_java.wahook.Model;

import java.time.LocalDateTime;

public class WahookCompanyDataModel {
    
    private int id;
    private String kode_perusahaan;
    private String nama_perusahaan;
    private String site_id;
    private String alamat;
    private String tanggal_activation;
    private String tanggal_expired;
    private int status;
    private int unlimited;
    private int unlimited_msg;
    private int erp;
    private int noSession;
    private String link_webhook;
    private int use_webhook;
    private LocalDateTime insert_at;
    private String insert_by;
    private String update_at;
    private String update_by;
    private int anti_spam;
    private int use_bot;
    private int as_bridging;
    private String webhook_survey;

    // Getters and setters...

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getKode_perusahaan(){
        return this.kode_perusahaan;
    }

    public void setKode_perusahaan(String kode_perusahaan){
        this.kode_perusahaan = kode_perusahaan;
    }

    public String getNama_perusahaan(){
        return this.nama_perusahaan;
    }

    public void setNama_perusahaan(String nama_perusahaan){
        this.nama_perusahaan = nama_perusahaan;
    }

    public String getSite_id(){
        return this.site_id;
    }

    public void setSite_id(String site_id){
        this.site_id = site_id;
    }

    public String getAlamat(){
        return this.alamat;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getTanggal_activation(){
        return this.tanggal_activation;
    }

    public void setTanggal_activation(String tanggal_activation){
        this.tanggal_activation = tanggal_activation;
    }

    public String getTanggal_expired(){
        return this.tanggal_expired;
    }

    public void setTanggal_expired(String tanggal_expired){
        this.tanggal_expired = tanggal_expired;
    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getUnlimited(){
        return this.unlimited;
    }

    public void setUnlimited(int unlimited){
        this.unlimited = unlimited;
    }

    public int getUnlimited_msg(){
        return this.unlimited_msg;
    }

    public void setUnlimited_msg(int unlimited_msg){
        this.unlimited_msg = unlimited_msg;
    }

    public int getErp(){
        return this.erp;
    }

    public void setErp(int erp){
        this.erp = erp;
    }

    public int getNoSession(){
        return this.noSession;
    }

    public void setNoSession(int noSession){
        this.noSession = noSession;
    }

    public String getLink_webhook(){
        return this.link_webhook;
    }

    public void setLink_webhook(String link_webhook){
        this.link_webhook = link_webhook;
    }

    public int getUse_webhook(){
        return this.use_webhook;
    }

    public void setUse_webhook(int use_webhook){
        this.use_webhook = use_webhook;
    }

    public LocalDateTime getInsert_at(){
        return this.insert_at;
    }

    public void setInsert_at(LocalDateTime insert_at){
        this.insert_at = insert_at;
    }

    public String getInsert_by(){
        return this.insert_by;
    }

    public void setInsert_by(String insert_by){
        this.insert_by = insert_by;
    }

    public String getUpdate_at(){
        return this.update_at;
    }

    public void setUpdate_at(String update_at){
        this.update_at = update_at;
    }

    public String getUpdate_by(){
        return this.update_by;
    }

    public void setUpdate_by(String update_by){
        this.update_by = update_by;
    }

    public int getAnti_spam(){
        return this.anti_spam;
    }

    public void setAnti_spam(int anti_spam){
        this.anti_spam = anti_spam;
    }

    public int getUse_bot(){
        return this.use_bot;
    }

    public void setUse_bot(int use_bot){
        this.use_bot = use_bot;
    }

    public int getAs_bridging(){
        return this.as_bridging;
    }

    public void setAs_bridging(int as_bridging){
        this.as_bridging = as_bridging;
    }

    public String getWebhook_survey(){
        return this.webhook_survey;
    }

}
