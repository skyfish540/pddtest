package com.wpool.pdd.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

/**
 *
 */
public class AliyunApi {

    public static void main(String[] args) {
        String res = aliCqJianJiao("F:/pdd/image/test5.png");
        //String res = alicSzart("F:/pdd/image/test5.png");
        System.out.println(res);
    }

    //深圳艾科瑞特科技有限公司
    public static String alicSzart(String filePath){
        //API产品路径
        String requestUrl = "https://codevirify.market.alicloudapi.com/icredit_ai_image/verify_code/v1";
        //阿里云APPCODE
        String appcode = "c5f9dc558bdc4724bd4e7f4dd8b91bca";
        String verify_code=null;
        CloseableHttpClient httpClient = null;
        try{
            httpClient = HttpClients.createDefault();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // 装填参数
            if (true) {
                //启用BASE64编码方式进行识别
                //内容数据类型是BASE64编码
                String imgBase64 = picTrunBase64Str(filePath);
                params.add(new BasicNameValuePair("IMAGE", imgBase64));
                params.add(new BasicNameValuePair("IMAGE_TYPE", "0"));
            } else {
                //启用URL方式进行识别
                //内容数据类型是图像文件URL链接
                params.add(new BasicNameValuePair("IMAGE", "https://icredit-api-market.oss-cn-hangzhou.aliyuncs.com/%E9%AA%8C%E8%AF%81%E7%A0%81.jpg"));
                params.add(new BasicNameValuePair("IMAGE_TYPE", "1"));
            }
            // 创建一个HttpGet实例
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader("Authorization","APPCODE " + appcode);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            // 设置请求参数
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            // 发送GET请求
            HttpResponse execute = httpClient.execute(httpPost);
            // 获取状态码
            int statusCode = execute.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            // 获取结果
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            JSONObject jsonObj = (JSONObject) JSON.parse(result);
            String verify_code_entity = jsonObj.getString("VERIFY_CODE_ENTITY");
            JSONObject jObj= (JSONObject) JSON.parse(verify_code_entity);
            verify_code = jObj.getString("VERIFY_CODE");
            System.out.println(result);
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return verify_code;
    }


    //重庆尖叫网络科技有限公司
    public static String aliCqJianJiao(String filePath){
        String host = "https://yzmplus.market.alicloudapi.com";
        String path = "/ysyzm";
        String method = "POST";
        String appcode = "c5f9dc558bdc4724bd4e7f4dd8b91bca";
        String v_code ="";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        String base64Str = picTrunBase64Str(filePath);
        bodys.put("v_color", "如：蓝色");
        bodys.put("v_pic", base64Str);
        bodys.put("v_type", "col");

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //获取response的body
            String respStr=EntityUtils.toString(response.getEntity());
            JSONObject jsonObject= (JSONObject) JSON.parse(respStr);
            v_code = jsonObject.getString("v_code");

            System.out.println(respStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v_code;
    }

    //将图片转成base64编码字符串格式
    public static String picTrunBase64Str(String imgFile){
        String imgBase64 = "";
        try {
            File file = new File(imgFile);
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(encodeBase64(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgBase64;
    }
}
