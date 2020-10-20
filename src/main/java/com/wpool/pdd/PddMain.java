package com.wpool.pdd;

import java.io.IOException;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import com.wpool.pdd.util.RandTest;
import com.wpool.pdd.util.TestBase64Image;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class PddMain {

    final static Logger logger = Logger.getLogger(PddMain.class);

    private static ChromeDriver driver = null;

    public static CookieStore cookieStore = new BasicCookieStore();

    public static CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore)
            .setRetryHandler(new HttpRequestRetryHandler() {
                //NoHttpResponseException 和连接超时重试
                @Override
                public boolean retryRequest(IOException exception,
                                            int executionCount,
                                            HttpContext httpContext) {
                    if (executionCount > 3) {
                        logger.warn("Maximum tries reached for client http pool ");
                        return false;
                    }
                    if (exception instanceof NoHttpResponseException ||
                            exception instanceof ConnectTimeoutException ||
                            exception instanceof SocketException) {
                        logger.warn("No response from server on " + executionCount + " call");
                        return true;
                    }
                    return false;
                }
            }).build();

    static {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeUtil chromeUtil = new ChromeUtil();
        chromeUtil.setDevelopmentMode(true);
        chromeUtil.setNoPassword(true);
        chromeUtil.setHeightWithWidth(Integer.valueOf(1920), Integer.valueOf(1080));
        driver = chromeUtil.getChorme();
        String jsString = "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})";
        try {
            driver.executeScript(jsString, new Object[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String HttpGet(String url) {
        RequestConfig requestConfig = RequestConfig.
                custom().
                setConnectTimeout(20000).
                setConnectionRequestTimeout(20000).
                setSocketTimeout(20000).
                setRedirectsEnabled(true).build();
        HttpGet httpGet2 = new HttpGet(url);
        httpGet2.setConfig(requestConfig);
        String srtResult = null;
        int StatusCode = 404;
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet2);
            StatusCode = httpResponse.getStatusLine().getStatusCode();
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
                logger.info(strResult);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("异常信息:" + e.fillInStackTrace());
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


    public static String getxmlvalue(String keyname, String srcstr) {
        if (srcstr.indexOf("<" + keyname + ">") >= 0) {
            srcstr = srcstr.substring(srcstr.indexOf("<" + keyname + ">") + keyname
                    .length() + 2, srcstr
                    .indexOf("</" + keyname + ">"));
        } else {
            srcstr = "";
        }
        return srcstr;
    }

    public static String getmidstr(String keystr1, String keystr2, String src) {
        String str = "";
        try {
            if ((src.indexOf(keystr1) < 0) || (src.indexOf(keystr2) < 0)) {
                return "";
            }
            int s = src.indexOf(keystr1) + keystr1.length();
            String tmpstr = src.substring(s, src.length());
            int d = tmpstr.indexOf(keystr2);
            str = tmpstr.substring(0, d);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String gettimestr(String sdf) {
        String v_ymd = null;
        try {
            java.util.Date currTime = new java.util.Date();
            SimpleDateFormat yymmdd = new SimpleDateFormat(sdf, java.util.Locale.US);

            yymmdd.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
            v_ymd = new String(yymmdd.format(currTime).getBytes("iso-8859-1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v_ymd;
    }

    public static String paykachiapi(String userid, String key, String pfacevalue, String type,
                                     String mobile, String bizid) {
        String str = "";

        String sign = MD5HEX(userid + mobile + pfacevalue + key, "utf-8");
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("notifyurl", "http://www.baidu.com"));
        list.add(new BasicNameValuePair("userid", userid));
        list.add(new BasicNameValuePair("pfacevalue", pfacevalue));
        list.add(new BasicNameValuePair("type", type));
        list.add(new BasicNameValuePair("mobile", mobile));
        list.add(new BasicNameValuePair("bizid", bizid));
        list.add(new BasicNameValuePair("key", sign));
        return str = httpPost("http://open.kp-api.com/ms/sendFlowOrderAPI", list);
    }

    public static String kaChiApiByCode(String userid, String key, String productid,
                                        String mobile, String bizid){
        String respStr =null;
        String sign = MD5HEX(userid + mobile + productid + key, "utf-8");
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("notifyurl", "http://www.baidu.com"));
        list.add(new BasicNameValuePair("userid", userid));
        list.add(new BasicNameValuePair("productid", productid));
        list.add(new BasicNameValuePair("mobile", mobile));
        list.add(new BasicNameValuePair("bizid", bizid));
        list.add(new BasicNameValuePair("key", sign));

        return respStr = httpPost("http://open.kp-api.com/ms/sendFlowOrderAPI", list);
    }


    public static String getPfacevalueAndTpye(String s, String oid, String mNo) {
        String pfacevalue = "";
        String type = "";
        String payStr="";

        if ((s.indexOf("充值10/元") >= 0) || (s.indexOf("快充10/元") >= 0)) {
            pfacevalue = "10";
            type = "002";
        } else if ((s.indexOf("充值20/元") >= 0) || (s.indexOf("快充20/元") >= 0)) {
            pfacevalue = "20";
            type = "002";
        } else if ((s.indexOf("充值30/元") >= 0) || (s.indexOf("快充30/元") >= 0)) {
            pfacevalue = "30";
            type = "002";
        } else if ((s.indexOf("充值50/元") >= 0) || (s.indexOf("快充50/元") >= 0)) {
            pfacevalue = "50";
            type = "002";
        } else if ((s.indexOf("充值100/元") >= 0) || (s.indexOf("快充100/元") >= 0)) {
            pfacevalue = "100";
            type = "002";
        } else if ((s.indexOf("充值200/元") >= 0) || (s.indexOf("快充200/元") >= 0)) {
            pfacevalue = "200";
            type = "002";
        } else if ((s.indexOf("充值300/元") >= 0) || (s.indexOf("快充300/元") >= 0)) {
            pfacevalue = "300";
            type = "002";
        } else if ((s.indexOf("充值500/元") >= 0) || (s.indexOf("快充500/元") >= 0)) {
            pfacevalue = "500";
            type = "002";
        }
        logger.info("手机号:"+mNo + " ,面值:" + pfacevalue);

        if ((!pfacevalue.equals("")) && (!mNo.equals("")) && (!oid.equals(""))) {
            //P42765   b5191d9fe885e389132a9314e8adbe33
            //P19737   da86892ffe296161cb2d5690a7dbbb10
            //通过面值提交
             payStr = paykachiapi("P42765",
                    "b5191d9fe885e389132a9314e8adbe33",
                    pfacevalue,
                    type,
                    mNo,
                    oid);
            logger.info("面值提交结果:" + payStr);
        }else {
            //通过编码提交
            payStr = sendPayByCode(s, oid, mNo);
        }
        return payStr;
    }


    public static String sendPayByCode(String s, String oid, String mNo){
        String productid = "";
        String payStr="";

        if (s.indexOf("江苏移动流量充值1G") >= 0) {
            productid = "0010106001024";
        } else if (s.indexOf("江苏移动流量充值2G") >= 0) {
            productid = "0010106002048";
        } else if (s.indexOf("江苏移动流量充值3G") >= 0) {
            productid = "0010106003072";
        } else if (s.indexOf("江苏移动流量充值5G") >= 0) {
            productid = "0010106005120";
        } else if (s.indexOf("江苏移动流量充值6G") >= 0) {
            productid = "0010106006144";
        } else if (s.indexOf("江苏移动流量充值10G") >= 0) {
            productid = "00101060010240";
        } else if (s.indexOf("江苏移动流量充值15G") >= 0) {
            productid = "00101060015360";
        } else if (s.indexOf("江苏移动流量充值30G") >= 0) {
            productid = "00101060030720";
        }else if (s.indexOf("作业帮VIP会员月卡") >= 0){
            productid = "90701010020";
        }else if (s.indexOf("作业帮会员季卡")>=0){
            productid = "90701010056";
        }else if (s.indexOf("作业帮VIP会员年卡")>=0){
            productid = "907010100198";
        }else if (s.indexOf("全国联通流量包【当月有效】,100M流量包")>=0){
            productid = "002010100100";
        }else if (s.indexOf("全国联通流量包【当月有效】,200M流量包")>=0){
            productid = "002010100200";
        }else if (s.indexOf("全国联通流量包【当月有效】,300M流量包")>=0){
            productid = "002010100300";
        }else if (s.indexOf("全国联通流量包【当月有效】,1G流量包")>=0){
            productid = "0020101001024";
        }else if (s.indexOf("全国联通流量100M充值")>=0){
            productid = "002010100100";
        }else if (s.indexOf("全国联通流量200M充值")>=0){
            productid = "002010100200";
        }else if (s.indexOf("全国联通流量300M充值")>=0){
            productid = "002010100300";
        }else if (s.indexOf("全国联通流量1G充值")>=0){
            productid = "0020101001024";
        }else if (s.indexOf("全国电信流量100M充值")>=0){
            productid = "003010100100";
        }else if (s.indexOf("全国电信流量200M充值")>=0){
            productid = "003010100200";
        }else if (s.indexOf("全国电信流量500M充值")>=0){
            productid = "003010100500";
        }else if (s.indexOf("全国电信流量2G充值")>=0){
            productid = "0030101002048";
        }else if (s.indexOf("全国电信流量包【当月有效】,100M流量包")>=0){
            productid = "003010100100";
        }else if (s.indexOf("全国联通流量包【当月有效】,500M流量包")>=0){
            productid = "003010100500";
        }else if (s.indexOf("全国联通流量包【当月有效】,2G流量包")>=0){
            productid = "0030101002048";
        }else if (s.indexOf("全国联通流量包【当月有效】,6G流量包")>=0){
            productid = "0030101006144";
        }else if (s.indexOf("全国联通流量包【当月有效】,10G流量包")>=0){
            productid = "00301010010240";
        }else if (s.indexOf("PLUS未来会员")>=0){
            productid = "120010100200";
        }

        logger.info(mNo + " ," + productid);

        if ((!productid.equals("")) && (!mNo.equals("")) && (!oid.equals(""))) {

            payStr = kaChiApiByCode("P43707",
                    "551353623992ce7fec3f3b091fb360e7",
                    productid,
                    mNo,
                    oid);
            logger.info("编码提交结果:" + payStr);
        }
        return payStr;
    }

    public static void sliderCheck(WebDriver driver,String s,int count){
        //logger.info(Jsoup.parse(ps));
        String originalUrl = Jsoup.parse(s).select("[class=slider-img-bg]").first().attr("src");
        String cutUrl=Jsoup.parse(s).select("[class=slider-item]").first().attr("src");
        //获取到的图片连接有时是不全的，没有前面的字段，编辑补全该图片的url
        System.out.println(originalUrl);
        TestBase64Image t = new TestBase64Image();
        //在这里可以直接写入base64的值进行测试
        String path = t.saveImg(originalUrl,count);
        String path2 = t.saveImg(cutUrl,(count+1));
        //输出路径
        System.out.println(path);
        System.out.println(path2);

        //手动拖动滑块
        Actions action = new Actions(driver);
        WebElement moveButton = driver.findElement(By.xpath("//*[@id='slide-button']"));
        //移到滑块元素并悬停
        action.moveToElement(moveButton).clickAndHold(moveButton);
        try {
            Thread.sleep(2000L);
            action.moveByOffset(176, 0).perform();
            Thread.sleep(2000L);
            action.dragAndDropBy(moveButton,176, 0).perform();
            Thread.sleep(2000L);
            action.release().perform(); // 松开鼠标
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendOutGoods(String payStr,String ps,WebDriver driver){

        try {
            //Thread.sleep(1000L);    //1s
            if (((payStr.indexOf("\"resultCode\":\"T00001\"") >= 0) ||
                    (payStr.indexOf("\"resultCode\":\"T00009\"") >= 0)) &&
                    (ps.indexOf("修改物流信息") < 0)) {
                //点击"发货"按钮
                //newDriver.findElement(By.cssSelector("div:nth-child(3) > .BTN_primary_4-73-0 > span")).click();
                String xpath1 = "//*[@id='mf-mms-orders-container']/div/div/div[4]/div[1]/div[2]/div/div[2]/button";
                //newDriver.findElement(By.xpath(xpath1)).click();
                //如果当前元素被遮挡住(用click点击不成)要用到Keys
                driver.findElement(By.xpath(xpath1)).sendKeys(Keys.ENTER);
                Thread.sleep(1000L);    //2s
                //点击"自己联系物流" 标签
                //String xpath2=".MDL_body_4-73-0 .TAB_capsule_4-73-0:nth-child(2) > .TAB_capsuleLabel_4-73-0";
                //newDriver.findElement(By.cssSelector(xpath2)).click();
                String xpath2 = "/html/body/div[7]/div/div/div[2]/div[3]/div[1]/div/div/div[2]";
                driver.findElement(By.xpath(xpath2)).click();
                Thread.sleep(1000L);
                //点击"无物流发货" 选项
                //newDriver.findElement(By.cssSelector(".RD_outerWrapper_4-73-0:nth-child(2)")).click();
                String xpath3 = "/html/body/div[7]/div/div/div[2]/form/div[2]/div[2]/div/div/div/label[2]/div[1]";
                driver.findElement(By.xpath(xpath3)).click();
                Thread.sleep(1000L);
                /***
                 * 设置浏览器大小使用setSize方法，需要Dimension对象作为参数，
                 * 获取当前浏览器窗口的大小使用getSize方法，返回 Dimension对象。
                 * Dimension位于org.openqa.selenium包中并提供了getHeight和getWidth方法输出高 宽。
                 */
                           /* org.openqa.selenium.Dimension targetSize = new org.openqa.selenium.Dimension(1080, 900);
                            newDriver.manage().window().setSize(targetSize);
                            newDriver.manage().window().maximize();*/

                //点击"其它" 选项
                //newDriver.findElement(By.cssSelector(".MDL_header_4-73-0")).click();
                String xpath4 = "/html/body/div[7]/div/div/div[2]/form/div[3]/div[2]/div/div/div/label[3]/div[2]";
                driver.findElement(By.xpath(xpath4)).click();
                Thread.sleep(1000L);
                //点击"确定"按钮
                //newDriver.findElement(By.cssSelector("div:nth-child(5) > .BTN_primary_4-73-0 > span")).click();
                String xpath5 = "/html/body/div[7]/div/div/div[2]/form/div[5]/button[1]";
                driver.findElement(By.xpath(xpath5)).click();
                Thread.sleep(1000L);    //3s
            }

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }



    @Test
    public void testUnit(){
        System.out.println("hello junit");
        String s = sendPayByCode("江苏移动流量充值1G", "ka089456123", "15051885736");
        System.out.println(s);
    }

    public static void main (String[]args){
            //读取日志文件
            PropertyConfigurator.configure("log4j.properties");
            try {
                String usernameId = "yuchuanlu";    //13851780333
                String passwordId = "Nj111111";     //zqc@31613646
                String urlStr = "https://mms.pinduoduo.com/login?redirectUrl=https%3A%2F%2Fmms.pinduoduo.com%2Fhome%2F";

                driver.manage().timeouts().implicitlyWait(10000L, java.util.concurrent.TimeUnit.SECONDS);
                driver.manage().timeouts().pageLoadTimeout(10000L, java.util.concurrent.TimeUnit.SECONDS);
                driver.manage().window().maximize();
                driver.get(urlStr);
                Thread.sleep(3000L);
                //点击“账户登录”按钮
                driver.findElement(By.cssSelector(".last-item")).click();
                //输入 用户名
                driver.findElement(By.cssSelector("#usernameId")).sendKeys(new CharSequence[]{usernameId});
                Thread.sleep(2000L);
                //输入密码
                driver.findElement(By.cssSelector("#passwordId")).sendKeys(new CharSequence[]{passwordId});
                Thread.sleep(3000L);
                //点击“登录”按钮
                driver.findElement(By.xpath("//*[@id='root']/div/div/div/main/div/section[2]/div/div/div/div[2]/section/div/div[2]/button")).click();
                //等待10s，保证页面加载完成
                Thread.sleep(10000L);
                String drvstr = "";
                for (; ; ) {
                    TimeUnit.SECONDS.sleep(1);
                    logger.info("当前时间：" + gettimestr("yyyy-MM-dd HH:mm:ss"));
                    driver.get("https://mms.pinduoduo.com/orders/list?type=0");
                    TimeUnit.SECONDS.sleep(3);
                    drvstr = driver.getPageSource();
                    if (drvstr.indexOf("订单编号：") > 0) {
                        String orderid = getmidstr("订单编号：", "</span>", drvstr);
                        //点击"查看详情"
                        String xpathDetail = "//*[@id='mf-mms-orders-container']/div/form/div[3]/div[1]/div[1]/div[2]/div/div/div/table/tbody/tr/td[8]/div/a[1]";
                        WebElement detailClick = driver.findElement(By.xpath(xpathDetail));
                        //当有蒙层在元素之上并且不消失时
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", detailClick);
                        //detailClick.click();
                        Thread.sleep(2000L);    //3s
                        String currentWindow = driver.getWindowHandle();
                        //logger.info("currentWindow->" + currentWindow);
                        Set<String> handles = driver.getWindowHandles();
                        Iterator<String> it = handles.iterator();
                        WebDriver newDriver = null;
                        while (it.hasNext()) {
                            String handle = it.next();
                            //logger.info("handles:" + handle);
                            if (!handle.equals(currentWindow)) {
                                newDriver = driver.switchTo().window(handle);
                                break;
                            }
                        }
                        if (newDriver != null) {
                            String ps = "";
                            int count = 0;
                            //判断是否有拼图验证
                            for (; ; ) {
                                count += 5;
                                ps = newDriver.getPageSource();
                                if ((ps.indexOf("请向右滑块完成拼图") < 0) && (ps.indexOf("请点击") < 0) &&
                                    (ps.indexOf("ICN_outerWrapper_4-72-1 MDL_headerCloseIcon_4-72-1 ICN_type-close_4-72-1") < 0)) {
                                    break;
                                }
                                logger.info("需要拼图");
                                //sliderCheck(newDriver, ps, count);
                                TimeUnit.SECONDS.sleep(5);
                                //等待5分钟，刷新页面，跳出拼图循环
                                if (count - 500 == 0) {
                                    logger.info("刷新当前页");
                                    newDriver.navigate().refresh();
                                    break;
                                }
                            }
                            Thread.sleep(1000L); //1s
                            //点击"查看充值信息"
                            ps = newDriver.getPageSource();
                            String rechargeStr = getmidstr("<div class=\"beast-with-tag-remark\">", "</div></div></span></div><div class=\"BeastBlockTitle___handleContent___1sP4f\">", ps);
                            //logger.info(rechargeStr);
                            if (StringUtils.isNotBlank(rechargeStr) &&
                                    rechargeStr.equals("审核中")) {
                                logger.info("有订单需要审核");
                                newDriver.close();
                                driver.switchTo().window(currentWindow);
                                int rdInt = RandTest.getRandom(90);
                                logger.info("随机时间数：" + rdInt + " s");
                                TimeUnit.SECONDS.sleep(rdInt);
                                continue;
                            }
                            String detailBtn = "//*[@id='mf-mms-orders-container']/div/div/div[5]/div[2]/div/div[1]/div/div[2]/div/a";
                            newDriver.findElement(By.xpath(detailBtn)).click();
                            Thread.sleep(3000L);    //此处必须等待3s以上，为了将页面的号码显示出来
                            ps = newDriver.getPageSource();
                            String mobileno = getmidstr("充值号码", "充值类型", ps);
                            mobileno = getmidstr("font-size: 12px;\"><div>", "<a data-testid=\"beast", mobileno);
                            logger.info(gettimestr("yyyy-MM-dd HH:mm:ss") + ",订单号:" + orderid);
                            String payStr = getPfacevalueAndTpye(ps, orderid, mobileno);
                            //物流发货
                            if (StringUtils.isNotBlank(payStr)){
                                sendOutGoods(payStr, ps, newDriver);
                            }
                            if (driver.getWindowHandles().size() > 1) {
                                newDriver.close();
                                driver.switchTo().window(currentWindow);
                            }
                        }
                    } else {
                        //Thread.sleep(20000L);
                        int rdInt = RandTest.getRandom(45); //60
                        logger.info("随机时间数：" + rdInt + " s");
                        TimeUnit.SECONDS.sleep(rdInt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("异常信息2：" + e.fillInStackTrace());
            } finally {
                driver.quit();
            }
        }
    }

/* Location:              F:\pdd\tencent-crack.jar!\com\yaofei\tencentcrack\PddMain.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */