package org.n1vnhil.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;
import tech.medivh.classpy.classfile.MethodInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Hotspot {

    private String mainClass;

    private List<String> classPath;

    private BootstrapClassLoader classLoader;

    public Hotspot(String mainClass, String classPathString) {
        this.mainClass = mainClass;
        this.classLoader = new BootstrapClassLoader(Arrays.asList(classPathString.split("/")));
    }


    public void start() throws ClassNotFoundException {
        ClassFile mainClassFile = classLoader.loadClass(mainClass);
        StackFrame mainFrame = new StackFrame(mainClassFile.getMainMethod(), mainClassFile.getConstantPool());
        Thread mainThread = new Thread("main", mainFrame, classLoader);
        mainThread.start();
    }




}
