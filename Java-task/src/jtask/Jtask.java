/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package jtask;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nesx64
 */
public class Jtask {

    ArrayList<Task> loadedList;
    ArrayList<Task> addedTasks;
    String saveFilePath;
    Jsettings settings;
    final static int EXIT_PROGRAM = 7;
    final static String SAVEFILE_SEPARATOR = ";:;";
    final static String VERSION = "v0.1";

    /**
     * Main constructor for Jtask class.
     */
    public Jtask() {
        loadedList = new ArrayList<>();
        addedTasks = new ArrayList<>();
        settings = new Jsettings();
        saveFilePath = settings.getSavePath();
    }

    /**
     * Launching jtask program after init.
     */
    public void run() {
        Scanner sc = new Scanner(System.in);
        int userInput = 0;
        while (userInput != EXIT_PROGRAM) {
            System.out.println(Utils.JTASK_MENU);
            userInput = sc.nextInt();
            inputCheck(userInput);
        }
    }

    /**
     * Check wether existing file containing all tasks already exists or not. If
     * the file already exists, it will load everything inside the global list.
     * If autoload is enabled, will not ask for typing and will load
     * automatically the save file.
     */
    public void init() {
        if (!settings.autoloadEnabled()) {
            System.out.println("If you already have an existing file with data, please type the file path, else the file will be created: ");
            Scanner sc = new Scanner(System.in);
            String filePath = sc.nextLine();
            saveFilePath = filePath;
            try {
                File data = new File(filePath);
                if (data.createNewFile()) {
                    System.out.printf("Data file created: %s\n", data.getName());
                    System.out.printf("jtask program is setting up...\n");
                } else {
                    init_loadSequence(data);
                }
            } catch (IOException e) {
                System.err.printf("No file name provided.\n");
                this.init();
            }
        } else {
            try {
                init_loadSequence(settings.getSaveFile());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Jtask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.printf("jtask ready!\n\n");
    }

    /**
     * <strong>Exit jtask program.</strong> <br>Save in the save file all new
     * added tasks, only if autosave is enabled.
     */
    public void exit() {
        if (settings.autosaveEnabled()) {
            System.out.printf("jtask: saving ...\n");
            if (!addedTasks.isEmpty()) {
                try {
                    PrintWriter pw = new PrintWriter(new FileWriter(saveFilePath, true));
                    for (Task t : addedTasks) {
                        if (t.getStatus()) {
                            System.out.println("Writing finished tasks.");
                            pw.printf("%s%s%s%s%s%s%s%s%s%s%s\n", t.getLabel(), SAVEFILE_SEPARATOR, t.getBeginning(), SAVEFILE_SEPARATOR,
                                    t.getEnd(), SAVEFILE_SEPARATOR, t.getTaskType(), SAVEFILE_SEPARATOR, t.getDescription(), SAVEFILE_SEPARATOR, t.getStatus());
                        } else {
                            System.out.println("Writing unfinished tasks.");
                            pw.printf("%s%s%s%s%s%s%s%s%s\n", t.getLabel(), SAVEFILE_SEPARATOR, t.getBeginning(), SAVEFILE_SEPARATOR,
                                    t.getTaskType(), SAVEFILE_SEPARATOR, t.getDescription(), SAVEFILE_SEPARATOR, t.getStatus());
                        }
                    }
                    pw.flush();
                    pw.close();
                } catch (IOException ex) {
                    Logger.getLogger(Jtask.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("jtask: successfully saved.\nProgram will now exit.\n");
            } else {
                System.out.println("jtask: no new tasks was added, don't need to save anything.\n");
            }

        } else {
            System.out.println("jtask: warning: autosave is disabled. Any new tasks added won't be saved.\n");
        }
        System.out.println("Thanks for using jtask.");
        System.out.println("If you've faced any bugs, please contact me on https://github.com/nesx64/jtask");
    }

    /**
     * <strong> Loading sequence of jtask init. </strong> <br> Will load save
     * file if it exists.
     */
    private void init_loadSequence(File filePath) throws FileNotFoundException {
        System.out.printf("Loading %s ...\n", filePath);
        Scanner fileInput = new Scanner(filePath);
        while (fileInput.hasNext()) {
            String currentLine = fileInput.next();
            String[] elements = currentLine.split(SAVEFILE_SEPARATOR);
            if (elements.length < 5 || elements.length > 6) {
                jtask_err_wrongFormat(filePath.toString());
            }
            LocalDate begin = Utils.convertFileInputToDate(elements[1]);

            if (elements.length > 5) {
                LocalDate end = Utils.convertFileInputToDate(elements[2]);
                TaskType type = TaskType.convertFileInputToTaskType(elements[3]);
                Boolean status = Utils.convertFileInputToBoolean(elements[5]);
                loadedList.add(new Task(elements[0], begin, end, type, elements[4], status));
            } else {
                TaskType type = TaskType.convertFileInputToTaskType(elements[2]);
                Boolean status = Utils.convertFileInputToBoolean(elements[4]);
                loadedList.add(new Task(elements[0], begin, type, elements[3], status));
            }
        }
    }

    /**
     * <strong>Will check user input and call for the required
     * command.</strong><br>
     * Output error if value is incorrect.
     *
     * @param input user input to select the command
     */
    private void inputCheck(int input) {
        if (input > 7 || input < 1) {
            System.err.printf("jtask: error: incorrect input for menu selection.");
            return;
        }
        switch (input) {
            case 1 ->
                jtask_add();
            case 2 ->
                jtask_viewActive();
            case 3 ->
                jtask_viewInactive();
            case 4 ->
                jtask_reset();
            case 5 ->
                jtask_viewAll();
            case 6 ->
                jtask_settings();
        }
    }

    /**
     * <strong> Main method to add a new task in the addedTasks list.</strong>
     * <br> Will not save in the save file until the user exits jtask, with
     * autosave enabled.
     */
    private void jtask_add() {
        boolean valid = false;
        String label = "";
        LocalDate begin = null;
        LocalDate end = null;
        TaskType type = null;
        String desc = "";
        String resp = "";
        while (!valid) {
            System.out.println("jtask: add a new task.");
            Scanner sc = new Scanner(System.in);
            System.out.println("First, please type the task label:");
            label = sc.nextLine();
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
            System.out.println("Is the task already over? (y)es (n)o");
            resp = sc.nextLine();
            while (!resp.equals("y") && !resp.equals("n")) {
                System.err.println("jtask: input mismatch. Expected y or n.");
                resp = sc.nextLine();
            }
            wrongDateFormat = true;
            if (resp.equals("y")) {
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
            }
            System.out.println("Then, please type the type of the task:");
            boolean wrongType = true;
            while (wrongType) {
                System.out.println("Types : CHORES, JOB, MEETING, STUDY, ENTERTAINMENT");
                String temp = sc.nextLine();
                if (TaskType.isValidTaskType(temp)) {
                    wrongType = false;
                    type = TaskType.convertFileInputToTaskType(temp);
                }
            }
            System.out.println("Then, please type the description of the task:");
            desc = sc.nextLine();
            valid = Utils.confirmAdd(label, begin, end, type, desc, resp);
        }
        if (resp.equals("y")) {
            addedTasks.add(new Task(label, begin, end, type, desc, true));
        } else {
            addedTasks.add(new Task(label, begin, type, desc, false));
        }
        System.out.printf("Successfully added %s task. \n", label);
    }

    /**
     * TODO
     */
    private void jtask_viewActive() {
        System.out.println("coming for v0.2");
    }

    /**
     * TODO
     */
    private void jtask_viewInactive() {
        System.out.println("coming for v0.2");
    }

    /**
     * TODO
     */
    private void jtask_reset() {
        System.out.println("coming for v0.2");
    }

    /**
     * TODO
     */
    private void jtask_viewAll() {
        System.out.println("coming for v0.2");
    }

    /**
     * <strong>Display all settings state.</strong><br>
     * Will also ask if the user want to change a value.
     */
    private void jtask_settings() {
        System.out.println("-- Settings --");
        System.out.println("Currently:");
        System.out.printf("autosave: %s\n", settings.autosaveEnabled());
        System.out.printf("autoload: %s\n", settings.autoloadEnabled());
        System.out.printf("save file path: %s\n\n", settings.getSavePath());
    }

    /**
     * <strong>Outputs error for wrong save file formatting.</strong><br>
     * Displays the expected line format.
     *
     * @param fileName file name that has wrong format.
     */
    private void jtask_err_wrongFormat(String fileName) {
        System.err.printf("jtask: error: file %s has wrong format.\n", fileName);
        System.err.printf("Expected line format:\n");
        System.err.printf("[LABEL];:;[BEGIN DATE];:;__optional__[END DATE];:;[TASK TYPE];:;[DESCRIPTION];:;[TASK STATUS]\n");
        System.err.printf("Check carefully for ;:; separator, arg numbers. Description cannot have spaces.\n");
        System.err.printf("Dates format : YYYY-MM-DD\n\n");
        System.exit(1);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Jtask global = new Jtask();
        System.out.print(Utils.JTASK_INFO);
        global.init();
        global.run();
        global.exit();
    }
}
