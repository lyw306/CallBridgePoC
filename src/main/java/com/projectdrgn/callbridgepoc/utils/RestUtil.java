package com.projectdrgn.callbridgepoc.utils;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RestUtil {
    public static Logger log = LoggerFactory.getLogger(RestUtil.class);

    public static RestTemplate template = new RestTemplate();

    public static File directory = new File("./audio");

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static void download(String fileName, String url) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "tempfile" + "-" + sdf.format(new Date());
        }
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
        ResponseEntity<byte[]> response = template.exchange(url, HttpMethod.GET,
                httpEntity, byte[].class);
        log.info("===状态码================");
        log.info(">> {}", response.getStatusCode());
        log.info("===返回信息================");
        log.info(">> {}", response.getHeaders().getContentType());
        log.info(">> {}", response.getHeaders().getContentType().getSubtype());
        try {
            File file = File.createTempFile(fileName, "." + FileUtil.getFileSuffix(response.getHeaders().getContentType().getSubtype()), directory);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(response.getBody());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
