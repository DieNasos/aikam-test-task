package ru.nsu.ccfit.beloglazov.aikamtest;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("ERROR: INVALID NUMBER OF ARGUMENTS.");
            System.out.println("REQUIRED: 1. COMMAND; 2. INPUT JSON-FILE; 3. OUTPUT JSON-FILE.");
            return;
        }

        String command = args[0];
        String input = args[1];
        String output = args[2];

        String url = "jdbc:postgresql://127.0.0.1:5432/aikamtest";
        String user = "postgres";
        String pass = "PostgreSQL";

        Executor executor = new Executor(url, user, pass, output);
        executor.execute(command, input);
        executor.finish();
    }
}