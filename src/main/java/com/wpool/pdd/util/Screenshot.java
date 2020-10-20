package com.wpool.pdd.util;

import com.wpool.pdd.ChromeUtil;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Screenshot {

    private static ChromeDriver driver = null;
    /*static {
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
    public static void main(String[] args) {
        driver.get("https://www.baidu.com/");
        try {
            WebElement setting = driver.findElement(By.id("s-usersetting-top"));
            Actions actions = new Actions(driver);
            actions.clickAndHold(setting).perform();
            driver.findElement(By.linkText("搜索设置")).click();
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement resultPic = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[6]/div/div/div"));
        //WebElement resultPic = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[6]"));

        getScreenShow(driver,resultPic);
    }
    public static void getScreenShow(ChromeDriver driver,WebElement element){
        try {
            Thread.sleep(2000L);
            File src = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("F:/pdd/image/result.png"));
            FileUtils.copyFile(captureElement(src, element), new File("F:/pdd/image/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法会截取屏幕内的所有内容，包括系统自带的任务栏以及浏览器的导航栏和操作菜单
     */
    public static void screenByRobot(){
        BufferedImage image = null;
        try {
            image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "jpg", new File("F:/pddLogger/rebot截图.jpg"));
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 该方法可以只截取自动化运行的浏览器窗口内，不会截取浏览器的操作按钮和系统的任务栏区域
     */
    public static void screenshot(){
        //自己重新封装的访问url方法
        File srcfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcfile, new File("F:/pddLogger/takescreen截图.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File captureElement(File screenshot, WebElement element){
        try {
            BufferedImage img = ImageIO.read(screenshot);
            int width = element.getSize().getWidth()+51;    //51 最佳
            int height = element.getSize().getHeight()+15;  //15 最佳
            //获取指定元素的坐标
            Point point = element.getLocation();
            //从元素左上角坐标开始，按照元素的高宽对img进行裁剪为符合需要的图片    //x+252,y+120
            BufferedImage dest = img.getSubimage(((point.getX()+252)), ((point.getY()+120)), width, height);
            System.out.println(point.getX()+","+point.getY()+","+width+","+height);
            ImageIO.write(dest, "png", screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshot;
    }



}
