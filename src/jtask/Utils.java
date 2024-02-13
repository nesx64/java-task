/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jtask;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lolo
 */
public class Utils {

    /**
     * Return the number of lines in a file.
     *
     * @param in the file we want the number of lines.
     * @return the number of lines in the file.
     */
    static int getFileSize(File in) {
        int i = 0;
        Scanner sc;
        try {
            sc = new Scanner(in);
            while (sc.hasNext()) {
                i++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    /**
     * <strong> Converts a String input to a LocalDate.</strong><br>
     * Format output: YYYY-MM-DD
     *
     * @param input String to convert to a DateLocal
     * @return the converted String
     */
    static LocalDate convertFileInputToDate(String input) {
        int i = 0;
        int dtype = 0;
        int year = 0;
        int month = 0;
        int day = 0;
        while (dtype != 3) {
            StringBuilder temp = new StringBuilder();
            while (i < input.length() && input.charAt(i) != '-') {
                temp.append(input.charAt(i));
                i++;
            }
            if (temp.length() != 0) {
                int addValue = Integer.parseInt(temp.toString());
                switch (dtype) {
                    case 0 ->
                        year = addValue;
                    case 1 ->
                        month = addValue;
                    default ->
                        day = addValue;
                }
                dtype++;
            }
            i++;
        }
        return LocalDate.of(year, month, day);
    }

    /**
     * <strong> Converts a String input to a Boolean.</strong><br>
     * Can be wether true or false.
     *
     * @param input String to convert to a Boolean
     * @return the converted String
     */
    static Boolean convertInputToBoolean(String input) {
        if (input.equals("true") || input.equals("false")) {
            return input.equals("true");
        }
        return null;
    }

    /**
     * <strong>Ask the user for confirmation before adding a new
     * task.</strong><br>
     * Depends on the status of the task being added by the user.
     *
     * @param t Task created from user input in add protocol
     * @return User confirmation
     */
    static Boolean confirmAdd(Task t) {
        System.out.println("Before adding, let's review the task you want to add:");
        if (!t.isDone() && t.getEnd() != null) {
            t.display();
        } else {
            t.display();
        }
        Scanner sc = new Scanner(System.in);
        String userInput = "";
        System.out.println("Is it okay for you? (y)es or (n)o");
        while (!userInput.equals("y") && !userInput.equals("n")) {
            userInput = sc.nextLine();
            if (!userInput.equals("y") && !userInput.equals("n")) {
                System.err.println("jtask: input mismatch. Expected y or n.");
            }
        }
        return userInput.equals("y");
    }

    /**
     * jtask informations displayed on start.
     */
    final static String JTASK_INFO = " --- jtask " + Jtask.VERSION + " ---\n"
            + "GNU GPL-3.0 LICENSE\n"
            + "by nesx64 \n- NO WARRANTY PROVIDED -\n\n";

    /**
     * jtask main menu.
     */
    final static String JTASK_MENU = ""
            + " --   jtask   --\n\n"
            + "1. Add a new task\n"
            + "2. View active tasks\n"
            + "3. View inactive tasks\n"
            + "4. Reset task file\n"
            + "5. View all tasks\n"
            + "6. Save\n"
            + "7. Settings\n"
            + "8. Quit\n\n"
            + "--    " + Jtask.VERSION + "    --\n";

    /**
     * jtask settings.ini file (user, not default) header
     */
    final static String JTASK_SETTINGS_HEAD = ""
            + "##jtask user settings | " + Jtask.VERSION + "\n"
            + "#it's better to modify directly inside jtask instead of manually.\n"
            + "#do it at your own risk.\n"
            + "\n"
            + "#--------------------------------------\n\n";
}
