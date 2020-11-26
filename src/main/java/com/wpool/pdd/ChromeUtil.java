package com.wpool.pdd;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wpool.pdd.util.AliyunApi;
import com.wpool.pdd.util.GetStringUtul;
import com.wpool.pdd.util.GetToken;
import com.wpool.pdd.util.Screenshot;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;


import javax.imageio.ImageIO;

import static com.wpool.pdd.util.AliyunApi.aliCqJianJiao;
import static com.wpool.pdd.util.GetToken.authISP;
import static com.wpool.pdd.util.GetToken.epayForkachi;
import static com.wpool.pdd.util.Screenshot.captureElement;
import static com.wpool.pdd.util.AliyunApi.alicSzart;


public class ChromeUtil {
    private static ChromeDriver driver = null;
    final static Logger logger =Logger.getLogger(ChromeUtil.class);
    private String downLoadPath = null;
    private boolean isHeadless = false;
    private boolean noPicture = false;
    private boolean noPassword=false;
    private boolean developmentMode = false;
    private Integer height = Integer.valueOf(800);
    private Integer width = Integer.valueOf(1300);

    public void setHeadless(boolean isHeadless) {
        this.isHeadless = isHeadless;
    }

    public void setNoPicture(boolean noPicture) {
        this.noPicture = noPicture;
    }

    public void setNoPassword(boolean noPassword) {
        this.noPassword = noPassword;
    }

    public void setDownLoadPath(String path) {
        this.downLoadPath = path;
    }

    public void setDevelopmentMode(boolean isDevelop) {
        this.developmentMode = isDevelop;
    }

    public void setHeightWithWidth(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }


