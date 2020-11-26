package com.wpool.pdd.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wpool.pdd.thread.MyThread3;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

import static com.wpool.pdd.util.GetToken.*;
import static com.wpool.pdd.util.GetToken.callBackToKC;

/**
 *
 */
public class DXPay {

    final static Logger log= LogManager.getLogger(DXPay.class);
    public static String run(String device,String uriPath) throws InterruptedException {
        String mobile = "";
        String facevalue="";
        String id="";
        //设备2 device@1431207012   设备1 device@1453327863
        String url="http://localhost:8090/TotalControl/v2/devices/"+device+"/screen/inputs";
        String payStr3="";
        payStr3 = sendDXPay(url, device, mobile, facevalue, uriPath);
        return payStr3;
    }
}
