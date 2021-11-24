package com.projectdrgn.callbridgepoc.utils;

import com.alibaba.fastjson.JSONObject;
import com.projectdrgn.callbridgepoc.model.DialStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileUtil {

    public static File recordFile = new File("./data/record.json");

    public static File dialStatusFile = new File("./data/dialStatus.json");

    //写文件
    public static String writeJsonfile(String content, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(("#&" + content).getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    //读取文件
    public static String readJsonFile(File file) {
        String read = null;
        try {
            FileReader filereader = new FileReader(file);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = filereader.read()) != -1) {
                sb.append((char) ch);
            }
            filereader.close();
            read = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read;
    }

    public static Map<String, Object> parseMap(String response) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        String[] strArr = response.split("&");
        for (String str : strArr) {
            String[] kv = str.split("=");
            if (kv.length < 2) {
                map.put(kv[0].toLowerCase(), null);
            } else {
                map.put(kv[0].toLowerCase(), kv[1]);
            }
        }
        return map;
    }

    //读取文件
    public static String getFileSuffix(String suffix) {
        String res = "wav";
        if (suffix.contains("wav")) {
            res = "wav";
        } else if (suffix.contains("ogg")) {
            res = "ogg";
        } else if (suffix.contains("mp3")) {
            res = "mp3";
        } else if (suffix.contains("wma")) {
            res = "wma";
        } else if (suffix.contains("asf")) {
            res = "asf";
        } else if (suffix.contains("rm")) {
            res = "rm";
        } else if (suffix.contains("real")) {
            res = "real";
        } else if (suffix.contains("midi")) {
            res = "midi";
        } else if (suffix.contains("ape")) {
            res = "ape";
        } else if (suffix.contains("vqf")) {
            res = "vqf";
        }
        return res;
    }

    public static String StringEncodeTransfer(String encodeStr) {
        return encodeStr.replaceAll("%3A", ":").replaceAll("%2F", "/")  //过滤URL 包含中文
                .replaceAll("%3F", "?").replaceAll("%3D", "=").replaceAll(
                        "%26", "&").replaceAll("%2B", "+");
    }

    public static void main(String[] args) {
        String input = "";
        JSONObject json = new JSONObject(parseMap(input));
        String content = json.toJSONString();

        System.out.println(content);
        String input22 = "%2B%3A";
        System.out.println(StringEncodeTransfer(input22));

        Map<String, Object> map = (Map) JSONObject.parse(json.toJSONString());

        try {
            DialStatus dialStatus = BeanToMapUtil.map2bean(map, DialStatus.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(RestUtil.sdf.format(new Date()));
//        String url = "";
//        String name = "test1234";
//        RestUtil.download(name,url);
    }
}
