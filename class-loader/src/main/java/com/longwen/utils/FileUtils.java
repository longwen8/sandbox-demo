package com.longwen.utils;

import java.io.*;

public class FileUtils {

    public static void test(String path){
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(path+"en");
            int b = 0;
            int b1 = 0;
            try {
                while((b = fis.read()) != -1){
                    //每一个byte异或一个数字2
                    fos.write(b ^ 2);
                }
                fos.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileUtils.test("D:\\jar\\MyTest.class");
    }

}
