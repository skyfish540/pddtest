package com.wpool.pdd.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class InitLog {
    @Test
    public  void init() {//该方法必须在所有log4j使用前调用 最好时初始化时就调用执行 加载好配置

        Logger logger = LogManager.getLogger(InitLog.class);

        logger.debug("This is Debug Message!");
        logger.info("This is Info Message!");
        logger.warn("This is warn Message!");
        logger.error("This is Error Message!");
        Properties props  =   new  Properties();//创建一个系统参数对象
        String filePath="F:/bjpowernode/pddtest/log4j.properties";
        try  {
            FileInputStream istream  =  new  FileInputStream(filePath);//读取配置文件(log4j.properties)位置
            props.load(istream);//将配置加载到系统参数对象中

            istream.close();
            String logFile  = props.getProperty( "log4j.appender.file.File " );
            System.out.println(logFile);
            // 设置路径可以不经配置
            props.setProperty( " log4j.appender.file.File " ,logFile);//可以不配置
            PropertyConfigurator.configure(props); // 装入log4j配置信息
        }  catch  (IOException e) {
            System.out.println( " Could not read configuration file [ "   +  filePath  +   " ]. " );
            System.out.println( " Ignoring configuration file [ "   +  filePath  +   " ]. " );
            return ;
        }
    }

}


