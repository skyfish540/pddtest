package com.wpool.pdd.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.wpool.pdd.util.GetToken.threadEex;

/**
 *
 */
public class MyThread7 implements Runnable {
    final static Logger log= LogManager.getLogger(MyThread7.class);
    @Override
    public void run() {
        String url="http://localhost:8090/TotalControl/v2/devices/device@248174670/screen/inputs";

        threadEex(url,"device@248174670","device7/", "7 ");

    }
}
