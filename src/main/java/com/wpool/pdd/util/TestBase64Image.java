package com.wpool.pdd.util;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.codec.binary.Base64.decodeBase64;


import java.util.Base64.Encoder;

import java.util.Base64.Decoder;  //加解密的包




/**
 *
 */
public class TestBase64Image {
    // 图片路劲层级分隔符
    private static String separator = "/";

    public static void main(String[] args) {
        TestBase64Image t = new TestBase64Image();
        //在这里可以直接写入base64的值进行测试
        String path = t.saveImg("",1);
        //输出路径
        System.out.println(path);
    }



    public String saveImg(String baseImg,int count)  {
        //定义一个正则表达式的筛选规则，为了获取图片的类型
        String rgex = "data:image/(.*?);base64";
        String type = getSubUtilSimple(baseImg, rgex);
        //去除base64图片的前缀
        baseImg = baseImg.replaceFirst("data:(.+?);base64,", "");
        byte[] b;
        byte[] bs;
        OutputStream os = null;
        String fileName = "";
        String nowDate = "";
        // 格式化并获取当前日期（用来命名）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        nowDate = format.format(new Date());
        //把图片转换成二进制
       /* Decoder decoder =  Base64.getMimeDecoder();
        b = decoder.decode(baseImg);*/
        //生成路径
        String path = "F:/pddLogger"  + separator + "img" + separator + nowDate + separator;
        //随机生成图片的名字，同时根据类型结尾
        //fileName = UUID.randomUUID().toString() + "." + type;
        fileName = count + "." + type;

        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        File imageFile = new File(path + "/" + fileName);
        //BASE64Decoder d = new BASE64Decoder();
        // 保存
        try {
            //bs = d.decodeBuffer(baseImg);  //import sun.misc.BASE64Decoder;
            bs=decodeBase64(baseImg);
            os = new FileOutputStream(imageFile);
            os.write(bs);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
        }

        return "img" + separator + nowDate + separator + fileName;

    }

    public static String getSubUtilSimple(String soap,String rgex){
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        while(m.find()){
            return m.group(1);
        }
        return "";


    }

}
