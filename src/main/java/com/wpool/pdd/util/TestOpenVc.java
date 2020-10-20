package com.wpool.pdd.util;



/**
 *
 */
/*
public class TestOpenVc {
   */
/* static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }*//*


   */
/* public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        // 这个不能少
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat m  = Mat.eye(4, 4, CvType.CV_8UC1);
        System.out.println("m = " + m.dump());

        m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
        System.out.println("OpenCV Mat: " + m);
        Mat mr1 = m.row(1);
        mr1.setTo(new Scalar(1));
        Mat mc5 = m.col(5);
        mc5.setTo(new Scalar(5));
        System.out.println("OpenCV Mat data:\n" + m.dump());
        //handleImage();


    }*//*


    */
/*public static void handleImage(){
        // 读取背景原图
        //Mat src = Imgcodecs.imread("F:/pddLogger/img/2020-10-13/5-2.jpeg", Imgcodecs.IMREAD_UNCHANGED);
        Mat target=Imgcodecs.imread("F:/pddLogger/img/2020-10-13/ts.png", Imgcodecs.IMREAD_UNCHANGED);
        // 存储处理后的图片数据
        Mat dst = new Mat();
        // 1.高斯模糊处理
        //Imgproc.GaussianBlur(target, dst, new Size(9,9), 0, 0, Core.BORDER_DEFAULT);
        try {
            TimeUnit.SECONDS.sleep(1);

        // 2.灰度化处理
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGB2GRAY);   //Imgcodecs.IMREAD_GRAYSCALE;Imgproc.COLOR_BGR2GRAY
        TimeUnit.SECONDS.sleep(1);
        // 3.二值化
        //Imgproc.threshold(dst, dst,0,255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        //Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Mat matRev = reversePixel(target);
        //calculatorMoveDistance(src,target);

        // 存储在本地
        Imgcodecs.imwrite("F:/pddLogger/img/2020-10-13/99.png", dst);

    }*//*


    */
/**
     * 反转像素
     * @param src
     * @return
     *//*

    */
/*public static Mat reversePixel(Mat src){
        Mat clone = src.clone();
        int rows = clone.rows();
        int cols = clone.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double[] doubles = clone.get(i, j);

                doubles[0] = 255 - doubles[0];
                if (doubles.length > 1){
                    doubles[1] = 255 - doubles[1];
                }
                clone.put(i, j, doubles);
            }
        }
        return clone;
    }*//*


    */
/**
     * 计算滑块位置
     * @param source
     * @param target
     * @return
     *//*

   */
/* public static Point calculatorMoveDistance(Mat source ,Mat target){
        // 背景图
        Mat clone = source.clone();
        // 处理结果
        Mat result = new Mat();
        // 匹配结果
        Imgproc.matchTemplate(source, target, result, Imgproc.TM_CCORR_NORMED);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        // 获取匹配结果坐标
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);
        Point maxLoc = minMaxLocResult.maxLoc;
        System.out.println("x坐标："+maxLoc.x+",y坐标"+maxLoc.y);
        // 在图上做标记
        Imgproc.rectangle(clone, maxLoc,
                new Point(maxLoc.x + target.cols(),maxLoc.y + target.rows()),
                new Scalar(0, 255, 0));

        Imgcodecs.imwrite("F:/pddLogger/img/2020-10-13/89.png", clone);
        return maxLoc;

    }*//*

}
*/
