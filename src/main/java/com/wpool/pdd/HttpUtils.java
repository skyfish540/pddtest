package com.wpool.pdd;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class HttpUtils {
    public static String postOutApi(String url, String reqType, Map<String, Object> map, String encoding){
        String reqParamJson= JSON.toJSONString(map);
        System.out.println(reqParamJson);

        HttpURLConnection urlConnection=null;
        OutputStream os=null;
        InputStream is=null;
        StringBuffer sb=null;

        try {
            URL reqUrl= new URL(url);
            urlConnection= (HttpURLConnection) reqUrl.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");//设置请求方式为POST
            urlConnection.setDoOutput(true);//允许写出
            urlConnection.setDoInput(true);//允许读入
            urlConnection.setUseCaches(false);//不使用缓存
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("Connection","Keep-Alive");
            //设置参数类型是json格式
            //urlConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            //连接网络。请求行，请求头的设置必须放在网络连接前
            urlConnection.connect();
            if (reqType.toLowerCase()=="json"){
                urlConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                os=urlConnection.getOutputStream();
                os.write(reqParamJson.getBytes(encoding));
            }else {
                os=urlConnection.getOutputStream();
                os.write(pinJieMapValues(map).getBytes(encoding));
            }

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                is=urlConnection.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                sb= new StringBuffer();
                String str=null;
                while ((str=br.readLine())!=null){
                    sb.append(str);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //销毁Http连接
            urlConnection.disconnect();
            try {
                //关闭输入输出流
                os.close();
               is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String pinJieMapValues(Map<String,Object> map){
        StringBuffer sb=new StringBuffer();
        if (map!=null){
            Iterator<Map.Entry<String,Object>> iterator=map.entrySet().iterator();
            String key="";
            String value="";
            while (iterator.hasNext()){
                Map.Entry<String,Object> entry=iterator.next();
                key=entry.getKey();
                value= (String) entry.getValue();
                //System.out.println(key+"="+value);
                sb.append(key);
                sb.append("=");
                sb.append(value);
                if (iterator.hasNext()){
                    sb.append("&");
                }
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
