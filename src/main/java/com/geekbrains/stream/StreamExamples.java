package com.geekbrains.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExamples {
    public static void main(String[] args) throws IOException {
        Supplier<Stream<Integer>> streamSupplier =
                () -> Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Consumer<Integer> printer = x -> System.out.print(x + " ");
        // iterate
        streamSupplier.get()
                .forEach(printer);
        System.out.println();

        // filter odd
        streamSupplier.get()
                .filter(x -> x % 2 != 0)
                .forEach(printer);
        System.out.println();

        Predicate<Integer> less8 = x -> x < 8;
        Predicate<Integer> less11 = x -> x < 11;

        // allMatch все элементы удовлетворяют предикату
        System.out.println(
                streamSupplier.get()
                        .allMatch(less8)
        );

        System.out.println(
                streamSupplier.get()
                        .allMatch(less11)
        );

        // anyMatch - хотя бы один элемент удовлетворяет предикату
        System.out.println(
                streamSupplier.get()
                        .anyMatch(less8)
        );

        // noneMath - ни один элемент не удовлетворяет предикату
        System.out.println(
                streamSupplier.get()
                        .noneMatch(less11)
        );
        System.out.println();

        // map
        streamSupplier.get()
                .map(x -> x * 2)
                .peek(printer)
                .map(x -> String.valueOf(x).length())
                .forEach(printer);

        // flatMap

        List<List<String>> list = new ArrayList<>();
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        List<String> l3 = new ArrayList<>();
        l1.add("1");
        l1.add("2");
        l2.add("3");
        l3.add("4");
        l3.add("5");
        l3.add("6");
        list.add(l1);
        list.add(l2);
        list.add(l3);

        // map -> Stream<Stream<String>>
        // flatMap -> Stream<String>
        System.out.println();
        list.stream()
                .flatMap(Collection::stream)
                .forEach(System.out::println);

        // collect words from file

        // [a-zA-Z]+
        // [0-9]{1,7}
        // [abcZY,\\.\\+&*]

        // System.out.println("123".matches("[a-z]+"));

        Files.lines(Paths.get("serverFiles", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .map(word -> word.replaceAll("\\W", ""))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .sorted()
                .distinct()
                .forEach(System.out::println);

        // collect to list
        List<String> words = Files.lines(Paths.get("serverFiles", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .map(word -> word.replaceAll("\\W", ""))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .sorted()
                .distinct()
                .collect(Collectors.toList());
        System.out.println(words);

        // collect to String
        String wordsString = Files.lines(Paths.get("serverFiles", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .map(word -> word.replaceAll("\\W", ""))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .sorted()
                .distinct()
                .collect(Collectors.joining(", "));
        System.out.println(wordsString);

        // collect words to map with counting
        Map<String, Integer> wordsMap = Files.lines(Paths.get("serverFiles", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .map(word -> word.replaceAll("\\W", ""))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                x -> 1,
                                Integer::sum
                        )
                );

        System.out.println(wordsMap);

        // top 10 words in text
        List<Map.Entry<String, Integer>> top10 = wordsMap.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .limit(10)
                .collect(Collectors.toList());

        System.out.println(top10);

        wordsMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .limit(10)
                .forEach(System.out::println);

        // reduce - вычисление на stream
        // sum of digits
        streamSupplier.get()
                .reduce(Integer::sum)
                .ifPresent(System.out::println);

        Integer sum = streamSupplier.get()
                .reduce(Integer::sum)
                .orElse(0);
        System.out.println(sum);

        // collect to list with reduce
        List<Integer> integers = streamSupplier.get()
                .reduce(
                        new ArrayList<>(),
                        (l, value) -> {
                            l.add(value);
                            return l;
                        },
                        (left, right) -> left
                );

        Map<Integer, Integer> map = Stream.of(1, 1, 1, 2, 2, 3)
                .reduce(
                        new HashMap<>(),
                        (l, value) -> {
                            if (l.containsKey(value)) {
                                l.put(value, l.get(value) + 1);
                            } else {
                                l.put(value, 1);
                            }
                            return l;
                        },
                        (left, right) -> left
                );
        System.out.println(integers);
        System.out.println(map);

    }
}
