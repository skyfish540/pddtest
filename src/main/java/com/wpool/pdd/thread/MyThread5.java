package com.wpool.pdd.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.wpool.pdd.util.GetToken.threadEex;

/**
 *
 */
public class MyThread5 implements Runnable {
    final static Logger log= LogManager.getLogger(MyThread5.class);
    @Override
    public void run(){
        String url="http://localhost:8090/TotalControl/v2/devices/device@1054208357/screen/inputs";
        threadEex(url,"device@1054208357","device5/", "5 ");

       /* String mobile = "";
        String facevalue="";
        String id="";
        String ISPType="";

        String url="http://localhost:8090/TotalControl/v2/devices/device@248174670/screen/inputs";
        String payStr5="支付成功";

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
                ISPType=jsonObject.getString("type3");
                log.info(id+" "+mobile+" "+facevalue+" "+ISPType+" "+resultCode);
                facevalue=facevalue.substring(0,facevalue.indexOf("."));
                //携号转网验证，如果是携号转网，就不处理，显示处理中
                boolean bl = authISP(mobile);
                if (bl && Integer.valueOf(facevalue)>=10){    //没有转转网
                    //订单入库
                    int saveCount = insertMobile(mobile, facevalue, id);
                    if (saveCount>0){
                        //发送充值
                        try {
                            //判断是移动(U100) 电信(U300) 联通(U200)
                            if (StringUtils.isNotBlank(ISPType) && StringUtils.equals("U100", ISPType)){
                                //移动
                                payStr5 = tmallPay(url,"device@248174670",mobile, facevalue,"device5/");
                            }else if (StringUtils.isNotBlank(ISPType) && StringUtils.equals("U300", ISPType)){
                                //电信
                                payStr5 = DXPay.run("device@248174670", "device5/");

                            }else {
                                //联通

                            }
                            if (StringUtils.isNotBlank(payStr5) && StringUtils.equals("支付成功", payStr5)){
                                log.info("payStr5 "+payStr5);
                            }else {
                                log.info("MyThread5 充值出错....");
                                //返回微信小程序主页面 1.发送一个返回键(back) 2.发送一个home键
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
        }
*/

    }
}
