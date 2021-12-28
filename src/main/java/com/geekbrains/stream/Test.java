package com.geekbrains.stream;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Test {

    public static void main(String[] args) {

        // since java8
        IntFunction mul = (x, y) -> x * y;
        IntFunction sum = Integer::sum;

        System.out.println(mul.foo(2, 3));
        System.out.println(sum.foo(2, 3));

        Calculator calculator = new Calculator();

        calculator.calculate(2, 3, (x, y) -> x * y);
        calculator.calculate(2, 3, Integer::sum);

        // Stream interfaces
        // consumer -> forEach(terminal) -> void, peek(stream) -> Stream<T>
        Consumer<String> consumer = arg -> System.out.println("Hello " + arg);
        consumer.accept("world!");
        Consumer<String> print = System.out::println;
        print.accept("123");

        // Predicate -> filter(stream), (allMatch, anyMatch, noneMatch)(terminal)
        Predicate<Integer> isOdd = x -> x % 2 != 0;
        Predicate<String> isEmpty = String::isEmpty;
        System.out.println(isOdd.test(12));
        System.out.println(isEmpty.test("123"));

        // Function -> (map, flatMap)(stream)
        Function<String, Integer> length = String::length;
        System.out.println(length.apply("123"));

        // Supplier -> collector (terminal)
        Supplier<HashMap<String, TreeSet<Integer>>> supplier = () -> {
            HashMap<String, TreeSet<Integer>> map = new HashMap<>();
            map.put("key", new TreeSet<>());
            map.get("key").add(1);
            map.get("key").add(2);
            map.get("key").add(3);
            return map;
        };

        System.out.println(supplier.get());
    }
}
