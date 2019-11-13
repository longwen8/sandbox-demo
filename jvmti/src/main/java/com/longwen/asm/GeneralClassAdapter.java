package com.longwen.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.*;

/**
 * Created by huangxinping on 19/11/12.
 */
public class GeneralClassAdapter extends ClassAdapter {

    public GeneralClassAdapter(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        // 当是sayHello方法是做对应的修改
        if(name.equals("sayHello")){
            MethodVisitor newMv = new sayHelloMethodAdapter(mv);
            return newMv;
        }else{
            return mv;
        }
    }

    // 定义一个自己的方法访问类
    class sayHelloMethodAdapter extends MethodAdapter {

        public sayHelloMethodAdapter(MethodVisitor mv) {
            super(mv);
        }

        // 在源方法前去修改方法内容,这部分的修改将加载源方法的字节码之前
        @Override
        public void visitCode() {
            // 记载隐含的this对象，这是每个JAVA方法都有的
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            // 从常量池中加载“yy”字符到栈顶
            mv.visitLdcInsn("xphuang");
            // 将栈顶的"yy"赋值给name属性
            mv.visitFieldInsn(Opcodes.PUTFIELD, Type.getInternalName(Person.class), "name", Type.getDescriptor(String.class));
        }
    }

}