    public ChromeDriver getChorme() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        DesiredCapabilities caps = initChromeOption();
        ChromeDriver driver = new ChromeDriver(caps);
        driver.manage().window().setSize(new Dimension(this.width.intValue(), this.height.intValue()));
        return driver;
    }

    public DesiredCapabilities initChromeOption() {
        ChromeOptions options = new ChromeOptions();
        DesiredCapabilities caps = new DesiredCapabilities();
        options.addArguments("no-sandbox");
        options.addArguments(new String[] {"user-agent=\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36\"" });

        if (this.isHeadless) {
            options.addArguments(new String[] { "-headless" });
        }
        if (this.noPicture) {
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", Integer.valueOf(2));
            options.setExperimentalOption("prefs", prefs);
        }
        //不弹出是否保存密码框
        if (this.noPassword){
            Map<String, Object> prefPwd = new HashMap<>();
            prefPwd.put("credentials_enable_service", false);
            prefPwd.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefPwd);
        }
        if (this.downLoadPath != null) {
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("download.default_directory", this.downLoadPath);
            chromePrefs.put("profile.default_content_settings.popups", Integer.valueOf(0));
            options.setExperimentalOption("prefs", chromePrefs);
        }
        //不显示正在受自动化软件控制
        if (this.developmentMode) {
            List<String> excludeSwitches = new ArrayList<>();
            excludeSwitches.add("enable-automation");
            options.setExperimentalOption("excludeSwitches", excludeSwitches);
            //options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.setExperimentalOption("useAutomationExtension",false);
        }
        caps.setCapability("chromeOptions", options);
        return caps;
    }

   /* static {
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
    }*/

    public static void main(String[] args) throws InterruptedException {

        String mobile = "";
        String facevalue="";
        String id="";
        String ispType="";


        //driver.get("https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
        for (; ;){
            TimeUnit.SECONDS.sleep(2);
            ChromeUtil chromeUtil = new ChromeUtil();
            chromeUtil.setDevelopmentMode(true);
            chromeUtil.setHeightWithWidth(Integer.valueOf(1200), Integer.valueOf(900));
            ChromeDriver driver = chromeUtil.getChorme();
            logger.info("当前时间：" + GetStringUtul.gettimestr("yyyy-MM-dd HH:mm:ss"));
            driver.get("https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
            driver.manage().timeouts().implicitlyWait(10000L, java.util.concurrent.TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(10000L, java.util.concurrent.TimeUnit.SECONDS);
            driver.manage().window().maximize();
            String timestr= String.valueOf(System.currentTimeMillis());
            //输入号码 13276675363  18651602096
            //从订单系统 抓取订单

            String respstr = epayForkachi("P160708172012320", timestr, "002", "U100","T001","afb93858aa300bbdac2f54f1da882743");
            //System.out.println(respstr);
            JSONObject jsonObject= (JSONObject) JSON.parse(respstr);
            String resultCode=jsonObject.getString("resultCode");
            if (StringUtils.equals("T00008", resultCode)){
                logger.info("无订单...");
            }else {
                mobile = jsonObject.getString("zcaccount");
                facevalue=jsonObject.getString("facevalue");
                id=jsonObject.getString("id");
                logger.info(id+" "+mobile+" "+facevalue+" "+resultCode);
                //携号转网验证，如果是携号转网，就不处理，显示处理中
                boolean bl = authISP(mobile);

            }
            driver.findElement(By.xpath("//input[@id='number']")).sendKeys("13276675363");
            Thread.sleep(3000L);
            //选择 金额
            //driver.findElement(By.xpath("//*[@id='card01']")).click();
        /*WebElement moneyClick=driver.findElement(By.xpath("//*[@id='card01']"));
        //当有蒙层在元素之上并且不消失时
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", moneyClick);*/
            //输入金额
            driver.findElement(By.xpath("//*[@id='anyAmount']")).sendKeys("2");

            Thread.sleep(2000L);
            //手动拖动滑块
            Actions action = new Actions(driver);
            WebElement moveButton = driver.findElement(By.className("nc_bg"));
            //移到滑块元素并悬停
            action.moveToElement(moveButton).clickAndHold(moveButton);
            Thread.sleep(3000L);
            action.moveByOffset(258, 0).perform();
            Thread.sleep(3000L);
            action.dragAndDropBy(moveButton,258, 0).perform();
            Thread.sleep(1000L);
            action.release();
            Thread.sleep(1000L);
            //点击下一步
            driver.findElement(By.xpath("//*[@id='submitButton']")).click();
            Thread.sleep(2000L);
            String currentWindow = driver.getWindowHandle();
            System.out.println("currentWindow->"+currentWindow);
            Set<String> handles = driver.getWindowHandles();
            java.util.Iterator<String> it = handles.iterator();
            WebDriver newDriver = null;
            while (it.hasNext()) {
                String handle = it.next();
                System.out.println("handles:"+handle);
                if (!handle.equals(currentWindow)) {
                    newDriver = driver.switchTo().window(handle);
                    break;
                }
            }
            if (newDriver!=null){

                TimeUnit.SECONDS.sleep(2);
                aliPay(newDriver, driver);
                //ePay(newDriver);

                TimeUnit.SECONDS.sleep(12);
                String pageSource = newDriver.getPageSource();
                //System.out.println(pageSource);
                String getmidstr = GetStringUtul.getmidstr("订单号：", "</td>", pageSource);
                System.out.println("getmidstr->"+getmidstr);
            }
            TimeUnit.SECONDS.sleep(2);

            if (driver.getWindowHandles().size() > 1) {
            newDriver.close();
            driver.switchTo().window(currentWindow);
            driver.close();
            TimeUnit.SECONDS.sleep(5);
            }
        }
    }

    public static void aliPay(WebDriver newDriver,ChromeDriver driver){
        try {
        //选择支付宝
        newDriver.findElement(By.xpath("//*[@id='bank_DEBITCARD_ALIPAY']")).click();
        Thread.sleep(2000L);
        //点击确定
        newDriver.findElement(By.xpath("//*[@id='showAll']/div[2]/div[2]/a")).click();
        Thread.sleep(2000L);
        //输入支付宝用户名
        newDriver.findElement(By.xpath("//*[@id='J_TloginForm']/div/div[1]/input")).sendKeys("luxin-282@163.com");
        Thread.sleep(2000L);
        //String js1 = "document.getElementById('J_password').style.display='block';"; getElementsByName
        //String js1 = "document.getElementById('J_password').type='text';";
           /* String js1 = "document.getElementsByName('pwdSecurityId')[0].type='text';";
            ((JavascriptExecutor)newDriver).executeScript(js1);*/

        //输入支付宝密码
        newDriver.findElement(By.xpath("//*[@id='payPasswd_rsainput']")).sendKeys("851103");
        Thread.sleep(2000L);
        //F:/pdd/image  div[3]表示当前层级下的第三个div标签 //input[@id='authcode1601021552615']
        System.out.println("等等5s");
        //查找图形验证码元素
        WebElement elementPic=newDriver.findElement(By.xpath("//*[@id='J_TloginForm']/div/div[3]/div/div/div/span/input"));
        //获取图形验证码
        Screenshot.getScreenShow(driver,elementPic);
        TimeUnit.SECONDS.sleep(5);
        String s_code = aliCqJianJiao("F:/pdd/image/test.png");
        logger.info("s_code:"+s_code);
        TimeUnit.SECONDS.sleep(2);
        //输入图形验证码
        elementPic.sendKeys(s_code);
        //点击下一步
        newDriver.findElement(By.xpath("//*[@id='J_TloginForm']/div/div[5]/button")).click();
        TimeUnit.SECONDS.sleep(5);
        String curWindow = newDriver.getWindowHandle();
        System.out.println("curWindow->"+curWindow);

        Actions actions=new Actions(newDriver);
        WebElement elementPd1 = newDriver.findElement(By.xpath("//*[@id='payPassword_container']/div/i[1]/b"));
        actions.sendKeys(elementPd1,"8").perform();
        TimeUnit.SECONDS.sleep(1);
        WebElement elementPd2 = newDriver.findElement(By.xpath("//*[@id='payPassword_container']/div/i[2]/b"));
        actions.sendKeys(elementPd2,"5").perform();
        WebElement elementPd3 = newDriver.findElement(By.xpath("//*[@id='payPassword_container']/div/i[3]/b"));
        actions.sendKeys(elementPd3,"1").perform();
        TimeUnit.SECONDS.sleep(1);
        WebElement elementPd4 = newDriver.findElement(By.xpath("//*[@id='payPassword_container']/div/i[4]/b"));
        actions.sendKeys(elementPd4,"1").perform();
        TimeUnit.SECONDS.sleep(1);
        WebElement elementPd5 = newDriver.findElement(By.xpath("//*[@id='payPassword_container']/div/i[5]/b"));
        actions.sendKeys(elementPd5,"0").perform();
        TimeUnit.SECONDS.sleep(1);
        WebElement elementPd6 = newDriver.findElement(By.xpath("//*[@id='payPassword_container']/div/i[6]/b"));
        actions.sendKeys(elementPd6,"3").perform();
        TimeUnit.SECONDS.sleep(1);
        //点击确认付款
        newDriver.findElement(By.xpath("//*[@id='J_authSubmit']")).click();
        System.out.println("点击确认付款");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void ePay(WebDriver newDriver){

        try {
        //选择 沃钱包储蓄卡
        newDriver.findElement(By.xpath("//*[@id='bank_DEBITCARD_WOYF']")).click();
        Thread.sleep(2000L);

            //点击确定
            newDriver.findElement(By.xpath("//*[@id='showAll']/div[2]/div[2]/a")).click();

            /*//选择 平台支付
            newDriver.findElement(By.xpath("//*[@id='showAll']/div[1]/ul/li[3]")).click();
            TimeUnit.SECONDS.sleep(2);
            //选择 沃钱包
            newDriver.findElement(By.xpath("//*[@id='bank_WOYF01']")).click();
            TimeUnit.SECONDS.sleep(2);
            //选择 确认支付
            newDriver.findElement(By.xpath("//*[@id='showAll']/div[4]/div/a")).click();*/

            TimeUnit.SECONDS.sleep(5);
            System.out.println("进入iframe");
            WebElement iframe = newDriver.findElement(By.id("main"));
            newDriver.switchTo().frame(iframe);

            //输入 沃支付账户
            newDriver.findElement(By.xpath("/html/body/div/div[2]/div/form[1]/div[1]/input")).sendKeys("15051885736");
            TimeUnit.SECONDS.sleep(5);

            //输入 支付密码
            System.out.println("输入密码");
           /* String js = "$('input[id='personPwd']').val('123456')";
            ((JavascriptExecutor)newDriver).executeScript(js);*/
            JavascriptExecutor js1 = (JavascriptExecutor) newDriver;
            js1.executeScript("document.getElementById('personPwd').setAttribute(\"autocomplete\",\"on\")");

            Actions actions=new Actions(newDriver);
            WebElement element = newDriver.findElement(By.xpath("//*[@id='personPwd']"));
            //actions.click().sendKeys(element, "408182").build().perform();

            //JavascriptExecutor js1 = (JavascriptExecutor) newDriver;
            js1.executeScript("arguments[0].value='408182'", element);
            TimeUnit.SECONDS.sleep(2);

            //点击 下一步
            System.out.println("点击下一步");
            newDriver.findElement(By.xpath("//*[@id='personLogin_btn']")).click();

            TimeUnit.SECONDS.sleep(3);
            //再次输入支付密码
            System.out.println("再次输入支付密码");
            WebElement elementPay = newDriver.findElement(By.xpath("//*[@id='pay_pwd']"));
            //js1.executeScript("arguments[0].value='408182'", elementPay);

            TimeUnit.SECONDS.sleep(6);
            //点击 确认支付
            System.out.println("确认支付");
            WebElement eClick = newDriver.findElement(By.xpath("//*[@id='pay_submit_btn']"));
            actions.click(eClick).build().perform();
            //newDriver.findElement(By.xpath("//*[@id='pay_submit_btn']")).click();

            TimeUnit.SECONDS.sleep(2);
            newDriver.switchTo().parentFrame();
            //点击 关闭交易失败弹框
            System.out.println("闭交易失败弹框");
            newDriver.findElement(By.xpath("//*[@id='result_3']/div/input")).click();

            TimeUnit.SECONDS.sleep(2);
            newDriver.switchTo().frame(iframe);
            //点击 免费获取验证码
            System.out.println("免费获取验证码");
            newDriver.findElement(By.xpath("//*[@id='phone_msg_code_control']/div/a")).click();

            TimeUnit.SECONDS.sleep(2);
            //输入 验证码
            System.out.println("输入 验证码");
            newDriver.findElement(By.xpath("//*[@id='phoneCode']")).sendKeys("123556");

            TimeUnit.SECONDS.sleep(2);
            //点击 再次确认支付
            System.out.println("再次确认支付");
            newDriver.findElement(By.xpath("//*[@id='pay_submit_btn']")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}


/* Location:              F:\pdd\tencent-crack.jar!\com\yaofei\tencentcrack\ChromeUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */