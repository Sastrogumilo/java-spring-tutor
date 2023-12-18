package com.wahook_java.wahook.Controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wahook_java.wahook.Model.WahookCompanyDataModel;
import com.wahook_java.wahook.Service.DBService;
import com.wahook_java.wahook.Service.MinioService;
import com.wahook_java.wahook.Service.ResponseHelper;
import com.wahook_java.wahook.Service.URLFileGetter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;


@Service
@Repository
public class TestController {
    
    private final ResponseHelper response;
    private final DBService koneksi;
    private final MinioService minioService;

    public TestController(
        DBService koneksi,
        MinioService minioService,
        ResponseHelper response
        ){
        this.response = response;
        this.koneksi = koneksi;
        this.minioService = minioService;
    }

    public Object testku(
        @RequestHeader  Map<String, ?> header,
        @RequestParam   Map<String, ?> body
    ){
        return response.jsonBerhasil("test", "", "", "", body, header);
    }

    /**
     * Test SQL
     * Test ini untuk mengetahui untuk menyederhanakan eksekusi query di java,
     * Jadinya balikan model dapat langsung dijadikan object atau langsung dijadikan data model
     */
    public Object testsql(  @RequestHeader  Map<String, ?> header,
                            @RequestBody   Map<String, ?> body
    ){
        String query = "SELECT * FROM sinarmas_dpmall.ms_kelurahan mk LIMIT 100  "; //1.5 GB data

        String query_wahook = "SELECT * FROM nexa_wahook.ms_perusahaan";

        try{

            // Object data_query = koneksi.query(query); //bisa begini 
            List<Map<String, Object>> data_query = koneksi.query(query); //atau begini
            List<WahookCompanyDataModel> data_query_wahook = koneksi.query_wahook(query_wahook); //atau begini

            //Dari semua contoh yang ditulis di atas, alhamdulillah puji Tuhan semua bisa digunakan

            Object hasil_akhir = Map.of(
                "from_db_main", data_query,
                "from_db_wahook", data_query_wahook
            );  

            return response.jsonBerhasil(hasil_akhir, "", "", "", "", header);

        }catch(Exception e){
            return response.jsonBadRequest(e.getMessage(), "", "", body, "", header);
        }

        /**
         * AutoCannon Report:
        Running 10s test @ http://192.168.20.100:6969/testsql
        10 connections
        ┌─────────┬───────┬───────┬────────┬────────┬──────────┬──────────┬─────────┐
        │ Stat    │ 2.5%  │ 50%   │ 97.5%  │ 99%    │ Avg      │ Stdev    │ Max     │
        ├─────────┼───────┼───────┼────────┼────────┼──────────┼──────────┼─────────┤
        │ Latency │ 18 ms │ 59 ms │ 169 ms │ 210 ms │ 69.44 ms │ 64.86 ms │ 1088 ms │
        └─────────┴───────┴───────┴────────┴────────┴──────────┴──────────┴─────────┘
        ┌───────────┬────────┬────────┬─────────┬─────────┬─────────┬────────┬────────┐
        │ Stat      │ 1%     │ 2.5%   │ 50%     │ 97.5%   │ Avg     │ Stdev  │ Min    │
        ├───────────┼────────┼────────┼─────────┼─────────┼─────────┼────────┼────────┤
        │ Req/Sec   │ 203    │ 203    │ 314     │ 326     │ 294.1   │ 35.21  │ 203    │
        ├───────────┼────────┼────────┼─────────┼─────────┼─────────┼────────┼────────┤
        │ Bytes/Sec │ 823 kB │ 823 kB │ 1.27 MB │ 1.32 MB │ 1.19 MB │ 143 kB │ 823 kB │
        └───────────┴────────┴────────┴─────────┴─────────┴─────────┴────────┴────────┘

        Req/Bytes counts sampled once per second.
        # of samples: 11

        3k requests in 11.01s, 13.1 MB read

        */
    }

    /**
     * Test Async SQL
     * Test ini untuk mengetahui apakah async sql lebih cepat dari sql biasa
     */
    // it simmilar with async await in nodeJS, but not fully asyncronous
    public Object testAsyncSQL( @RequestHeader  Map<String, ?> header,
                                @RequestParam   Map<String, ?> body
    ){
        String query = "SELECT * FROM sinarmas_dpmall.ms_kelurahan mk LIMIT 100 ";

        String query_wahook = "SELECT * FROM nexa_wahook.ms_perusahaan";
        
        
        CompletableFuture<Object> data_query = koneksi.queryAsync(query);
        CompletableFuture<List<WahookCompanyDataModel>> data_query_wahook = koneksi.queryAsyncWahook(query_wahook);
        
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(data_query, data_query_wahook);

        try {
            // Block until all futures complete
            allFutures.join();
        
            // Combine the results
            HashMap<String, Object> combinedMap = new HashMap<>();
            combinedMap.put("data_query", data_query.join());
            combinedMap.put("data_query_wahook", data_query_wahook.join());
        
            // Store the result in an Object variable
            Object hasil = combinedMap;
        
            return response.jsonBerhasil(hasil, "", "", "", body, header);
        } catch (Exception ex) {
            return response.jsonBadRequest(ex.getMessage(), "", "", "", body, header);
        }
        
        /**
         * Test with autocannon
         */

    }


