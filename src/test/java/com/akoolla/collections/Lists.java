package com.akoolla.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
