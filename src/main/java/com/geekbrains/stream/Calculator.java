package com.geekbrains.stream;

public class Calculator {

    public int calculate(int x, int y, IntFunction function) {
        return function.foo(x, y);
    }

    public int sum(int x, int y) {
        return calculate(x, y, Integer::sum);
    }

}
