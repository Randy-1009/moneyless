package com.example.moneyless.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpUpload {

    public static String UploadUserDataByPost(String id,String password,String budget,String phone,String username,String sex){
        String address = "http://10.249.86.29:8080/userUpload";
        String result = "";
        try{
            URL url = new URL(address);//初始化URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//请求方式

            //超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);

            //我们请求的数据
            String data = "id="+ URLEncoder.encode(id,"UTF-8")+
                    "&password="+ URLEncoder.encode(password,"UTF-8")+
                    "&budget="+ URLEncoder.encode(budget,"UTF-8")+
                    "&phone="+ URLEncoder.encode(phone,"UTF-8")+
                    "&username="+ URLEncoder.encode(username,"UTF-8")+
                    "&sex="+ URLEncoder.encode(sex,"UTF-8");

            //获取输出流
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
                result = new String(message.toByteArray());
                //return result;
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    public static String UploadWasteBookByPost(String id,String userid,String category_id,String w_type,String category_icon,String create_time,String note,String amount,String category){
        String address = "http://10.249.86.29:8080/wasteBookUpload";
        String result = "";

        try{
            URL url = new URL(address);//初始化URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//请求方式

            //超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);

            //我们请求的数据
            String data = "id="+ URLEncoder.encode(id,"UTF-8")+
                    "&userid="+ URLEncoder.encode(userid,"UTF-8")+
                    "&category_id="+ URLEncoder.encode(category_id,"UTF-8")+
                    "&w_type="+ URLEncoder.encode(w_type,"UTF-8")+
                    "&category_icon="+ URLEncoder.encode(category_icon,"UTF-8")+
                    "&create_time="+ URLEncoder.encode(create_time,"UTF-8")+
                    "&note="+ URLEncoder.encode(note,"UTF-8")+
                    "&amount="+ URLEncoder.encode(amount,"UTF-8")+
                    "&category="+ URLEncoder.encode(category,"UTF-8");

            //获取输出流
            OutputStream out = conn.getOutputStream();
            conn.connect();
            out.write(data.getBytes());
            out.flush();
            out.close();


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
                result = new String(message.toByteArray());
                //return result;
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;

    }
    public static String UploadCategoryByPost(String category_id,String userid,String category_name,String category_icon,String category_order,String category_type){
        String address = "http://10.249.86.29:8080/userUploadcategory";
        String result = "";

        try{
            URL url = new URL(address);//初始化URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//请求方式

            //超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);
            //我们请求的数据
            String data = "category_id="+ URLEncoder.encode(category_id,"UTF-8")+
                    "&userid="+ URLEncoder.encode(userid,"UTF-8")+
                    "&category_name="+ URLEncoder.encode(category_name,"UTF-8")+
                    "&category_icon="+ URLEncoder.encode(category_icon,"UTF-8")+
                    "&category_order="+ URLEncoder.encode(category_order,"UTF-8")+
                    "&category_type="+ URLEncoder.encode(category_type,"UTF-8");

            //获取输出流
            OutputStream out = conn.getOutputStream();
            conn.connect();
            out.write(data.getBytes());
            out.flush();
            out.close();


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
                result = new String(message.toByteArray());
                //return result;
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;

    }
}