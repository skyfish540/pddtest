package com.wpool.pdd.util;
import com.wpool.pdd.ChromeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContexts;
import org.jsoup.Jsoup;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.alibaba.fastjson.JSONObject;


import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 *
 */
public class TencentCrawler {
    private static String BASE_PATH = "F:/pddLogger/";
    //小方块距离左边界距离 距离不宜过大
    private static int START_DISTANCE = 1;
//    private static int START_DISTANCE = 22;
    private static ChromeDriver driver=null;
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


    //主函数
    public static void main(String[] args) throws IOException {
        //crawl();
        calcMoveDistance(1);
    }

    public static void crawl(){

        //driver = new ChromeDriver();
        for(int i = 0; i < 10; i++) {
            try {
                driver.manage().window().setSize(new Dimension(1024, 768));
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
                driver.manage().window().maximize();

                //*1 打开网页地址，输入登录信息，点击弹出验证码界面
                driver.get("https://007.qq.com/online.html?ADTAG=capt.slide");//这里是网页地址
                WebElement element = driver.findElement(By.cssSelector("a[data-type='1']"));
                element.click();
                Thread.sleep(2 * 1000);
                driver.findElement(By.id("code")).click();


                // System.out.println(json.get("environmentURL"));
                // driver.get(json.getString("environmentURL"));;//登录页面
                // System.out.println(json.get("environmentURL"));
                // LoginPage lg = null;
                // lg = new LoginPage(driver);
                // lg.doLogin(json);//登录
                // WebElement element = null;

                Actions actions = new Actions(driver);

                //*2 切换到验证码框的frame
                driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"tcaptcha_iframe\"]")));

                //*3 获取到滑动验证码的整张图片连接
                String originalUrl = Jsoup.parse(driver.getPageSource()).select("[id=cdn1]").first().attr("src");
                System.out.println(Jsoup.parse(driver.getPageSource()));
                //获取到的图片连接有时是不全的，没有前面的字段，编辑补全该图片的url
                System.out.println(originalUrl);
                TestBase64Image t = new TestBase64Image();
                //在这里可以直接写入base64的值进行测试

                //输出路径
                String originalUrl1 = "https://t.captcha.qq.com"+originalUrl;//
                System.out.println(originalUrl1);

                //*4 通过该方法下载处理图片
                //downloadOriginalImg(i, originalUrl1, driver.manage().getCookies());

                Thread.sleep(3 * 1000);

                //*5 通过该方法得到活动块的距离（里面有个参数需要调整）
                int distance = calcMoveDistance(i);

                Thread.sleep(3 * 1000);
                List<MoveEntity> list = getMoveEntity(distance);
                element = driver.findElement(By.id("tcaptcha_drag_button"));
                actions.clickAndHold(element).perform();
                int d = 0;

                //*6拖动滑块
                for (MoveEntity moveEntity : list) {
                    actions.moveByOffset(moveEntity.getX(), moveEntity.getY()).perform();
                    System.out.println("向右总共移动了:" + (d = d + moveEntity.getX()));
                    Thread.sleep(moveEntity.getSleepTime());
                }
                Thread.sleep(1 * 1000);
                actions.release(element).perform();
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //*7 判断验证码滑动是否成功，如成功，结束循环
            try {
                //获取到登录成功后页面的元素
                WebElement newPage = driver.findElement(By.xpath("//*[@id=\"user_box\"]/div"));

                break;
            } catch (Exception e) {
            }

        }
//            driver.quit();
    }
    private static void downloadOriginalImg(int i, String originalUrl, Set<Cookie> cookieSet) throws IOException {
        CookieStore cookieStore = new BasicCookieStore();
        cookieSet.forEach( c -> {
            BasicClientCookie cookie = new BasicClientCookie(c.getName(), c.getValue());
            cookie.setPath(c.getPath());
            cookie.setDomain(c.getDomain());
            cookie.setExpiryDate(c.getExpiry());
            cookie.setSecure(true);
            cookieStore.addCookie(cookie);
        });
        InputStream is = null;
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType())
                            , (chain, authType) -> true).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE)
                            .register("https", new SSLConnectionSocketFactory(sslContext))
                            .build();
            is = HttpClients.custom()
//                    .setProxy(new HttpHost("127.0.0.1", 8888))
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36")
                    .setDefaultCookieStore(cookieStore)
                    .setConnectionManager(new PoolingHttpClientConnectionManager(socketFactoryRegistry))
                    .build()
                    .execute(new HttpGet(originalUrl))
                    .getEntity().getContent();
            FileUtils.copyInputStreamToFile(is, new File(BASE_PATH + "tencent-original" + i + ".png"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 如何判定找到目标滑块位置
     * y轴上至少找到一条长度为30px的白线
     * @throws IOException
     */
    public static int calcMoveDistance(int i) throws IOException {
        BufferedImage fullBI = ImageIO.read(new File("F:/pddLogger/img/2020-10-13/1.jpeg"));
        System.out.println("fullBI.getWidth():"+fullBI.getWidth());
        for(int w = 0 ; w < fullBI.getWidth(); w++){
            int whiteLineLen = 0;
            for (int h = 0; h < fullBI.getHeight(); h++){
                int[] fullRgb = new int[3];
                fullRgb[0] = (fullBI.getRGB(w, h)  & 0xff0000) >> 16;
                fullRgb[1] = (fullBI.getRGB(w, h)  & 0xff00) >> 8;
                fullRgb[2] = (fullBI.getRGB(w, h)  & 0xff);
                if ((Math.abs(fullRgb[0] - 0xff) + Math.abs(fullRgb[1] -0xff) + Math.abs(fullRgb[2] - 0xff)) < 40){
                    whiteLineLen++;
                } else {
                    whiteLineLen = 0;
                    continue;
                }
                if (whiteLineLen >= 20){
                    System.out.println("找到缺口成功，实际缺口位置x：" + w);
                    System.out.println("应该移动距离：" + (w/2 - START_DISTANCE));
                    //网页显示大小为实际图片大小的一半多一点，这个一点需要多次调试判断，这里是30
                    return w/2+30 - START_DISTANCE;
                }
            }

        }
        throw new RuntimeException("计算缺口位置失败");
    }
    public static List<MoveEntity> getMoveEntity(int distance){
        List<MoveEntity> list = new ArrayList<>();
        for (int i = 0 ;i < distance; i++){

            MoveEntity moveEntity = new MoveEntity();
            moveEntity.setX(1);
            moveEntity.setY(0);
            moveEntity.setSleepTime(0);
            list.add(moveEntity);
        }
        return list;
    }
    static class MoveEntity{
        private int x;
        private int y;
        private int sleepTime=200;//毫秒  *该参数影响滑动时的速度，速度过快导致验证失败的可能性较高。

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getSleepTime() {
            return sleepTime;
        }

        public void setSleepTime(int sleepTime) {
            this.sleepTime = sleepTime;
        }
    }

}
