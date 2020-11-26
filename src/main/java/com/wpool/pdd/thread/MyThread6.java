package com.wpool.pdd.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.wpool.pdd.util.GetToken.threadEex;

/**
 *
 */
public class MyThread6 implements Runnable {
    final static Logger log= LogManager.getLogger(MyThread6.class);
    @Override
    public void run() {
        String url="http://localhost:8090/TotalControl/v2/devices/device@246343071/screen/inputs";

        threadEex(url,"device@246343071","device6/", "6 ");

    }
}
