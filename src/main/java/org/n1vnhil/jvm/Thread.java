package org.n1vnhil.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.GetStatic;
import tech.medivh.classpy.classfile.bytecode.Instruction;
import tech.medivh.classpy.classfile.bytecode.InvokeVirtual;
import tech.medivh.classpy.classfile.constant.ConstantMethodrefInfo;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class Thread {

    private final String threadName;

    private final JvmStack stack;

    private final PcRegister pcRegister;

    private BootstrapClassLoader classLoader;

    public Thread(String threadName, StackFrame stackFrame, BootstrapClassLoader classLoader) {
        this.threadName = threadName;
        this.stack = new JvmStack();
        this.stack.push(stackFrame);
        this.pcRegister = new PcRegister(stack);
        this.classLoader = classLoader;
    }

    public void start() throws Exception {
        for(Instruction instruction: pcRegister) {
            System.out.println(instruction);
            ConstantPool constantPool = stack.peek().constantPool;
            switch (instruction.getOpcode()) {
                case getstatic -> {
                    GetStatic getStatic = (GetStatic) instruction;
                    String className = getStatic.getClassName(constantPool);
                    String fieldName = getStatic.getFieldName(constantPool);
                    Object staticField = null;
                    if (className.contains("java")) {
                        Class<?> clazz = Class.forName(className);
                        Field declaredField = clazz.getDeclaredField(fieldName);
                        staticField = declaredField.get(null);
                        stack.peek().pushObjectToOperandStack(staticField);
                    }
                }

                case iconst_1 -> {
                    stack.peek().pushObjectToOperandStack(1);
                }

                case invokevirtual -> {
                    InvokeVirtual invokeVirtual = (InvokeVirtual) instruction;
                    ConstantMethodrefInfo methodrefInfo = invokeVirtual.getMethodInfo(constantPool);
                    String className = methodrefInfo.className(constantPool);
                    String methodName = methodrefInfo.methodName(constantPool);
                    List<String> params = methodrefInfo.paramClassName(constantPool);

                    if (className.contains("java")) {
                        Class<?> clazz = Class.forName(className);
                        Method declaredMethod = clazz.getDeclaredMethod(methodName, params.stream().map(this::nameToClass).toArray(Class[]::new));
                        Object[] args = new Object[params.size()];
                        for(int i = args.length - 1; i >= 0; i--) {
                            args[i] = stack.peek().getOperandStack().pop();
                        }
                        Object result = declaredMethod.invoke(stack.peek().getOperandStack().pop(), args);
                        if(!methodrefInfo.isVoid(constantPool)) {
                            stack.peek().getOperandStack().push(result);
                        }
                        break;
                    }

                    ClassFile classFile = classLoader.loadClass(className);
                    MethodInfo finalMethodInfo = classFile.getMethods(methodName).get(0);
                    Object[] args = new Object[params.size() + 1];
                    for (int i = args.length - 1; i >= 0; i--) {
                        args[i] = stack.peek().getOperandStack().pop();
                    }
                    StackFrame stackFrame = new StackFrame(finalMethodInfo, classFile.getConstantPool(), args);
                    stack.push(stackFrame);
                }

                case _return -> {
                    stack.pop();
                }

                default -> {
                    throw new RuntimeException("这个指令还没有实现：" + instruction);
                }
            }
        }
    }

    private Class<?> nameToClass(String className) {
        if(className.equals("int")) {
            return int.class;
        }

        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }
}
