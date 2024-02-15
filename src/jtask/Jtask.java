package jtask;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author nesx64
 */
public class Jtask {

    final ArrayList<Task> loadedList;
    final ArrayList<Task> addedTasks;
    String saveFilePath;
    final Jsettings settings;
    final static int EXIT_PROGRAM = 8;
    final static String SAVE_FILE_SEPARATOR = ";:;";
    final static String VERSION = "v0.3.1";
    final static int ERROR_EXIT_CODE = 1;

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
     * Check whether existing file containing all tasks already exists or not. If
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
                init_loadSequence(new File(settings.getSavePath()));
            } catch (FileNotFoundException ex) {
                System.err.printf("jtask: loading data error: save file was not found.\n");
                System.err.printf("as autoload is enabled, it will now be disabled.\n\n");
                System.err.printf("next, you'll have to specify a save file.\n\n");
                System.err.flush();
                settings.setAutoload(Boolean.FALSE);
                init();
            }
        }
        System.out.printf("jtask ready!\n\n");
    }

    /**
     * <strong>Exit jtask program.</strong> <br>
     * Save in the save file all new added tasks, only if autosave is enabled.
     */
    public void exit() {
        if (settings.autosaveEnabled()) {
            System.out.printf("jtask: saving ...\n");
            if (!addedTasks.isEmpty()) {
                save();
                System.out.println("Program will now exit.\n");
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
     * <strong>Save jtask tasks to save file.</strong>
     */
    private void save() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(saveFilePath, false));
            if (!loadedList.isEmpty()) {
                writeSaveFile(pw, loadedList);
            }
            writeSaveFile(pw, addedTasks);
            loadedList.addAll(addedTasks);
            addedTasks.clear();
            System.out.println("jtask: successfully saved.\n\n");
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(Jtask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * <strong> Write in save file from the given list all tasks.</strong> <br>
     *
     * @param pw printWriter used to write into the save file
     * @param list list to be copied into the save file
     */
    private void writeSaveFile(PrintWriter pw, ArrayList<Task> list) {
        for (Task t : list) {
            if (t.isDone()) {
                pw.printf("%s%s%s%s%s%s%s%s%s%s%s\n", t.getLabel(), SAVE_FILE_SEPARATOR, t.getBeginning(), SAVE_FILE_SEPARATOR,
                        t.getEnd(), SAVE_FILE_SEPARATOR, t.getTaskType(), SAVE_FILE_SEPARATOR, t.getDescription(), SAVE_FILE_SEPARATOR, t.isDone());
            } else {
                pw.printf("%s%s%s%s%s%s%s%s%s\n", t.getLabel(), SAVE_FILE_SEPARATOR, t.getBeginning(), SAVE_FILE_SEPARATOR,
                        t.getTaskType(), SAVE_FILE_SEPARATOR, t.getDescription(), SAVE_FILE_SEPARATOR, t.isDone());
            }
        }
    }

    /**
     * <strong> Loading sequence of jtask init. </strong> <br> Will load save
     * file if it exists.
     */
    private void init_loadSequence(File filePath) throws FileNotFoundException {
        System.out.printf("Loading %s ...\n", filePath);
        Scanner fileInput = new Scanner(filePath);
        while (fileInput.hasNextLine()) {
            String currentLine = fileInput.nextLine();
            String[] elements = currentLine.split(SAVE_FILE_SEPARATOR);
            if (elements.length < 5 || elements.length > 6) {
                jtask_err_wrongFormat(filePath.toString(), currentLine);
            }
            LocalDate begin = Utils.convertFileInputToDate(elements[1]);

            if (elements.length > 5) {
                LocalDate end = Utils.convertFileInputToDate(elements[2]);
                TaskType type = TaskType.convertFileInputToTaskType(elements[3]);
                Boolean status = Utils.convertInputToBoolean(elements[5]);
                loadedList.add(new Task(elements[0], begin, end, type, elements[4], status));
            } else {
                TaskType type = TaskType.convertFileInputToTaskType(elements[2]);
                Boolean status = Utils.convertInputToBoolean(elements[4]);
                loadedList.add(new Task(elements[0], begin, null, type, elements[3], status));
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
        if (input > 8 || input < 1) {
            System.err.printf("jtask: error: incorrect input for menu selection.");
            return;
        }
        switch (input) {
            case 1 -> jtask_add();
            case 2 -> jtask_remove();
            case 3 -> jtask_edit();
            case 4 -> jtask_reset();
            case 5 -> jtask_viewAll(false);
            case 6 -> save();
            case 7 -> jtask_settings();
        }
    }

    /**
     * <strong> Main method to add a new task in the addedTasks list.</strong>
     * <br> Will not save in the save file until the user exits jtask, with
     * autosave enabled.
     */
    private void jtask_add() {
        System.out.println("jtask: add a new task.");
        Task t;
        do {
            t = add_collectProtocol();
        } while (!Utils.confirmAdd(t));
        if (t.isDone()) {
            addedTasks.add(t);
        } else {
            addedTasks.add(t);
        }
        System.out.printf("Successfully added %s task. \n", t.getLabel());
    }

    /**
     * <strong> Main protocol to retrieve new task info. </strong><br>
     * Will ask for every needed info to create a new task.<br>
     *
     * @return the task made of user typed infos
     */
    private Task add_collectProtocol() {
        String label = Utils.retrieveLabel();
        LocalDate begin = Utils.retrieveBegin();
        Boolean done = Utils.retrieveDone();
        return new Task(label, begin, Utils.retrieveEnd(done), Utils.retrieveType(), Utils.retrieveDesc(), done);
    }

    /**
     * <strong> Remove a selected task from task list.</strong> <br>
     * Will automatically save before removing.
     */
    private void jtask_remove() {
        save();
        System.out.println("jtask: remove mode");
        Integer toRemove;
        do {
            toRemove = selectTask();
        } while (toRemove == null);
        loadedList.remove(loadedList.get(toRemove));
        Task.shiftLeftTaskListKeys(loadedList, toRemove - 1);
        save();
    }

    /**
     * <strong> Edit a selected task from task list.</strong> <br>
     * Will automatically save before editing.
     */
    private void jtask_edit() {
        save();
        System.out.println("jtask: edition mode");
        Integer toEditKey;
        do {
            toEditKey = selectTask();
        } while (toEditKey == null);
        Task toEdit = loadedList.get(toEditKey);
        editProtocol(toEdit);
        save();
    }

    /**
     * <strong> Main protocol to edit a task.</strong> <br>
     * Will automatically save before removing.
     */
    private void editProtocol(Task toEdit) {
        System.out.println("You chose to edit :");
        toEdit.display(true);
        int input;
        System.out.println("Select what you want to edit:");
        System.out.println("(1) Title  (2) Description  (3) Status  (4) Back to menu");
        input = new Scanner(System.in).nextInt();
        switch (input) {
            case 1 -> toEdit.setLabel(Utils.retrieveLabel());
            case 2 -> toEdit.setDesc(Utils.retrieveDesc());
            case 3 -> toEdit.setToDone(Utils.retrieveEnd(Boolean.TRUE));
        }
        if (input != 4) {
            System.out.printf("Successfully edited task %s.\n", toEdit.getLabel());
        }

    }

    /**
     * <strong> Make user select a task from its key.</strong> <br>
     * Will check if the given key corresponds to a real task.
     *
     * @return the task key
     */
    private Integer selectTask() {
        jtask_viewAll(true);
        System.out.println("Select the task with the corresponding key:");
        int input = new Scanner(System.in).nextInt();
        if (loadedList.contains(Task.searchByKey(loadedList, input))) {
            return input;
        } else {
            System.err.println("jtask: selected task does not exist. Check selected key.");
            System.err.flush();
            return null;
        }
    }

    /**
     * TODO
     */
    private void jtask_reset() {
        System.out.println("coming for newer versions.");
    }

    /**
     * <strong> Display all tasks </strong> <br>
     * Won't filter status.
     */
    private void jtask_viewAll(boolean showKey) {
        if (!showKey) {
            System.out.printf("---- Existing tasks ----\n\n");
            if (!loadedList.isEmpty()) {

                for (Task t : loadedList) {
                    t.display(false);
                }
            } else {
                System.out.printf("No existing tasks. Nothing to display.\n\n");
            }
            System.out.printf("---- Added tasks ----\n\n");
            if (!addedTasks.isEmpty()) {

                for (Task t : addedTasks) {
                    t.display(false);
                }
            } else {
                System.out.printf("No new added tasks. Nothing to display.\n\n");
            }
        } else {
            System.out.printf("---- Existing tasks ----\n\n");
            if (!loadedList.isEmpty()) {

                for (Task t : loadedList) {
                    t.display(true);
                }
            } else {
                System.out.printf("No existing tasks. Nothing to display.\n\n");
            }
            System.out.printf("---- Added tasks ----\n\n");
            if (!addedTasks.isEmpty()) {

                for (Task t : addedTasks) {
                    t.display(true);
                }
            } else {
                System.out.printf("No new added tasks. Nothing to display.\n\n");
            }
        }
    }

    /**
     * <strong>Display all settings state.</strong><br>
     * Will also ask if the user wants to change a value.<br>
     * If any setting is modified, the <code> settings.ini</code> file <br>
     * will be updated.
     */
    private void jtask_settings() {
        settings.displaySettingsStatus();
        if (settings.edit()) {
            settings.update();
            System.out.println("Settings successfully updated and reloaded.");
        }
        System.out.println("Going back to menu...");
    }

    /**
     * <strong>Outputs error for wrong save file formatting.</strong><br>
     * Displays the expected line format.
     *
     * @param fileName file name that has wrong format.
     */
    private static void jtask_err_wrongFormat(String fileName, String line) {
        System.err.printf("jtask: error: file %s has wrong format.\n", fileName);
        System.err.printf("found: \n%s\n", line);
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
