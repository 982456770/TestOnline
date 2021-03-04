package com.testonline.tools;

import com.testonline.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import sun.rmi.runtime.Log;


public class Urlpath {
    public static String urlPath="http://192.168.3.10:8080/examination_war_exploded/";
    public static String id="";

    public static final String TAG="连接线程";
    public String connect(String str,String urlpath){
        String urlPath= Urlpath.urlPath+urlpath;
        Log.d(TAG,"启动线程");
        String msg = "";
        try {
            //初始化URL
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("POST");
            //设置超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            //设置允许输入
            conn.setDoInput(true);
            //设置允许输出
            conn.setDoOutput(true);
            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);
            Log.d(TAG,"发送的数据："+str);
            //我们请求的数据
            String data = "str="+ URLEncoder.encode(str,"UTF-8");
            //獲取輸出流
            Log.d(TAG,"编码后："+data);
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            conn.connect();
            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                msg = new String(message.toByteArray());
                Log.d(TAG,"解码前:"+msg);
                msg= URLDecoder.decode(msg,"utf-8");
                Log.d(TAG,"解码后:"+msg);
                return msg;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(MainActivity.TAG,"exit");
        return msg;
    }
}
