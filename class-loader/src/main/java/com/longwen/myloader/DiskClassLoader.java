package com.longwen.myloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DiskClassLoader extends ClassLoader {

    private String myLibPath;

    public DiskClassLoader(String path){
        myLibPath = path;
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String fileName = getFileName(name);

        File file = new File(myLibPath,fileName);

        try {
            FileInputStream is = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            is.close();
            bos.close();

            return defineClass(name,data,0,data.length);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return super.findClass(name);
    }


    //获取要加载 的class文件名
    private String getFileName(String name) {
        // TODO Auto-generated method stub
        int index = name.lastIndexOf('.');
        if(index == -1){
            return name+".class";
        }else{
            return name.substring(index + 1)+".class";
        }
    }

}
