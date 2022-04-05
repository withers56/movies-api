package util;

import java.util.Scanner;

public class Input {
    private final Scanner scanner;

    public Input() {
        scanner =  new Scanner(System.in);
    }

    public String getString() {
        return scanner.nextLine();
    }

    public String getString(String prompt) {
        System.out.printf("%s", prompt);
        return scanner.nextLine();
    }

    public boolean yesNo(){
        System.out.print("Yes or no?: ");
        String yesOrNoString = scanner.nextLine().trim();

        return yesOrNoString.equalsIgnoreCase("Yes") || yesOrNoString.equalsIgnoreCase("y");
    }

    public boolean yesNo(String prompt){
        System.out.printf("%s", prompt);
        String yesOrNoString = scanner.nextLine().trim();

        return yesOrNoString.equalsIgnoreCase("Yes") || yesOrNoString.equalsIgnoreCase("y");
    }

    public int getInt (int min, int max) {

        while (true) {
            int userNum = getInt();

            if (min <= userNum && userNum <= max) {
                System.out.println("Valid!");
                return userNum;
            }
            System.out.println("Invalid.");
        }

    }

    public int getInt (int min, int max, String prompt) {
        System.out.printf("%s", prompt);

        while (true) {
            int userNum = getInt();

            if (min <= userNum && userNum <= max) {
                System.out.println("Valid!");
                return userNum;
            }
            System.out.println("Invalid.");
        }

    }

    public int getInt () {
        while (true) {
            try {
                return Integer.parseInt(getString());
            }catch (NumberFormatException e) {
                System.out.println("That is not a integer!");
            }
        }
    }

    public int getInt (String prompt) {
        System.out.printf("%s", prompt);

        return getInt();
    }

    public double getDouble (double min, double max) {
        while (true) {
            System.out.print("Enter a num between " + min + " and " + max + ": ");
            double userNum = getDouble();

            if (min <= userNum && userNum <= max) {
                System.out.println("Valid!");
                return userNum;
            }
            System.out.println("Invalid.");
        }
    }
    @SuppressWarnings("all")
    public double getDouble () {
        while (true) {
            try {
                String aString = getString();
                return Double.valueOf(aString);
            }catch (Throwable e) {
                System.out.println("That is not a number!");
            }
        }
    }

    public double getDouble (String prompt) {
        System.out.printf("%s", prompt);
        while (true) {
            try {
                return Double.parseDouble(getString());
            }catch (Throwable e) {
                System.out.println("That is not a number!");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
