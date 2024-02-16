package jtask;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;

import org.json.*;

/**
 * @author nesx64
 */
public class Jtask {

    final ArrayList<Task> loadedList;
    final ArrayList<Task> addedTasks;
    String saveFilePath;
    final Jsettings settings;
    final static int EXIT_PROGRAM = 8;
    final static String VERSION = "v0.4";
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
    private void run() {
        Scanner sc = new Scanner(System.in);
        int userInput = 0;
        while (userInput != EXIT_PROGRAM) {
            System.out.println(Utils.JTASK_MENU);
            userInput = sc.nextInt();
            inputCheck(userInput);
        }
    }

    /**
     * <strong> Initialize jtask by loading up all data.</strong> <br>
     * Will create the specified file if not found. Save file is restricted to
     * be JSON only.
     */
    private void init() {
        if (!settings.autoloadEnabled()) {
            System.out.println("If you already have an existing file with data, please type the file path, else the file will be created:");
            saveFilePath = retrieveJsonFile();
            try {
                File data = new File(saveFilePath);
                if (data.createNewFile()) {
                    System.out.println("%s file was created. It will be the default save file.");
                    settings.setSavePath(saveFilePath);
                    System.out.println("You can enable autoload is settings to skip this step next time.");
                    System.out.println("jtask is setting up...");
                } else {
                    loadJson(data);
                }
            } catch (IOException e) {
                System.err.println("jtask: no file name provided. Try again.");
            }
        } else {
            try {
                loadJson(new File(settings.getSavePath()));
            } catch (FileNotFoundException ex) {
                System.err.printf("jtask: loading data error: save file was not found.\n");
                System.err.printf("as autoload is enabled, it will now be disabled.\n\n");
                System.err.printf("next, you'll have to specify a save file.\n\n");
                System.err.flush();
                settings.setAutoload(Boolean.FALSE);
                init();
            }
        }
    }

    /**
     * <strong> Retrieve the json save file path for init. </strong>
     * Only used when autoload is disabled.
     *
     * @return the retrieved json file path (as a String)
     */
    private String retrieveJsonFile() {
        String jsonFile;
        boolean fileIsJson;
        do {
            Scanner sc = new Scanner(System.in);
            jsonFile = sc.nextLine();
            fileIsJson = jsonFile.substring(jsonFile.length() - 3).equals("json");
        } while (!fileIsJson);
        return jsonFile;
    }

    /**
     * <strong>Exit jtask program.</strong> <br>
     * Save in the save file all new added tasks, only if autosave is enabled.
     */
    private void exit() {
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

    private void clearAddedQueue() {
        loadedList.addAll(addedTasks);
        addedTasks.clear();
    }

    /**
     * <strong>Save jtask tasks to save file.</strong>
     */
    private void save() {
        clearAddedQueue();
        try (Writer writer = new PrintWriter(saveFilePath)) {
            JSONArray js = new JSONArray();
            fillJsonArray(js);
            writer.write(js.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("jtask: successfully saved.\n\n");
    }

    private void fillJsonArray(JSONArray array) {
        for (Task t : loadedList) {
            JSONObject obj = new JSONObject();
            obj.put("label", t.getLabel());
            obj.put("beginDate", t.getBeginning().toString());
            if (t.getEnd() == null) {
                obj.put("endDate", "");
            } else {
                obj.put("endDate", t.getEnd());
            }
            obj.put("taskType", t.getTaskType().toString());
            obj.put("description", t.getDescription());
            obj.put("done", t.isDone().toString());
            array.put(obj);
        }
    }

    /**
     * <strong> Loading sequence of jtask init. </strong> <br> Will load save
     * file if it exists.
     */
    private void loadJson(File filePath) throws FileNotFoundException {
        System.out.println("Loading...");
        try (Reader reader = new FileReader(filePath)) {
            JSONTokener token = new JSONTokener(reader);
            JSONArray saveJson = new JSONArray(token);
            for (int i = 0; i < saveJson.length(); i++) {
                JSONObject current = saveJson.getJSONObject(i);
                LocalDate begin = Utils.convertFileInputToDate(current.getString("beginDate"));
                LocalDate end = Utils.convertFileInputToDate(current.getString("endDate"));
                TaskType ttype = TaskType.convertFileInputToTaskType(current.getString("taskType"));
                loadedList.add(new Task(current.getString("label"), begin, end, ttype, current.getString("description"), current.getBoolean("done")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
