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
            t.display(false);
        } else {
            t.display(false);
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
     * <strong>Retrieve task label from user input. </strong> <br>
     *
     * @return task label from user input
     */
    public static String retrieveLabel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("First, please type the task label:");
        return sc.nextLine();
    }

    /**
     * <strong>Retrieve task beginning date from user input.</strong> <br>
     * Required format: YYYY-MM-DD
     *
     * @return task beginning date as LocalDate object
     */
    public static LocalDate retrieveBegin() {
        LocalDate begin = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Then, please type the beginning date of the task:");
        boolean wrongDateFormat = true;
        while (wrongDateFormat) {
            System.out.println("FORMAT: YYYY-MM-DD");
            String temp = sc.nextLine();
            if (temp.length() != 10) {
                System.err.println("jtask: wrong format for date.");
            } else {
                wrongDateFormat = false;
                begin = LocalDate.parse(temp);
            }
        }
        return begin;
    }

    /**
     * <strong>Retrieve task status from user input.</strong> <br>
     *
     * @return task status from user input
     */
    public static Boolean retrieveDone() {
        System.out.println("Is the task already over? (y)es (n)o");
        Scanner sc = new Scanner(System.in);
        String resp = sc.nextLine();
        while (!resp.equals("y") && !resp.equals("n")) {
            System.err.println("jtask: input mismatch. Expected y or n.");
            resp = sc.nextLine();
        }
        return resp.equals("y");
    }

    /**
     * <strong>Retrieve task ending date from user input.</strong> <br>
     * Required format: YYYY-MM-DD
     *
     * @param done true iff task is done
     * @return task ending date as LocalDate object
     */
    public static LocalDate retrieveEnd(Boolean done) {
        if (done) {
            Boolean wrongDateFormat = true;
            Scanner sc = new Scanner(System.in);
            LocalDate end = null;
            System.out.println("Then, please type the end date of the task:");
            while (wrongDateFormat) {
                System.out.println("FORMAT: YYYY-MM-DD");
                String temp = sc.nextLine();
                if (temp.length() != 10) {
                    System.err.println("jtask: wrong format for date.");
                } else {
                    wrongDateFormat = false;
                    end = LocalDate.parse(temp);
                }
            }
            return end;
        }
        return null;
    }

    /**
     * <strong>Retrieve task type from user input. </strong> <br>
     * Can be only one valid TaskType enum element.
     *
     * @return task type from user input.
     */
    public static TaskType retrieveType() {
        System.out.println("Then, please type the type of the task:");
        boolean wrongType = true;
        Scanner sc = new Scanner(System.in);
        TaskType type = null;
        while (wrongType) {
            System.out.println("Types : CHORES, JOB, MEETING, STUDY, ENTERTAINMENT");
            String temp = sc.nextLine();
            if (TaskType.isValidTaskType(temp)) {
                wrongType = false;
                type = TaskType.convertFileInputToTaskType(temp);
            } else {
                System.err.printf("jtask: %s wrong task type format.\n", temp);
                System.err.flush();
            }
        }
        return type;
    }

    /**
     * <strong>Retrieve task description from user input. </strong> <br>
     * No space allowed for now.
     *
     * @return task description from user input
     */
    public static String retrieveDesc() {
        System.out.println("Then, please type the description of the task:");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
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
            + "2. Remove a task\n"
            + "3. Edit a task\n"
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
