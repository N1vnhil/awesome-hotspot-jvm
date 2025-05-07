package org.n1vnhil.jvm;

public class Main {
    public static void main(String[] args) throws Exception {
        Hotspot hotspot = new Hotspot("org.n1vnhil.jvm.demo.Demo", "D:\\projects\\awesome-hotspot-jvm\\target\\classes");
        hotspot.start();
    }
}
