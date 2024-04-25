package jtask;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import error.ERR_ORIGIN;
import error.ERR_TYPE;
import error.JtaskException;

/**
 *
 * @author nesx64
 */
public class Jsettings {

    private Boolean autosave;
    private Boolean autoload;
    private String savePath;
    static final String SETTINGS_FILE = "settings.ini";
    static final String DEFAULT_SETTINGS = "./def/settings.ini";

    /**
     * <strong> Main constructor for Jsettings class. </strong> <br>
     * Instances a new Jsettings object for Jtask.
     */
    Jsettings() throws JtaskException {
        loadSettingsProtocol();
    }

    /**
     * <strong> Initialize the loading of all settings needed for Jtask.
     * </strong> <br>
     * Check if an existing settings file exists, if not, it will create a copy
     * of default settings from default directory.
     */
    private void loadSettingsProtocol() throws JtaskException {
        try {
            File setFile = new File(SETTINGS_FILE);
            if (!setFile.isFile()) {
                Files.copy(Paths.get(DEFAULT_SETTINGS), Paths.get("."), StandardCopyOption.REPLACE_EXISTING);
            }
            loadSettings();
        } catch (IOException e) {
            System.err.printf(
                    "jtask: unexpected error occurred in loading current settings. check def directory or reinstall jtask.\n");
            System.exit(Jtask.ERROR_EXIT_CODE);
        }
    }

    /**
     * <strong> Load all settings from settings file. </strong> <br>
     * settings.ini file may exist before trying to load.
     * 
     * @throws JtaskException
     */
    private void loadSettings() throws FileNotFoundException, JtaskException {
        Scanner sc = new Scanner(new File(SETTINGS_FILE));
        while (sc.hasNextLine()) {
            String currentLine = sc.nextLine();
            if (!currentLine.isEmpty() && !(currentLine.charAt(0) == '#')) {
                String key = currentLine.split("=")[1];
                switch (currentLine.split("=")[0]) {
                    case "autosave" ->
                        autosave = Utils.convertInputToBoolean(key);
                    case "autoload" ->
                        autoload = Utils.convertInputToBoolean(key);
                    case "save_file" ->
                        savePath = key;
                    default -> 
                        throw new JtaskException(ERR_TYPE.INVALID_FORMAT, ERR_ORIGIN.SETTINGS, 12);
                }
            }
        }
        if (autosave == null || autoload == null | savePath.isEmpty()) {
            throw new JtaskException(ERR_TYPE.CORRUPTED, ERR_ORIGIN.SETTINGS, 11);
        }
    }

    /**
     * <strong> Display each settings current status. </strong> <br>
     * One setting cannot be null.
     */
    public void displaySettingsStatus() {
        System.out.println("-- Settings --");
        System.out.println("Currently:");
        System.out.printf("autosave: %s\n", autosaveEnabled());
        System.out.printf("autoload: %s\n", autoloadEnabled());
        System.out.printf("save file path: %s\n\n", getSavePath());
    }

    /**
     * <strong> Main method to check if the user wants to edit current
     * settings.</strong> <br>
     * Boolean return value stands for modified settings status.
     *
     * @return true iff any setting was modified
     */
    public Boolean edit() {
        boolean modified = false;
        System.out.printf("Would you like to edit settings? (y)es (n)o\n");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        if (answer.equals("y")) {
            editProtocol();
            modified = true;
        }
        return modified;
    }

    /**
     * <strong> Main protocol to edit settings.</strong> <br>
     * Only called when user wants to edit, else it shouldn't be used.
     */
    private void editProtocol() {
        System.out.printf("You can edit:\n");
        System.out.printf("(1) autosave (2) autoload (3) save file path (4) back to menu\n");
        Scanner sc = new Scanner(System.in);
        boolean edited = false;
        int answer = sc.nextInt();
        while (answer != 4 && !edited) {
            switch (answer) {
                case 1 ->
                    setAutosave(editAutosave());
                case 2 ->
                    setAutoload(editAutoload());
                case 3 ->
                    setSavePath(editSavePath());
            }
            edited = true;
        }
    }

