package com.wpool.pdd.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.wpool.pdd.util.GetToken.threadEex;

/**
 *
 */
public class MyThread1 implements Runnable {
    final static Logger log= LogManager.getLogger(MyThread1.class);
    @Override
    public void run(){
        String url="http://localhost:8090/TotalControl/v2/devices/device@630990012/screen/inputs";

        threadEex(url,"device@630990012","device1/", "1 ");

       /* String mobile = "";
        String facevalue="";
        String id="";
        //设备3 device@1453327863 设备2 device@1640507393  设备1 device@630990012
        String url="http://localhost:8090/TotalControl/v2/devices/device@630990012/screen/inputs";
        String payStr1="订单已提交充值";

            for ( ; ; ){
                String timestr= String.valueOf(System.currentTimeMillis());
                //log.info(stringToDate(timestr));
                //下单接口
                String respstr = epayForkachi("P160708172012320", timestr, "002", "afb93858aa300bbdac2f54f1da882743");
                //System.out.println(respstr);
                JSONObject jsonObject= (JSONObject) JSON.parse(respstr);
                String resultCode=jsonObject.getString("resultCode");
                if (StringUtils.equals("T00008", resultCode)){
                    log.info("无订单...");
                }else {
                    mobile = jsonObject.getString("zcaccount");
                    facevalue=jsonObject.getString("facevalue");
                    id=jsonObject.getString("id");
                    log.info(id+" "+mobile+" "+facevalue+" "+resultCode);
                    //携号转网验证，如果是携号转网，就不处理，显示处理中
                    boolean bl = authISP(mobile);
                    if (bl){    //没有转转网
                        //订单入库
                        int saveCount = insertMobile(mobile, facevalue, id);
                        if (saveCount>0){
                            //发送充值
                            try {
                                payStr1=sendYDPay(url,"device@630990012",mobile, facevalue,"device1/");
                                if (StringUtils.isNotBlank(payStr1) && StringUtils.equals("订单已提交充值", payStr1)){
                                    log.info("payStr1 "+payStr1);
                                }else {
                                    log.info("MyThread1 充值出错....");

                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //回调接口
                            String callBack = callBackToKC(id, "888888", "2", "已充值");
                            log.info(id+" 回调 "+callBack);
                            //更新订单状态
                            int flag = updateMobile("99999", "2",id);
                            if (flag>0){
                                log.info(id+" 订单状态更新成功!");
                            }
                        }
                    }else {     //携号转网
                        //回调接口
                        String callBack = callBackToKC(id, "000000", "3", "充值失败");
                        log.info(id+" 回调 "+callBack);
                        //更新订单状态
                        int flag = updateMobile("99999", "3",id);
                        if (flag>0){
                            log.info(id+" 订单状态更新成功!");
                        }
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/


    }
}
