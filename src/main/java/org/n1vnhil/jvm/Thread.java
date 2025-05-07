package org.n1vnhil.jvm;

import tech.medivh.classpy.classfile.bytecode.Instruction;

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

    public void start() {
        for(Instruction instruction: pcRegister) {
            System.out.println(instruction);
            switch (instruction.getOpcode()) {
                case getstatic -> {

                }

                case iconst_1 -> {

                }

                case invokevirtual -> {

                }
            }
        }
    }
}
