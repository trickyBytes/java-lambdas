package com.akoolla.strings;

import static org.junit.Assert.*;

import java.util.function.IntConsumer;

import org.junit.Test;

/**
 * Iteration.
 *
 * @author richardTiffin
 * @version $Id$
 */
public class Iteration {

    private final String str = "w00t";

    @Test
    public void iterateCharsOfAString() throws Exception {
        //Ok but prints out integers for char rather than char
        str.chars()
        .forEach(System.out::println);

        //Use the printChar function from this test class
        str.chars()
        .forEach(Iteration::printChar);
        
        //Treat them as chars from the start
        str.chars()
        .mapToObj(ch -> Character.valueOf((char)ch))
        .forEach(System.out::println);

    }
    
    private static void printChar(int aChar) {
        System.out.println((char)(aChar));
    }
}
