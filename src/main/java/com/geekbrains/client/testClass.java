package com.geekbrains.client;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class testClass {
    public static void main(String[] args) {

        String path  = "E:\\Java\\2021\\geekbrains\\filemanager\\1\\fileManager";
        String s = "ServerPath:" + path;

        String a  = s.substring(s.indexOf(":") + 1);
        System.out.println(a);


        File dir = new File("E:\\Java\\2021\\geekbrains\\filemanager\\1\\fileManager"); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);

        for (File s1 : lst ) {
            System.out.println(s1.getName());
        }

        System.out.println("\21BA");

        String testFileSend = "sendFile qweasd.txt";

        String fileName = testFileSend.split(" ")[1];

        System.out.println(fileName);
    }
}
