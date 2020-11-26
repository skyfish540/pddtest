package com.wpool.pdd.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;

/**
 *
 */
public class BDOcr {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String accurateBasic(String path,String pic) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        try {
            // 本地文件路径
            String filePath = "D:/picture/"+path+pic+".jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            //"access_token":"24.774be2860d5e7d73782de0c8cd78d5d9.2592000.1608172727.282335-22993656"
            String accessToken = "24.774be2860d5e7d73782de0c8cd78d5d9.2592000.1608172727.282335-22993656";
            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);

            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            String resultWords=jsonObject.getString("words_result");
            //转成数组格式,从数组中去数据
            JSONArray words_result = JSONArray.parseArray(resultWords);
            String strWords= words_result.getString(0);
            //获取json数据
            JSONObject jsonWords= (JSONObject) JSON.parse(strWords);
            String word=jsonWords.getString("words");
            return word;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String s = BDOcr.accurateBasic("device0/","d5");
        if (StringUtils.equals("13947099612".trim(), s)){
            System.out.println("相等.....");
        }else {
            System.out.println("不相等****");
        }
        System.out.println(s);
    }
}