    // I tried to be fully asyncronous, but using thenApply 
    //to continue processing asynchronously without blocking.
    public Object tesAsyncSQLV2(
        @RequestHeader  Map<String, ?> header,
        @RequestParam   Map<String, ?> body
    ){

        String query = "SELECT * FROM sinarmas_dpmall.ms_kelurahan mk LIMIT 100 ";
        String query_wahook = "SELECT * FROM nexa_wahook.ms_perusahaan";

        CompletableFuture<Object> data_query = koneksi.queryAsync(query);
        CompletableFuture<List<WahookCompanyDataModel>> data_query_wahook = koneksi.queryAsyncWahook(query_wahook);

        return CompletableFuture.allOf(data_query, data_query_wahook)
        .thenApply(v -> {
            HashMap<String, Object> combinedMap = new HashMap<>();
            combinedMap.put("data_query", data_query.join());
            combinedMap.put("data_query_wahook", data_query_wahook.join());
            return combinedMap;
        })
        .handle((combinedMap, ex) -> {
            if (ex != null) {
                return response.jsonBadRequest(ex.getMessage(), "", "", "", body, header);
            } else {
                return response.jsonBerhasil(combinedMap, "", "", "", body, header);
            }
        });
    }
    /**
     * Test Upload Minio
     * Test untuk Upload Minio, entah kenapa kalau di windows minio service
     * tidak bisa dijalankan, jadi saya menggunakan minio yang linux
     */

    public Object testUploadMinio(  MultipartFile file, 
                                    Map<String, ?> body, 
                                    Map<String, ?> header){

        long currTimestamp = Instant.now().getEpochSecond();
        String filename =   body.get("filename") != null ? 
                            body.get("filename").toString() : 
                            currTimestamp + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1) ;
        
        String contentType = file.getContentType();

        /**
         *  Contoh saya mensetting value dan sanitasi di Java
            kalau di nodeJS jadinya seperti ini
            data_body: {
                bucket: body.bucket || "",
                body1: body.body1 || "",
                body2: body.body2 || ""
            }
         */

        Map<String, Object> data_body = new HashMap<>();
        data_body.put("bucket", body.get("bucket") != null ? body.get("bucket") : "");
        // data_body.put("body1", body.get("body1") != null ? body.get("body1") : "");
        // data_body.put("body2", body.get("body2") != null ? body.get("body2") : "");

        
        for (String key : data_body.keySet()) {
            String value = data_body.get(key).toString();
            if (value == null || value.isEmpty()) {
                return response.jsonBadRequest(key + " cannot be empty", "", "", "", body, header);
            }
        }
        // System.err.println(data_body);
        // return response.jsonBerhasil(data_body, "");

        try (InputStream content = file.getInputStream()) {
            Object link = minioService.uploadFile(filename, content, data_body.get("bucket").toString(), contentType);
            return response.jsonBerhasil(link, "", "", "", body, header);
        } catch (IOException e) {
            e.printStackTrace();
            return response.jsonBadRequest(e.getMessage(), "", "", "", body, header);
        }
    }

    public Object testRequestJson(
        @RequestHeader  Map<String, ?> header,
        @RequestParam   Map<String, ?> body
    ){

        return response.jsonBerhasil(body,  "/testJson", "", "", body, header);
    }

    public Object testLinkMinio(
        Map<String, ?> body,
        Map<String, ?> header

    ){
        String link_api = "/testLinkMinio";
        final URLFileGetter fileService;
        fileService = new URLFileGetter();
        Map<String, Object> data_body = new HashMap<>();
        data_body.put("link", body.get("link") != null ? body.get("link") : "");
        data_body.put("bucket", body.get("bucket") != null ? body.get("bucket") : "");
        
        for (String key : data_body.keySet()) {
            String value = data_body.get(key).toString();
            if (value == null || value.isEmpty()) {
                return response.jsonBadRequest(key + " cannot be empty", "", "", "", body, header);
            }
        }

        data_body.put("filename", body.get("filename") != null ? body.get("filename") : ""); //if empty get UNIX timestamp
        
    
        try {
            
            Map<String, Object> file = fileService.getFileInputStreamFromUrl(data_body.get("link").toString());
            //Upload ke minio
            String filename = data_body.get("filename") != ""   ? data_body.get("filename").toString() + "." +file.get("fileType").toString()
                                                                    : file.get("filename").toString(); 

            Object link_minio = minioService.uploadFile(
                                                        filename, 
                                                        (InputStream) file.get("file"), 
                                                        "wahook", 
                                                        file.get("contentType").toString());
            
            return response.jsonBerhasil(link_minio, "/testLinkMinio", "", "", body, header);
        } catch (IOException e) {
            e.printStackTrace();

            return response.jsonBadRequest("Gagal upload filr", link_api, "", "", data_body, header);
        }
    }
}
