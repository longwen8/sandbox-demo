package com.longwen.asm;

import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AsmTest {
    public static void main(String[] args) {
        new AsmTest().test1();
        System.out.println("=========================");
        new AsmTest().test2();
    }

    private void test1(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("D://jar/ByteCodeDemo.class");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ClassReader classReader = null;
        try {
            classReader = new ClassReader(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        //Java8选择ASM5，
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                System.out.println("field:" + name);
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                System.out.println("方法" + name);
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };
        //忽略调试信息
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
    }

    private void test2(){
        try{

            FileInputStream fileInputStream = new FileInputStream("D://jar/ByteCodeDemo.class");
            ClassReader classReader = new ClassReader(fileInputStream);
            ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            //忽略调试信息
            ClassNode classNode = new ClassNode(org.objectweb.asm.Opcodes.ASM5);
            classReader.accept(classNode, ClassReader.SKIP_DEBUG);
            for (MethodNode methodNode:classNode.methods) {
                System.out.println(methodNode.name);
            }
            classNode.accept(cw);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
