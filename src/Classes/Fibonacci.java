/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Classes;

/**
* Purpose: This program calculates and times the calculation for
a Fibonacci number.
*/
public class Fibonacci {
    public static int fibonacci(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fibonacci(n-1) + fibonacci(n-2);
    }
    public static void of(int n) {
        calculate(n);
    }
    public static void of(String args) {
        int input = 0;
        try {
            input = Integer.parseInt(args);
        } catch(Exception e) {
            System.out.println ("Input value must be an integer");
            System.exit(1);
        }
        calculate(input);
    }
    private static void calculate(int input) {
        long startTime = System.currentTimeMillis();
        System.out.println("The Fibonacci number for " + input +
        " is " + fibonacci(input));
        long endTime = System.currentTimeMillis();
        System.out.println("Total time used = " +
        (endTime - startTime) + " milliseconds");
    }
}