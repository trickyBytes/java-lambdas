package com.akoolla.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

public class Lists {
    private final List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

    @Test
    public void listIterations() throws Exception {
        for (String friend : friends) {
            System.out.println(friend);
        }

        // Tell the compiler the type we're expecting
        friends.forEach((final String name) -> System.out.println(name));

        // Compiler is clever and can infer type
        friends.forEach((name) -> System.out.println(name));

        // Ah but we don't need to declare the variable at all, but it then becomes non-final, don't modify
        friends.forEach(name -> System.out.println(name));

        // Maybe we could just use a method reference
        friends.forEach(System.out::println);
    }

    @Test
    public void transformingLists() throws Exception {
        final List<String> upperCaseNames = new ArrayList<String>();

        for (String name : friends) {
            upperCaseNames.add(name.toUpperCase());
        }
        System.out.println(upperCaseNames);
        upperCaseNames.clear();

        // Use internal iterator, still requires us to create an empty list
        friends.forEach(name -> upperCaseNames.add(name.toUpperCase()));
        System.out.println(upperCaseNames);
        upperCaseNames.clear();

        // Use Map as a lambda
        friends.stream()
                .map(name -> name.toUpperCase())
                .forEach(name -> System.out.print(name + " "));
        System.out.println();

        // Demonstrate doing something other than output string
        friends.stream()
                .map(name -> name.length())
                .forEach(count -> System.out.print(count + " "));
        System.out.println();

        // Use method reference
        friends.stream()
                .map(String::toUpperCase)
                .forEach(name -> System.out.print(name + " "));

        System.out.println();
    }

    @Test
    public void findingThings() throws Exception {
        List<String> startsWithN = new ArrayList<>();
        for (String name : friends) {
            if (name.startsWith("N")) {
                startsWithN.add(name);
            }
        }
        System.out.println(startsWithN);

        // That's a ton of code, simplify using a filter
        startsWithN = friends.stream()
                .filter(name -> name.startsWith("N"))
                .collect(Collectors.toList());

        System.out.println(startsWithN);
    }

    @Test
    public void reUse() throws Exception {
        final Predicate<String> startsWithN = name -> name.startsWith("N");

        final long countFriendsStartN = friends.stream()
                .filter(startsWithN)
                .count();

        System.out.println(String.format("Found %d names", countFriendsStartN));
    }

    @Test
    public void reUseAndLexicalScoping() throws Exception {
        // Start with a static method
        long countFriendsStartN = friends.stream()
                .filter(checkIfStartsWith("N"))
                .count();

        // Avoid static methods by declaring a function
        Function<String, Predicate<String>> startsWithLetter = (String letter) -> {
            Predicate<String> checkStarts = (String name) -> name.startsWith(letter);
            return checkStarts;
        };
        System.out.println(String.format("Found %d names", countFriendsStartN));

        countFriendsStartN = friends.stream()
                .filter(startsWithLetter.apply("N"))
                .count();
        System.out.println(String.format("Found %d names", countFriendsStartN));

        // Let the java compiler infer types
        startsWithLetter = letter -> name -> name.startsWith(letter);
        countFriendsStartN = friends.stream()
                .filter(startsWithLetter.apply("N"))
                .count();
        System.out.println(String.format("Found %d names", countFriendsStartN));

        long countFriendsStartB = friends.stream()
                .filter(startsWithLetter.apply("N"))
                .count();
        System.out.println(String.format("Found %d names", countFriendsStartB));
    }

    @Test
    public void getASingleElement() throws Exception {
        pickName(friends, "N");
        pickName(friends, "Z");
    }

    @Test
    public void getLongestNameUsingReduction() throws Exception {
        final Optional<String> aLongName = friends
                .stream()
                .reduce((name1, name2) -> name1.length() >= name2.length() ? name1 : name2);

        aLongName.ifPresent(name -> System.out.println(String.format("A longest name: %s", name)));
    }

    @Test
    public void joiningElements() throws Exception {
        // Old skool
        for (int i = 0; i < friends.size() - 1; i++) {
            System.out.println(friends.get(i) + ",");
        }
        if (friends.size() > 0)
            System.out.println(friends.get(friends.size() - 1));

        // Better
        System.out.println(String.join(", ", friends));

        // But Using streams gives us parallel powers...
        System.out.println(
                friends.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.joining(", ")));
    }

    public static void pickName(final List<String> names, final String startingLetter) {
        final Optional<String> foundName = names.stream()
                .filter(name -> name.startsWith(startingLetter))
                .findFirst();

        System.out.println(String.format("A name starting with %s: %s",
                startingLetter, foundName.orElse("No name found")));
    }

    public static Predicate<String> checkIfStartsWith(final String letter) {
        return name -> name.startsWith(letter);
    }
}
