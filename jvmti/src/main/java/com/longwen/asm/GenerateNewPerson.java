package com.longwen.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassReader;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by huangxinping on 19/11/12.
 */
public class GenerateNewPerson {

    public static void main(String[] args) {

        // 使用全限定名，创建一个ClassReader对象
        try {
            ClassReader classReader = new ClassReader("com.longwen.asm.Person");

            // 构建一个ClassWriter对象，并设置让系统自动计算栈和本地变量大小
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

            ClassAdapter classAdapter = new GeneralClassAdapter(classWriter);

            classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

            byte[] classFile = classWriter.toByteArray();

            File file = new File("/Users/huangxinping/java/alibaba/sandbox/demo/asm/com/longwen/asm/Person.class");

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(classFile);
            fos.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }
}
