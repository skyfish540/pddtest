package com.wpool.pdd.util;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 */
public class ReadLastFile {
    public static String readLastLine(File file, String charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()){

            return null;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file,"r");
            // 获取 RandomAccessFile对象文件指针的位置，初始位置为0
            System.out.println("输入内容："+raf.getFilePointer());   // 0
            long len = raf.length();    //读取文件的长度
            System.out.println("文件的长度："+len);
            if (len == 0L){
                return "";
            }
            long pos = len - 1;
            while (pos > 0) {
                pos--;
                raf.seek(pos);  //移动文件记录指针的位置
                if (raf.readByte() == '\n'){
                    break;
                }
            }
            if (pos == 0){
                raf.seek(0);
            }
            byte[] bytes = new byte[(int) (len - pos)];
            raf.read(bytes);
            if (charset == null){
                return new String(bytes);
            }
            String str=new String(bytes, charset);
            int index = str.lastIndexOf(":");
            System.out.println(str.lastIndexOf(":"));
            String substring = str.substring((index+1), 56);
            System.out.println(substring);

            return substring;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }

    @Test
    public void test() throws IOException {
        String filePath="F:/pdd/短信记录/COM2接收.txt";
        File file=new File(filePath);

        String lastLine = readLastLine(file, "GBK");
        System.out.println("lastLine->"+lastLine);

    }
}
