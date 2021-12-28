package com.geekbrains;

import com.geekbrains.model.FileMessage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class test {

    public static void main(String[] args) {

        Path baseDir = Paths.get(System.getProperty("user.home"));

        Path baseDir2 = Paths.get(System.getProperty("user.dir"));
        Path dir3 = baseDir2.resolve("storage");

        System.out.println(baseDir);
        System.out.println(baseDir2);

        System.out.println(baseDir2.getParent());

        System.out.println(dir3);





    }

}
