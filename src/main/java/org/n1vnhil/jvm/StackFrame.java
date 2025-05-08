package org.n1vnhil.jvm;

import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.Instruction;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class StackFrame {

    private MethodInfo methodInfo;

    /**
     * 局部变量表
     */
    private Object[] localVariable;

    /**
     * 操作数栈
     */
    private Deque<Object> operandStack;

    /**
     * 字节码
     */
    private List<Instruction> codes;

    /**
     * 当前执行位置
     */
    int currentIndex;

    public ConstantPool constantPool;

    public StackFrame(MethodInfo methodInfo, ConstantPool constantPool, Object... args) {
        this.methodInfo = methodInfo;
        this.localVariable = new Object[methodInfo.getMaxLocals()];
        this.operandStack = new ArrayDeque<>();
        this.codes = methodInfo.getCodes();
        this.constantPool = constantPool;
        System.arraycopy(args, 0, localVariable, 0, args.length);
    }

    public Instruction getNextInstruction() {
        return codes.get(currentIndex++);
    }

    public void pushObjectToOperandStack(Object object) {
        this.operandStack.push(object);
    }

    public Deque<Object> getOperandStack() {
        return this.operandStack;
    }
}
