package com.wpool.pdd.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.imageio.stream.IIOByteBuffer;
import java.net.URL;
import java.util.Random;


/**
 *
 */
public class RandTest {
    final static Logger logger =Logger.getLogger(RandTest.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        URL url = ClassLoader.getSystemResource("tessdata");
        System.out.println("url:"+url);
        String url1="F:/repository/net/sourceforge/tess4j/tess4j/3.2.1/tess4j-3.2.1.jar!/tessdata";
        String tesspath = url.getPath().substring(0,56)+"tessdata";
        System.out.println("tesspath:"+tesspath);


        for (int i = 0; i < 10; i++) {

            Random random=new Random();
            logger.info(random.nextInt(30));
        }
    }

    public static int getRandom(int i){
        Random random=new Random();
        return random.nextInt(30)+i;

    }
}
