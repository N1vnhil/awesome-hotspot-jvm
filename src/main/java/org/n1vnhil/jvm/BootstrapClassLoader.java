package org.n1vnhil.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class BootstrapClassLoader {

    private final List<String> classPath;

    public BootstrapClassLoader(List<String> classPath) {
        this.classPath = classPath;
    }

    public ClassFile loadClass(String fqcn) throws ClassNotFoundException {
        return classPath.stream()
                .map(path -> tryLoad(path, fqcn))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ClassNotFoundException("找不到" + fqcn));
    }

    private ClassFile tryLoad(String path, String mainClass) {
        File classFilePath = new File(path, mainClass.replace(".", "/") + ".class");
        if(!classFilePath.exists()) {
            return null;
        }

        try {
            byte[] bytes = Files.readAllBytes(classFilePath.toPath());
            return new ClassFileParser().parse(bytes);
        } catch (Exception e) {
            return null;
        }
    }
}
