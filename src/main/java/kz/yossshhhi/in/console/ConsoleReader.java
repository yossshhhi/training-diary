package kz.yossshhhi.in.console;

import java.util.Scanner;

public class ConsoleReader {
    private final Scanner scanner;

    public ConsoleReader() {
        scanner = new Scanner(System.in);
    }

    public String read() {
        return scanner.nextLine().trim();
    }
}
