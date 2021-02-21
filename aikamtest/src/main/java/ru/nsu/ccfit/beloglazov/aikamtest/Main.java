package ru.nsu.ccfit.beloglazov.aikamtest;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("ERROR: INVALID NUMBER OF ARGUMENTS.");
            System.out.println("REQUIRED: 1. COMMAND; 2. INPUT JSON-FILE; 3. OUTPUT JSON-FILE.");
            return;
        }

        try(Executor executor = new Executor()) {
            executor.execute(args[0], args[1], args[2]);
        }
    }
}