    /**
     * <strong>Retrieve the new value for autosave setting.</strong> <br>
     * If new value typed is neither true neither false, it will ask again.
     *
     * @return the new value for autosave setting
     */
    private Boolean editAutosave() {
        Boolean newValue;
        if (autosave) {
            System.out.printf("autosave is enabled\n");
            System.out.println("To keep enabled, type true. To disable, type false.");
        } else {
            System.out.printf("autosave is disabled\n");
            System.out.println("To keep disabled, type false. To enable, type true.");
        }
        do {
            newValue = Utils.convertInputToBoolean(new Scanner(System.in).nextLine());
            assert newValue != null;
            if (!newValue.equals(Boolean.TRUE) && !newValue.equals(Boolean.FALSE)) {
                System.err.printf("jtask: input mismatch. expected true or false\n");
                System.err.flush();
            }
        } while (!newValue.equals(Boolean.TRUE) && !newValue.equals(Boolean.FALSE));
        return newValue;
    }

    /**
     * <strong>Retrieve the new value for autoload setting.</strong> <br>
     * If new value typed is neither true neither false, it will ask again.
     *
     * @return the new value for autoload setting
     */
    private Boolean editAutoload() {
        Boolean newValue;
        if (autoload) {
            System.out.printf("autoload is enabled\n");
            System.out.println("To keep enabled, type true. To disable, type false.");
        } else {
            System.out.printf("autoload is disabled\n");
            System.out.println("To keep disabled, type false. To enable, type true.");
        }

        newValue = Utils.convertInputToBoolean(new Scanner(System.in).nextLine());
        return newValue;
    }

    /**
     * <strong>Retrieve the new value for savePath setting.</strong> <br>
     * If new path typed points to a non-existing file, it will ask again.
     *
     * @return the new path for savePath setting
     */
    private String editSavePath() {
        String newPath;
        System.out.printf("current save file path: %s\n", savePath);

        do {
            System.out.println("Please type the new save file path:");
            newPath = new Scanner(System.in).nextLine();
            if (!new File(newPath).isFile()) {
                System.err.printf("jtask: given path\n\n\t%s\n\nis not a file or doesn't exist.\n\n", newPath);
                System.err.flush();
            }
        } while (!new File(newPath).isFile());
        return newPath;
    }

    /**
     * <strong> Return the autosave setting state. </strong> <br>
     * If <code>true</code>, autosave is enabled. Else it's not.
     *
     * @return autosave state
     */
    public Boolean autosaveEnabled() {
        return autosave;
    }

    /**
     * <strong> Sets the autosave state to a new value. </strong> <br>
     * Only accepts <code>Boolean</code>.
     *
     * @param newValue new autosave state
     */
    private void setAutosave(Boolean newValue) {
        autosave = newValue;
    }

    /**
     * <strong> Return the autoload setting state. </strong> <br>
     * If<code>true</code>, autoload is enabled. Else it's not.
     *
     * @return autoload state
     */
    public Boolean autoloadEnabled() {
        return autoload;
    }

    /**
     * <strong> Sets the autoload state to a new value. </strong> <br>
     * Only accepts <code>Boolean</code>.
     *
     * @param newValue new autoload state
     */
    public void setAutoload(Boolean newValue) {
        autoload = newValue;
    }

    /**
     * <strong> Return the current save file path. </strong> <br>
     * <code>String</code> format only.
     *
     * @return current save file path
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * <strong> Sets the save file path to new specified path. </strong> <br>
     * Only accepts <code>String</code>.
     *
     * @param newPath new save file path
     */
    public void setSavePath(String newPath) {
        savePath = newPath;
    }

    /**
     * <strong> Update the <code>settings.ini</code> file to save new
     * settings.</strong> <br>
     * Will check if settings file exists, if not, Jtask will exit.
     */
    public void update() throws JtaskException {
        try {
            if (new File(SETTINGS_FILE).isFile()) {
                PrintWriter pw = new PrintWriter(new FileWriter(SETTINGS_FILE, false), true);
                pw.printf("%s", Utils.JTASK_SETTINGS_HEAD);
                pw.printf("\n#will save automatically to the specified save file.\n");
                pw.printf("autosave=%b\n\n", autosave);
                pw.printf("#will load automatically on start the specified save file if it exists\n");
                pw.printf("autoload=%b\n\n", autoload);
                pw.printf("#the save file where jtask will try to load any already existing data\n");
                pw.printf("save_file=%s", savePath);
                pw.close();
                loadSettings();
            }
        } catch (IOException ex) {
            System.err.println("jtask: settings.ini file not found.");
            System.err.flush();
            System.exit(Jtask.ERROR_EXIT_CODE);
        }
    }
}
