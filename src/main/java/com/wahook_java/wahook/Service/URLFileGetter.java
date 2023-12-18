package com.wahook_java.wahook.Service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public class URLFileGetter {
    
    public Map<String, Object> getFileInputStreamFromUrl(String fileUrl) throws IOException {
        //example URL: 
        //https://minio.nexa.net.id/tipsylion/jangan_dihapus_2_1676953484.mp4

        var url = URI.create(fileUrl).toURL();
        URLConnection connection = url.openConnection();
        connection.connect();

        String filename = Paths.get(url.getPath()).getFileName().toString();
        String contentType = connection.getContentType();
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);

        byte[] fileData;
        try (InputStream in = connection.getInputStream()) {
            fileData = in.readAllBytes();
        }

        InputStream fileStream = new ByteArrayInputStream(fileData);

        Map<String, Object> result = new HashMap<>();
        result.put("filename", filename);
        result.put("contentType", contentType);
        result.put("file", fileStream);
        result.put("fileType", fileType);
        

        return result;
    }
}
