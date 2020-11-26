package com.wpool.pdd.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wpool.pdd.api.BDOcr;
import com.wpool.pdd.model.Kc_ord;
import com.wpool.pdd.thread.*;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 *
 */
public class GetToken {


   final static Logger log= LogManager.getLogger(GetToken.class);

   // 设备1 device@1453327863  设备2 device@1431207012
    public static final String POST_URL = "http://localhost:8090/TotalControl/v2/devices/device@1453327863/screen/inputs";
    public static final String INPUTTEXT_URL = "http://localhost:8090/TotalControl/v2/devices/device@1453327863/screen/texts";
    public static String token="";

    public static CookieStore cookieStore = new BasicCookieStore();

    public static CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore).build();

    static {
        String tok= HttpGet("http://localhost:8090/TotalControl/v1/login");
        JSONObject jsonObject= (JSONObject) JSON.parse(tok);
        String resValue=jsonObject.getString("value");
        JSONObject jsonValue = (JSONObject) JSON.parse(resValue);
        token = jsonValue.getString("token");
        System.out.println(token);
    }

    public static void main(String[] args) throws InterruptedException {

       /* MyThread1 rab1=new MyThread1();
        Thread t1 = new Thread(rab1);
        t1.start();
        Thread.sleep(1000L);

        MyThread2 rab2=new MyThread2();
        Thread t2 = new Thread(rab2);
        t2.start();
        Thread.sleep(1000L);

        MyThread3 rab3=new MyThread3();
        Thread t3 = new Thread(rab3);
        t3.start();
        Thread.sleep(1000L);

        MyThread4 rab4=new MyThread4();
        Thread t4 = new Thread(rab4);
        t4.start();
        Thread.sleep(1000L);

        MyThread5 rab5=new MyThread5();
        Thread t5 =new Thread(rab5);
        t5.start();*/

      /*  MyThread6 rab6=new MyThread6();
        Thread t6 = new Thread(rab6);
        t6.start();
        Thread.sleep(1000L);*/

        MyThread8 rab8=new MyThread8();
        Thread t8 =new Thread(rab8);
        t8.start();

    }



    @Test
    public void getToken() throws InterruptedException, TesseractException {
       //发送 返回键
       /* String parmSd="token="+token+"&code=backspace&state=press";
        String urlSd="http://localhost:8090/TotalControl/v2/devices/device@462276935/screen/inputs";
        for (int i = 0; i <11 ; i++) {
            Thread.sleep(150L);
            String s = jsonpost(urlSd, parmSd);
            System.out.println(s);
        }*/

        String imageContext = getImageContext("device@1214825973", "device8/", "d3", "[471,275,716,346]");
        System.out.println(imageContext);;

        /*String url="http://localhost:8090/TotalControl/v2/devices/device@1214825973/screen/inputs";
        String mobile="17768119199";
        // 点击 号码输入框
        eventApi(url, token, 660, 433, "press");
        Thread.sleep(2000L);
        eventApi(url, token, 320, 433, "press");
        //输入手机号
        String[] str = mobile.split("");
        for(String m : str) {
            //Thread.sleep(1);
            keyboardToTamll(url,m);
        }
        // 点击 更多面额
        Thread.sleep(2000L);
        eventApi(url, token, 530, 1190, "press");
        // 点击 自定义金额
        Thread.sleep(2000L);
        eventApi(url, token, 888, 1210, "press");
        // 点击 充值金额
        Thread.sleep(2000L);
        keyboardToTamll(url,"5");
        // 点击 收起键盘
        Thread.sleep(2000L);
        eventApi(url, token, 972, 1214, "press");
        // 点击 立即充值
        Thread.sleep(2000L);
        eventApi(url, token, 540, 1580, "press");
        // 点击 确认支付
        Thread.sleep(5000L);
        eventApi(url, token, 540, 1550, "press");
        // 点击 确认付款
        Thread.sleep(5000L);
        eventApi(url, token, 500, 1112, "press");
        // 输入密码
        Thread.sleep(2000L);
        String[] strPwd = "185218".split("");
        for(String m : strPwd) {
            Thread.sleep(1);
            inputDxPwd(url,m);
        }
        // 点击 完成支付
        Thread.sleep(6000L);
        eventApi(url, token, 1005, 140, "press");
        //获取 充值成功截图
        Thread.sleep(8000L);
        String imageContext = getImageContext("device@1214825973", "device8/", "d3", "[471,275,723,352]");
        System.out.println(imageContext);

        //点击 继续充值
        Thread.sleep(3000L);
        eventApi(url, token, 780, 852, "press");*/


    }

    public static boolean authISP(String mobile){
        String clientStr = httpGetClient(mobile);
        String [] suc_str=new String[7];
        suc_str=clientStr.split(",");
        String ISP=suc_str[2].substring((suc_str[2].lastIndexOf(":") + 2), suc_str[2].lastIndexOf("'"));
        //log.info("淘宝接口 "+ISP);
        String sub_str= mobile.substring(0,7);

        Jedis jedis=new Jedis("localhost",6379);
        String hget = jedis.hget("myRedis", sub_str);
        //log.info("redis数据库 "+hget);
        if (StringUtils.isNotBlank(ISP) && StringUtils.equals(ISP,hget)){
            log.info("该号码不是携号转网");
            return true;
        }else {
            log.info("携号转网");
            return false;
        }
    }

    public static String stringToDate(String lo){
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    public static String sendYDPay(String url,String device,String mobile,String facevalue,String uri) throws InterruptedException {
        //点击输入手机号
        inputYDMoble(url,mobile);
        String context ="";
        String res="";
        int count=0;
        //校验输入是否正确，如果出现输入错误，就要重新输入，连续循环判断5次
        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i <5 ; i++) {
            context = getImageContext(device, uri,"d1","[30,310,610,386]");
            if (StringUtils.isNotBlank(context) && StringUtils.equals(context,mobile)){
                res=inputYDPwd(device,url,facevalue,uri);
                break;
            }else {
                count++;
                //如果号码输错了，叉掉以后，重新输入
                log.info("号码输错:"+count+"次，重新输入");
                eventApi(url,token, 900, 1790, "down");
                TimeUnit.SECONDS.sleep(2);
                eventApi(url,token, 900, 1790, "up");
                //重新输入号码
                Thread.sleep(500L);
                inputYDMoble(url,mobile);
                if (count>2){
                    //调用百度ocr，重新识别
                    context = BDOcr.accurateBasic(uri, "d1");
                    log.info("BDOCR " +context);
                    if (StringUtils.isNotBlank(context) && StringUtils.equals(context.trim(),mobile)){
                        res=inputYDPwd(device,url,facevalue,uri);
                        break;
                    }
                }
            }
        }
        return res;
    }

    public static String authHBPay(String url1,String device,String mobile,String parm,String uri) throws InterruptedException {
        String context ="";
        String res="";
        int count=0;
        //校验输入是否正确，如果出现输入错误，就要重新输入，连续循环判断5次
        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i <5 ; i++) {
            context = getImageContext(device, uri,"d3","[30,210,522,300]");
            if (StringUtils.isNotBlank(context) && StringUtils.equals(context,mobile)){
                break;
            }else {
                count++;
                //如果号码输错了，叉掉以后，重新输入
                log.info("号码输错:"+count+"次，重新输入");
                // 点击清除手机号 按钮
                eventApi(POST_URL,token, 797, 300, "press");
                //重新输入号码
                jsonpost(url1, parm);
                Thread.sleep(500L);
                               if (count>2){
                    //调用百度ocr，重新识别
                    context = BDOcr.accurateBasic(uri, "d3");
                    log.info("BDOCR " +context);
                    if (StringUtils.isNotBlank(context) && StringUtils.equals(context.trim(),mobile)){
                        break;
                    }
                }
            }
        }
        return res;
    }

    public static String authTmall(String url1,String device,String mobile,String uri) {
        String context ="";
        int count=0;
        try {
            //校验输入是否正确，如果出现输入错误，就要重新输入，连续循环判断5次
            //TimeUnit.SECONDS.sleep(1);
            for (int i = 0; i <5 ; i++) {
                context = getImageContext(device, uri,"d3","[47,388,306,462]");

                context=context.substring(0,11);
                if (StringUtils.isNotBlank(context) && StringUtils.equals(context,mobile)){
                    log.info("mobile="+context);
                    break;
                }else {
                    count++;
                    //如果号码输错了，叉掉以后，重新输入
                    log.info("号码输错:"+count+"次，重新输入");
                    //清除重新输入号码手
                    cleanAndInputMobile(url1,device,mobile);
                    Thread.sleep(500L);
                    if (count>3){
                        //调用百度ocr，重新识别
                        context = BDOcr.accurateBasic(uri, "d3");
                        log.info("BDOCR="+context);
                        if (StringUtils.isNotBlank(context) && StringUtils.equals(context.trim(),mobile)){
                            //log.info("BDOCR="+context);
                            break;
                        }
                    }
                }
            }

        }catch (Exception e){
            log.info(e.fillInStackTrace());
        }
        return context;
    }

    public static String sendDXPay(String url,String device,String mobile,String facevalue,String uri) throws InterruptedException {
        //点击输入手机号
        inputDXMoble(url,mobile);
        TimeUnit.SECONDS.sleep(1);
        String context ="";
        String res="";
        int count=0;
        //校验输入是否正确，如果出现输入错误，就要重新输入,连续循环判断5次
        for (int i = 0; i <5 ; i++) {
            context = getImageContext(device, uri,"d1","[400,610,700,710]");
            context=context.substring(0,11);
            if (StringUtils.isNotBlank(context) && StringUtils.equals(context,mobile)){
                res=inputDXPWD(device,url,facevalue,uri);
                break;
            }else {
                count++;
                //如果号码输错了，叉掉以后，重新输入
                log.info("号码输错:"+count+"次，重新输入");
                eventApi(url,token, 910, 1820, "down");
                TimeUnit.SECONDS.sleep(2);
                eventApi(url,token, 910, 1820, "up");
                //重新输入号码
                Thread.sleep(500L);
                inputDXMoble(url,mobile);
                if (count>2){
                    //调用百度ocr，重新识别
                    context = BDOcr.accurateBasic(uri, "d1");
                    log.info("BDOCR " +context);
                    if (StringUtils.isNotBlank(context) && StringUtils.equals(context.trim(),mobile)){
                        res=inputYDPwd(device,url,facevalue,uri);
                        break;
                    }
                }
            }
        }
        return res;

    }

    public static void findCoordinate(String url,String m) throws InterruptedException {
        if (StringUtils.isNotBlank(m)){
            Thread.sleep(100L);
            switch (m){
                case "1":
                    Thread.sleep(2000L);
                    eventApi(url,token, 180, 1360, "press");
                    break;
                case "2":
                    Thread.sleep(300L);
                    eventApi(url,token, 540, 1360, "press");
                    break;
                case "3":
                    Thread.sleep(300L);
                    eventApi(url,token, 900, 1360, "press");
                    break;
                case "4":
                    Thread.sleep(300L);
                    eventApi(url,token, 180, 1500, "press");
                    break;
                case "5":
                    Thread.sleep(300L);
                    eventApi(url,token, 540, 1500, "press");
                    break;
                case "6":
                    Thread.sleep(300L);
                    eventApi(url,token, 900, 1500, "press");
                    break;
                case "7":
                    Thread.sleep(300L);
                    eventApi(url,token, 180, 1645, "press");
                    break;
                case "8":
                    Thread.sleep(300L);
                    eventApi(url,token, 540, 1645, "press");
                    break;
                case "9":
                    Thread.sleep(300L);
                    eventApi(url,token, 900, 1645, "press");
                    break;
                case "0":
                    Thread.sleep(300L);
                    eventApi(url,token, 540, 1800, "press");
                    break;
            }
        }else {
            log.info("手机号不能为空");
        }
    }

    public static void findDXPFace(String url,String value) throws InterruptedException {
        if (StringUtils.isNotBlank(value)) {
            switch (value){
                case "1":
                    Thread.sleep(400L);
                    eventApi(url,token, 130, 1410, "press");
                    break;
                case "2":
                    Thread.sleep(400L);
                    eventApi(url,token, 400, 1410, "press");
                    break;
                case "3":
                    Thread.sleep(400L);
                    eventApi(url,token, 670, 1410, "press");
                    break;
                case "4":
                    Thread.sleep(400L);
                    eventApi(url,token, 130, 1550, "press");
                    break;
                case "5":
                    Thread.sleep(400L);
                    eventApi(url,token, 550, 1480, "press");
                    break;
                case "6":
                    Thread.sleep(400L);
                    eventApi(url,token, 900, 1480, "press");
                    break;
                case "7":
                    Thread.sleep(400L);
                    eventApi(url,token, 180, 1630, "press");
                    break;
                case "8":
                    Thread.sleep(400L);
                    eventApi(url,token, 540, 1640, "press");
                    break;
                case "9":
                    Thread.sleep(400L);
                    eventApi(url,token, 900, 1640, "press");
                    break;
                case "10":
                    Thread.sleep(400L);
                    eventApi(url,token, 180, 1290, "press");
                    Thread.sleep(400L);
                    eventApi(url,token, 540, 1830, "press");
                    break;
                case "20":
                    Thread.sleep(400L);
                    eventApi(url,token, 540, 1290, "press");
                    Thread.sleep(400L);
                    eventApi(url,token, 540, 1830, "press");
            }
        }else {
            log.info("面值不能为空");
        }
    }

    public static void findFaceValue(String url,String value) throws InterruptedException {
        if (StringUtils.isNotBlank(value)) {
            switch (value){
                case "1":
                    Thread.sleep(400L);
                    eventApi(url,token, 130, 1410, "press");
                    break;
                case "2":
                    Thread.sleep(400L);
                    eventApi(url,token, 400, 1410, "press");
                    break;
                case "3":
                    Thread.sleep(400L);
                    eventApi(url,token, 670, 1410, "press");
                    break;
                case "4":
                    Thread.sleep(400L);
                    eventApi(url,token, 130, 1550, "press");
                    break;
                case "5":
                    Thread.sleep(400L);
                    eventApi(url,token, 400, 1550, "press");
                    break;
                case "6":
                    Thread.sleep(400L);
                    eventApi(url,token, 670, 1550, "press");
                    break;
                case "7":
                    Thread.sleep(400L);
                    eventApi(url,token, 130, 1700, "press");
                    break;
                case "8":
                    Thread.sleep(400L);
                    eventApi(url,token, 400, 1700, "press");
                    break;
                case "9":
                    Thread.sleep(400L);
                    eventApi(url,token, 670, 1700, "press");
                    break;
                case "10":
                    Thread.sleep(400L);
                    eventApi(url,token, 130, 1410, "press");
                    Thread.sleep(400L);
                    eventApi(url,token, 400, 1830, "press");
                    break;
                case "20":
                    Thread.sleep(400L);
                    eventApi(url,token, 400, 1410, "press");
                    Thread.sleep(400L);
                    eventApi(url,token, 400, 1830, "press");
            }
        }else {
            log.info("面值不能为空");
        }
    }




    public static String getImageContext(String device,String path,String picName,String rect){    //device@1453327863
        String url="http://localhost:8090/TotalControl/v2/devices/"+device+"/screen/images?token="+token+"&location=pc&file=D:/picture/"+path+picName+".jpg&rect="+rect+"&type=jpg";

        String url1="http://localhost:8090/TotalControl/v2/devices/"+device+"/screen/images?token="+token+"&location=pc&file=D:/picture/"+path+picName+".jpg&type=jpg";
        String res = HttpGet(url);
        //System.out.println(res);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ITesseract instance = new Tesseract();
        //如果未将tessdata放在根目录下需要指定绝对路径
        //instance.setDatapath("the absolute path of tessdata");
        //如果需要识别英文之外的语种，需要指定识别语种，并且需要将对应的语言包放进项目中
        instance.setLanguage("chi_sim");
        //instance.setLanguage("eng");
        // 指定识别图片
        File imgDir = new File("D:/picture/"+path+picName+".jpg");
        long startTime = System.currentTimeMillis();
        String ocrResult = null;
        try {
            ocrResult = instance.doOCR(imgDir);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        //去空格
        String retrim="";
        if (StringUtils.isNotBlank(ocrResult)){
            retrim=ocrResult.replaceAll("\\s*", "");
        }

        // 输出识别结果
        log.info("OCR Result: " + retrim + " 耗时："+(endTime - startTime)/1000+"s");
        return retrim;
    }

    public static void inputYDMoble(String url,String mobile) throws InterruptedException {
        //定位 输入框
        eventApi(url,token, 200, 350, "press");
        Thread.sleep(1000L);
        //开始输入 手机号码
        TimeUnit.SECONDS.sleep(1);
        String[] str = mobile.split("");
        for(String m : str) {
            //Thread.sleep(1);
            findCoordinate(url,m);
        }
    }

    public static void inputDXMoble(String url,String mobile) throws InterruptedException {
        //定位 输入框
        TimeUnit.SECONDS.sleep(1);
        eventApi(url,token, 540, 640, "press");
        Thread.sleep(1000L);
        //再次 定位
        eventApi(url,token, 540, 640, "press");
        //开始输入 手机号码
        TimeUnit.SECONDS.sleep(1);
        String[] str = mobile.split("");
        for(String m : str) {
            //Thread.sleep(1);
            findCoordinate(url,m);
        }
    }





    public static  String inputYDPwd(String device,String url,String facevalue,String uri) throws InterruptedException {

        //点击 更多面额
        eventApi(url,token, 190, 1110, "press");
        //输入 自定义面值
        Thread.sleep(500L);
        findFaceValue(url,facevalue.substring(0,facevalue.indexOf(".")));
        //点击 立即充值
        Thread.sleep(1000L);
        eventApi(url,token, 955, 1695, "press");
        //点击 确认支付
        Thread.sleep(5000L);
        eventApi(url,token, 750, 1130, "press");

        //点击 立即支付
        /*Thread.sleep(3000L);    //4000L
        eventApi(url,token, 530, 1490, "press");
        Thread.sleep(1000L);
        //如果弹出支付框，点击叉
        eventApi(url, token,940, 1840, "press");*/

        log.info("输入支付密码");
        payPwd(url);

        //点击 微信支付  完成
        TimeUnit.SECONDS.sleep(1L);
        String d2con = getImageContext(device, uri,"d2", "[190,220,720,320]");

        if (StringUtils.isNotBlank(d2con) && StringUtils.equals("支付成功",d2con)){
            //System.out.println("点击 支付成功....");
            eventApi(url,token, 545, 1645, "press");
        }else {
            d2con = getImageContext(device,uri, "d2", "[190,220,720,320]");
            eventApi(url,token, 545, 1645, "press");
        }

        //点击 "确定" 支付成功
        TimeUnit.SECONDS.sleep(1L);
        String d3con = getImageContext(device,uri, "d3", "[260,570,770,740]");

        if (StringUtils.isNotBlank(d3con) && StringUtils.equals("订单已提交充值",d3con)){
            eventApi(url,token, 540, 1610, "press");
            log.info("完成充值");

        }else {
            d3con = getImageContext(device, uri,"d3", "[260,570,770,740]");
            eventApi(url,token, 540, 1610, "press");
        }
        return d3con;
    }

    public static String inputDXPWD(String device,String url,String facevalue,String uri) throws InterruptedException {
        //点击 收起键盘
        Thread.sleep(1000L);
        eventApi(url,token, 540, 1160, "press");
        //点击 输入自定义面值
        Thread.sleep(2000L);
        eventApi(url,token, 530, 1280, "press");
        //输入 自定义面值
        Thread.sleep(2000L);
        findDXPFace(url,facevalue.substring(0,facevalue.indexOf(".")));
        //点击 收起键盘
        Thread.sleep(1000L);
        eventApi(url,token, 540, 1170, "press");
        //点击 立即支付
        Thread.sleep(2000L);
        eventApi(url,token, 540, 1600, "press");
        //再次点击 立即支付
        Thread.sleep(8000L);    //8000L
        System.out.println("点击 立即支付------");
        eventApi(url,token, 520, 930, "press");


        //点击 确认支付（如果有弹窗）
        Thread.sleep(3000L);    //4000L
        eventApi(url, token,750, 1130, "press");
       /* Thread.sleep(1000L);
        //如果弹出支付框，点击叉
        eventApi(url, token,940, 1840, "press");*/

        log.info("输入支付密码");
        payPwd(url);

        //点击 微信支付  完成
        TimeUnit.SECONDS.sleep(3);
        String d2con = getImageContext(device, uri,"d2", "[190,220,720,320]");

        if (StringUtils.isNotBlank(d2con) && StringUtils.equals("支付成功",d2con)){
            //System.out.println("点击 支付成功....");
            eventApi(url,token, 545, 1645, "press");
        }else {
            d2con = getImageContext(device,uri, "d2", "[190,220,720,320]");
            eventApi(url,token, 545, 1645, "press");
        }

        //点击 "确定" 支付成功
        TimeUnit.SECONDS.sleep(1);
        String d3con = getImageContext(device,uri, "d3", "[380,830,660,1130]");

        if (StringUtils.isNotBlank(d3con) && StringUtils.equals("订单已提交充值",d3con)){
            eventApi(url,token, 530, 1080, "press");
            log.info("完成充值");

        }else {
            d3con = getImageContext(device, uri,"d3", "[380,830,660,1130]");
            eventApi(url,token, 530, 1080, "press");
        }
        return d3con;
    }

    public static int insertMobile( String mobile ,String facevalue,String id){
        Kc_ord mble=new Kc_ord();
        mble.setPhone(mobile);
        mble.setOdid(id);
        mble.setPfacevalue(facevalue);
        mble.setStatus(1);  //状态1 充值中，2 成功，3 失败
        int saveCount=0;
        try {
            saveCount = DBUtil.save(mble);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveCount;
    }

    public static int updateMobile(String sno,String status,String odid){
        String sql="update kc_ord set sno=?,status=? where odid=?";
        PreparedStatement ps=null;
        int flag=0;
        try {
            ps=DBUtil.createStatement(sql);
            ps.setString(1,sno);
            ps.setString(2,status);
            ps.setString(3,odid);
            flag=ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static void payPwd(String url) throws InterruptedException {
        //输入支付密码，1
        TimeUnit.SECONDS.sleep(4);
        eventApi(url,token, 197, 1418, "press");//1
        //输入支付密码，8
        TimeUnit.SECONDS.sleep(1);
        eventApi(url,token, 542, 1682, "press");//8
        //输入支付密码，5
        TimeUnit.SECONDS.sleep(1);
        eventApi(url,token, 397, 1565, "press");//5
        //输入支付密码，2
        TimeUnit.SECONDS.sleep(1);
        eventApi(url,token, 550, 1377, "press");//2
        //输入支付密码，1
        TimeUnit.SECONDS.sleep(1);
        eventApi(url,token, 170, 1372, "press");//1
        //输入支付密码，8
        TimeUnit.SECONDS.sleep(1);
        eventApi(url,token, 542, 1682, "press");//8
    }


    public static String hbPay10086(String url,String device,String mobile,String facevalue,String uri){
        String d1="";
        //eventApi(POST_URL,token, 100, 1063, "press");
        try {
            TimeUnit.SECONDS.sleep(1L);
            // 点击清除手机号 按钮
            eventApi(POST_URL,token, 797, 300, "press");
            TimeUnit.SECONDS.sleep(1L);
            // 点击清除手机号 按钮
            eventApi(POST_URL,token, 791, 300, "press");
            TimeUnit.SECONDS.sleep(1L);
            // 点击手机号码框
            eventApi(POST_URL,token, 330, 258, "press");
            TimeUnit.SECONDS.sleep(1L);
            String parm="token="+token+"&text="+mobile;  //13851993971,15051885736
            String url1="http://127.0.0.1:8090/TotalControl/v2/devices/"+device+"/screen/texts";
            // 输入手机号
            jsonpost(url1, parm);
            //验证 输入手机号
            d1 = authHBPay(url1, device, mobile, parm, uri);

            TimeUnit.SECONDS.sleep(1L);
            // 点击，输入自定义金额
            eventApi(url,token, 175, 920, "press");
            TimeUnit.SECONDS.sleep(1L);
            String parm1="token="+token+"&text="+facevalue;
            // 输入金额
            jsonpost(url1, parm1);
            TimeUnit.SECONDS.sleep(1L);
            String d2 = getImageContext(device, uri, "d2", "[42,1079,227,1176]");
            //System.out.println(d2);
            TimeUnit.SECONDS.sleep(1L);
            //点击 充值
            if (StringUtils.isNotBlank(d2) && StringUtils.equals("全网折扣",  d2)){
                eventApi(url,token, 531, 1396, "press");
            }else {
                eventApi(url,token, 531, 1215, "press");
            }
            TimeUnit.SECONDS.sleep(2L);
            //点击 确认付款
            eventApi(url,token, 500, 1750, "press");

            TimeUnit.SECONDS.sleep(2L);
            //输入密码
            eventApi(url,token, 180, 1300, "press"); //1
            TimeUnit.SECONDS.sleep(1L);
            eventApi(url,token, 535, 1642, "press"); //8
            TimeUnit.SECONDS.sleep(2L);
            eventApi(POST_URL,token, 535, 1480, "press"); //5
            TimeUnit.SECONDS.sleep(1L);
            eventApi(url,token, 535, 1300, "press"); //2
            TimeUnit.SECONDS.sleep(2L);
            eventApi(url,token, 180, 1300, "press"); //1
            TimeUnit.SECONDS.sleep(1L);
            eventApi(url,token, 535, 1642, "press"); //8
            TimeUnit.SECONDS.sleep(8L);
            //获得 充值成功截图
            d1= getImageContext(device, uri, "d1", "[921,90,1047,155]");
            //System.out.println(d1);
            TimeUnit.SECONDS.sleep(2L);
            //点击 完成
            eventApi(url,token, 995, 122, "press");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return d1;
    }
    public static String tmallPay(String url,String device,String mobile,String facevalue,String uri) throws InterruptedException {
        String ocres="";
        //清除 并输入手机号码
        cleanAndInputMobile(url,device,mobile);
        //TimeUnit.SECONDS.sleep(1L);
        //确认 输入手机号是否正确
        authTmall(url, device, mobile, uri);
        //选择充值金额
        if (StringUtils.isNotBlank(facevalue) && StringUtils.equals("10", facevalue)){
            TimeUnit.SECONDS.sleep(1L);
            eventApi(url, token, 180, 630, "press");
            //点击 立即充值
            TimeUnit.SECONDS.sleep(1L);
            eventApi(url, token, 860, 430, "press");
        }else if (StringUtils.isNotBlank(facevalue) && StringUtils.equals("20", facevalue)){
            TimeUnit.SECONDS.sleep(1L);
            eventApi(url, token, 525, 618, "press");
            //点击 立即充值
            TimeUnit.SECONDS.sleep(1L);
            eventApi(url, token, 860, 430, "press");
        }
       /* //点击 立即充值
        TimeUnit.SECONDS.sleep(1L);
        eventApi(url, token, 860, 430, "press");
        TimeUnit.SECONDS.sleep(1L);
        eventApi(url, token, 860, 430, "press");*/
        //确认订单，再点击即充值
        TimeUnit.SECONDS.sleep(2L);
        eventApi(url, token, 480, 1015, "press");
        //点击 立即支付
        TimeUnit.SECONDS.sleep(4L);
        eventApi(url, token, 500, 1720, "press");
        //输入 密码
        TimeUnit.SECONDS.sleep(3L);
        eventApi(url, token, 185, 1360, "press"); // 1
        TimeUnit.SECONDS.sleep(1L);
        eventApi(url, token, 530, 1672, "press"); // 8
        TimeUnit.SECONDS.sleep(1L);
        eventApi(url, token, 530, 1516, "press"); // 5
        TimeUnit.SECONDS.sleep(2L);
        eventApi(url, token, 530, 1360, "press"); // 2
        TimeUnit.SECONDS.sleep(1L);
        eventApi(url, token, 185, 1360, "press"); // 1
        //eventApi(url, token, 185, 1360, "up"); // 1
        TimeUnit.SECONDS.sleep(2L);
        eventApi(url, token, 530, 1672, "press"); // 8
        //eventApi(url, token, 530, 1672, "up"); // 8
        // 获取 支付成功图片
        TimeUnit.SECONDS.sleep(3L);
        ocres = getImageContext(device, uri, "d1", "[185,288,398,365]");
        for (int i = 0; i <15 ; i++) {
            if (StringUtils.isNotBlank(ocres) && StringUtils.equals("支付成功",ocres)){
                //向上返回一级
                Thread.sleep(500L);
                backUp(device);
                //eventApi(url, token, 70, 135, "press");
                //向上返回一级到，充值页面
                Thread.sleep(500L);
                backUp(device);
                //eventApi(url, token, 60, 135, "press");
                break;
            }else {
                Thread.sleep(500L);
                ocres = getImageContext(device, uri, "d1", "[185,288,398,365]");
            }
        }
        return ocres;
    }

    public static String dxApp(String url,String device,String mobile,String facevalue,String uri){
        String imageContext="";
        try {
            eventApi(url, token, 660, 433, "press");
            Thread.sleep(2000L);
            eventApi(url, token, 320, 433, "press");
            //输入手机号
            String[] str = mobile.split("");
            for(String m : str) {
                //Thread.sleep(1);
                keyboardToTamll(url,m);
            }
            // 点击 更多面额
            Thread.sleep(2000L);
            eventApi(url, token, 530, 1190, "press");
            // 点击 自定义金额
            Thread.sleep(2000L);
            eventApi(url, token, 888, 1210, "press");
            // 点击 充值金额
            Thread.sleep(2000L);
            keyboardToTamll(url,facevalue);
            // 点击 收起键盘
            Thread.sleep(2000L);
            eventApi(url, token, 972, 1214, "press");
            // 点击 立即充值
            Thread.sleep(2000L);
            eventApi(url, token, 540, 1580, "press");
            // 点击 确认支付
            Thread.sleep(3000L);
            eventApi(url, token, 540, 1550, "press");
            // 点击 确认付款
            Thread.sleep(5000L);
            eventApi(url, token, 500, 1112, "press");
            // 输入密码
            Thread.sleep(3000L);
            String[] strPwd = "185218".split("");
            for(String m : strPwd) {
                Thread.sleep(1);
                inputDxPwd(url,m);
            }
            // 点击 完成支付
            Thread.sleep(5000L);
            eventApi(url, token, 1005, 140, "press");
            //获取 充值成功截图
            imageContext = getImageContext(device, uri, "d8", "[471,275,723,352]");
            System.out.println(imageContext);

            //点击 继续充值
            Thread.sleep(3000L);
            eventApi(url, token, 780, 852, "press");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return imageContext;
    }

    public static void cleanAndInputMobile(String url,String device,String mobile){
        // 点击 输入手机框
        try {
            //TimeUnit.SECONDS.sleep(1L);
            eventApi(url, token, 296, 431, "press");
            String parmSd="token="+token+"&code=backspace&state=press";
            String urlSd="http://localhost:8090/TotalControl/v2/devices/"+device+"/screen/inputs";
            //清除 号码输入框
            TimeUnit.SECONDS.sleep(1L);
            for (int i = 0; i <12 ; i++) {
                Thread.sleep(150L);
                jsonpost(urlSd, parmSd);
            }
            eventApi(url, token, 296, 431, "press");
            jsonpost(urlSd, parmSd);
            //输入手机号
            /*String parm="token="+token+"&text="+mobile;  //13851993971,15051885736
            String urlIn="http://127.0.0.1:8090/TotalControl/v2/devices/"+device+"/screen/texts";
            TimeUnit.SECONDS.sleep(1L);
            jsonpost(urlIn, parm);*/
            Thread.sleep(100L);
            eventApi(url, token, 300, 1836, "press");

            //开始输入 手机号码
            //TimeUnit.SECONDS.sleep(1);
            String[] str = mobile.split("");
            for(String m : str) {
                //Thread.sleep(1);
                keyboardToTamll(url,m);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void threadEex(String url,String devices,String uri,String threadNum){
        String mobile = "";
        String facevalue="";
        String id="";
        String ISPType="";
        String resp="充值成功";

        for ( ; ; ){
            String timestr= String.valueOf(System.currentTimeMillis());
            //log.info(stringToDate(timestr));
            //下单接口
            String respstr = epayForkachi("P160708172012320", timestr, "002", "U100","T001","afb93858aa300bbdac2f54f1da882743");
            //String respstr = httpURLConnectionPOST("P160708172012320", timestr, "002", "afb93858aa300bbdac2f54f1da882743");
            //System.out.println(respstr);
            JSONObject jsonObject= (JSONObject) JSON.parse(respstr);
            String resultCode=jsonObject.getString("resultCode");
            if (StringUtils.equals("T00008", resultCode)){
                log.info("系统无订单...");
                try {
                    TimeUnit.SECONDS.sleep(20L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                mobile = jsonObject.getString("zcaccount");
                facevalue=jsonObject.getString("facevalue");
                id=jsonObject.getString("id");
                ISPType=jsonObject.getString("type3");
                log.info(threadNum+":"+id+" "+mobile+" "+facevalue+" "+ISPType);
                facevalue=facevalue.substring(0,facevalue.indexOf("."));
                //携号转网验证，如果是携号转网，就不处理，显示处理中
                boolean bl = authISP(mobile);
                if (bl && Integer.valueOf(facevalue)>=5){    //没有转转网
                    //订单入库
                    int saveCount = insertMobile(mobile, facevalue, id);

                    if (saveCount>0){
                        //发送充值
                        try {
                            //判断是移动(U100) 电信(U300) 联通(U200)
                            if (StringUtils.isNotBlank(ISPType) && StringUtils.equals("U100", ISPType)){
                                //移动
                                resp = tmallPay(url, devices, mobile, facevalue, uri);

                            }else if (StringUtils.isNotBlank(ISPType) && StringUtils.equals("U300", ISPType)){
                                //电信
                                resp = dxApp(url, devices, mobile, facevalue, uri);

                            }else {
                                //联通

                            }

                            if (StringUtils.isNotBlank(resp) && StringUtils.equals("支付成功", resp) ||StringUtils.equals("充值成功", resp)){
                                log.info("payStr"+threadNum +resp);
                                //回调接口
                                String callBack = callBackToKC(id, "88888", "2", "已充值");
                                log.info(threadNum+":"+id+" 回调 "+callBack);
                                //更新订单状态
                                int flag = updateMobile("99999", "2",id);
                                if (flag>0){
                                    log.info(id+" 订单状态更新成功!");
                                }
                            }else {
                                log.info(threadNum+" 充值出错....");
                                break;
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }else {     //携号转网
                    //回调接口
                    try {
                        TimeUnit.SECONDS.sleep(5L);
                        String callBack = callBackToKC(id, "000000", "3", "充值失败");
                        log.info(id+" 回调 "+callBack);
                        //更新订单状态
                        int flag = updateMobile("99999", "3",id);
                        if (flag>0){
                            log.info(id+" 订单状态更新成功!");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void backUp(String dev){
        //发送 返回键
        try {
            String parmSd="token="+token+"&code=back&state=press";
            String urlSd="http://localhost:8090/TotalControl/v2/devices/"+dev+"/screen/inputs";
                String s = jsonpost(urlSd, parmSd);
                //System.out.println(s);
                TimeUnit.SECONDS.sleep(1L);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void keyboardToTamll(String url,String p){
        //System.out.println(p);
        //System.out.println(url);
        try {
            if (StringUtils.isNotBlank(p)){
                Thread.sleep(100L);
                switch (p){
                    case "1":
                        Thread.sleep(1000L);
                        eventApi(url,token, 300, 1360, "press");
                        break;
                    case "2":
                        Thread.sleep(150L);
                        eventApi(url,token, 540, 1360, "press");
                        break;
                    case "3":
                        Thread.sleep(150L);
                        eventApi(url,token, 780, 1360, "press");
                        break;
                    case "4":
                        Thread.sleep(150L);
                        eventApi(url,token, 300, 1520, "press");
                        break;
                    case "5":
                        Thread.sleep(150L);
                        eventApi(url,token, 540, 1520, "press");
                        break;
                    case "6":
                        Thread.sleep(150L);
                        eventApi(url,token, 780, 1520, "press");
                        break;
                    case "7":
                        Thread.sleep(150L);
                        eventApi(url,token, 300, 1675, "press");
                        break;
                    case "8":
                        Thread.sleep(150L);
                        eventApi(url,token, 540, 1675, "press");
                        break;
                    case "9":
                        Thread.sleep(150L);
                        eventApi(url,token, 780, 1675, "press");
                        break;
                    case "0":
                        Thread.sleep(150L);
                        eventApi(url,token, 540, 1830, "press");
                        break;
                    case "10":
                        Thread.sleep(150L);
                        eventApi(url,token, 300, 1360, "press");
                        Thread.sleep(150L);
                        eventApi(url,token, 540, 1830, "press");
                        break;
                    case "20":
                        Thread.sleep(150L);
                        eventApi(url,token, 540, 1360, "press");
                        Thread.sleep(150L);
                        eventApi(url,token, 540, 1830, "press");
                        break;
                }
            }else {
                log.info("手机号不能为空");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void inputDxPwd(String url,String p){
        System.out.println(p);
        try {
            if (StringUtils.isNotBlank(p)){
                Thread.sleep(1000L);
                switch (p){
                    case "1":
                        Thread.sleep(2000L);
                        eventApi(url,token, 300, 1360, "down");
                        Thread.sleep(500L);
                        eventApi(url,token, 300, 1360, "up");
                        break;
                    case "2":
                        Thread.sleep(1500L);
                        eventApi(url,token, 540, 1360, "down");
                        Thread.sleep(500L);
                        eventApi(url,token, 540, 1360, "up");
                        break;

                    case "5":
                        Thread.sleep(2000L);
                        eventApi(url,token, 540, 1520, "down");
                        Thread.sleep(500L);
                        eventApi(url,token, 540, 1520, "up");
                        break;
                    case "8":
                        Thread.sleep(1500L);
                        eventApi(url,token, 540, 1689, "down");
                        Thread.sleep(500L);
                        eventApi(url,token, 540, 1689, "up");
                        break;
                }
            }else {
                log.info("手机号不能为空");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }








    public static void epay10010() throws InterruptedException {

        //点击 沃钱包app
        String result = jsonpost(POST_URL,"&token=M2rBU4Xl1gs5QslJ&x=680&y=1360&state=press");
        TimeUnit.SECONDS.sleep(2);
        //点击 通信缴费
        String result1 = jsonpost(POST_URL,"&token=M2rBU4Xl1gs5QslJ&x=120&y=800&state=press");
        TimeUnit.SECONDS.sleep(2);
        //点击 输入手机号
        String result2 = jsonpost(POST_URL,"&token=M2rBU4Xl1gs5QslJ&x=628&y=433&state=press");
        TimeUnit.SECONDS.sleep(2);
        //点击 清空手机号
        String result3 = jsonpost(POST_URL,"&token=M2rBU4Xl1gs5QslJ&x=1010&y=436&state=press");
        TimeUnit.SECONDS.sleep(2);
        //输入手机号 18651602096  13166021231 13276675363
        String result4 = jsonpost(INPUTTEXT_URL,"&token=M2rBU4Xl1gs5QslJ&text=13276675363");
        TimeUnit.SECONDS.sleep(2);
        //点击 输入面值
        String result5 = jsonpost(POST_URL,"&token=M2rBU4Xl1gs5QslJ&x=100&y=1000&state=press");
        TimeUnit.SECONDS.sleep(2);
        //输入 面值
        String result6 = jsonpost(INPUTTEXT_URL,"&token=M2rBU4Xl1gs5QslJ&text=5");
        TimeUnit.SECONDS.sleep(5);
        //点击 立即充值
        String result7 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=550&y=1060&state=press");
        TimeUnit.SECONDS.sleep(15);
        System.out.println("点击 立即支付");
        //点击 立即支付  (面值为1元的时候坐标，无折扣显示)
        //String result8 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=550&y=1055&state=press");
        //点击 立即支付  (面值大于1元的时候坐标，因为有一个折扣显示)
        String result19 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=540&y=1365&state=press");
        //输入 支付密码，4
        TimeUnit.SECONDS.sleep(5);
        String result9 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=180&y=1444&state=press");
        //输入 支付密码，0
        TimeUnit.SECONDS.sleep(1);
        String result10 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=540&y=1815&state=press");
        //输入 支付密码，8
        TimeUnit.SECONDS.sleep(1);
        String result11 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=540&y=1635&state=press");
        //输入 支付密码，1
        TimeUnit.SECONDS.sleep(1);
        String result12 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=180&y=1265&state=press");
        //输入 支付密码，8
        TimeUnit.SECONDS.sleep(1);
        String result13 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=540&y=1635&state=press");
        //输入 支付密码，2
        TimeUnit.SECONDS.sleep(1);
        String result14 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=530&y=1265&state=press");

        //关闭 弹框广告
        TimeUnit.SECONDS.sleep(5);
        String result15 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=540&y=1400&state=press");

        //点击完成
        TimeUnit.SECONDS.sleep(1);
        String result16 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=916&y=360&state=press");
        //点击 继续充值
        TimeUnit.SECONDS.sleep(2);
        String result18 = jsonpost(POST_URL,"&token=V35WOAcVnX4L1glp&x=780&y=1180&state=press");
        System.out.println(result16+"继续充值");
    }


    /**
     * 调用Post接口
     */
    public static String httpURLConnectionPOST(String userid, String timestr, String type, String sign) {
        StringBuilder sb=null;
        try {
            String key = MD5HEX(userid + timestr + sign, "utf-8");
            URL url = new URL("http://open.kp-api.com/ms/getKMOrderInfoAPI");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.connect();
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            String param = "userid="+userid+"&"+"timestr="+timestr+"&"+"type1="+type+"&"+"key="+key;
            dataout.writeBytes(param);
            System.out.println(param);
            dataout.flush();
            dataout.close();
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            sb = new StringBuilder(); // 用来存储响应数据

            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                //sb.append(bf.readLine());
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
            connection.disconnect(); // 销毁连接
            System.out.println(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String eventApi(String url,String token, int x, int y, String state) {
        String str = "";
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("token", token));
        list.add(new BasicNameValuePair("x", String.valueOf(x)));
        list.add(new BasicNameValuePair("y", String.valueOf(y)));
        list.add(new BasicNameValuePair("state", state));
        return str = httpPost(url, list);
    }

    public static String HttpGet(String url) {
        //将账号密码BASE64编码
        String encode= Base64.encodeBase64String("sigma:E2711518".getBytes());
        RequestConfig requestConfig = RequestConfig.
                custom().
                setConnectTimeout(20000).
                setConnectionRequestTimeout(20000).
                setSocketTimeout(20000).
                setRedirectsEnabled(true).build();
        HttpGet httpGet2 = new HttpGet(url);
        httpGet2.setHeader("Authorization",encode);
        httpGet2.setConfig(requestConfig);
        String srtResult = null;
        int StatusCode = 404;
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet2);
            StatusCode = httpResponse.getStatusLine().getStatusCode();
            //System.out.println("StatusCode "+StatusCode);
            String str1;
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                srtResult = EntityUtils.toString(httpResponse.getEntity());
                return srtResult;
            }
            srtResult = EntityUtils.toString(httpResponse.getEntity());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String httpPost(String url, List<BasicNameValuePair> list) {
        RequestConfig requestConfig = RequestConfig.
                custom().
                setConnectTimeout(20000).
                setConnectionRequestTimeout(20000).
                setSocketTimeout(50000).
                setRedirectsEnabled(true).build();
        HttpPost httpPost = new HttpPost(url);
        //保持长连接
        httpPost.setHeader("connection", "keep-alive");
        httpPost.setConfig(requestConfig);
        String strResult = "";
        int StatusCode = 404;
        CloseableHttpResponse httpResponse = null;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "gbk");
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null) {
                StatusCode = httpResponse.getStatusLine().getStatusCode();
                //logger.info("StatusCode: "+StatusCode);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                    return strResult;
                }
                strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                //System.out.println(strResult);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("异常信息:" + e.fillInStackTrace());
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String jsonpost(String url, String args) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求头属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行   否则会抛异常（java.net.ProtocolException: cannot write to a URLConnection if doOutput=false - call setDoOutput(true)）
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流并开始发送参数
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            //添加参数
            out.write(args);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    //synchronized
    public  static synchronized String epayForkachi(String userid, String timestr, String type1, String type3,String tid,String sign){

        String str = "";
        String key = MD5HEX(userid + timestr + sign, "utf-8");
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("userid", userid));
        list.add(new BasicNameValuePair("timestr", timestr));
        list.add(new BasicNameValuePair("type1", type1));
        list.add(new BasicNameValuePair("type3", type3));
        list.add(new BasicNameValuePair("tid", tid));
        list.add(new BasicNameValuePair("key", key));
        str = httpPost("http://open.kp-api.com/ms/getKMOrderInfoAPI", list);
        //String strURL="http://open.kp-api.com/ms/getKMOrderInfoAPI"+"?"+userid+"&"+timestr+"&"+type+"&"+key;
        //str = HttpGet(strURL);

        return str;
    }

    public static String MD5HEX(String str, String type) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(str.getBytes(type));
            digesta = alga.digest();
        } catch (java.security.NoSuchAlgorithmException ex) {
            System.out.println("非法摘要算法");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byte2hex(digesta);
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs;
            }
        }
        return hs;
    }

    public static String callBackToKC(String odid, String sno, String status, String msg){

        String res = "";
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("odid", odid));
        list.add(new BasicNameValuePair("sno", sno));
        list.add(new BasicNameValuePair("status", status));
        list.add(new BasicNameValuePair("msg", msg));
        res = httpPost("http://open.kp-api.com/ms/callback/kmzyreback", list);
        return res;
    }

    public static String httpGetClient(String tel){
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="+tel;
        String strResult=null;
        try {
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                //jsonResult = JSONObject.fromObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                System.out.println("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            System.out.println("get请求提交失败:" + url);
        }
        return strResult;
    }




    @Test
    public void testUnit(){

        String timestr= String.valueOf(System.currentTimeMillis());
        System.out.println(timestr);
        //下单接口
        String respstr = epayForkachi("P160708172012320", timestr, "002", "U100","T002","afb93858aa300bbdac2f54f1da882743");
        System.out.println(respstr);

        JSONObject jsonObject= (JSONObject) JSON.parse(respstr);
        String resultCode=jsonObject.getString("resultCode");
        if (StringUtils.equals("T00008", resultCode)){
            System.out.println("无订单!");
        }else {
            String mobile = jsonObject.getString("zcaccount");
            String facevalue=jsonObject.getString("facevalue");
            String id=jsonObject.getString("id");
            String type3=jsonObject.getString("type3");
            System.out.println(id+" "+mobile+" "+facevalue+" "+resultCode+" "+type3);

            //回调接口
            /*String callBack = callBackToKC(id, "ka5855354854", "2", "已充值");
            System.out.println(callBack);*/

        }

        //回调接口
        /*String callBack = callBackToKC("D12011121002501488", "ka5855354854", "3", "已充值");
        System.out.println(callBack);*/







    }
}
