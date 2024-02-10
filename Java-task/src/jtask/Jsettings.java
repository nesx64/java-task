/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jtask;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author lolo
 */
public class Jsettings {
    
    private Boolean autosave;
    private Boolean autoload;
    private String savePath;
    private File saveFile;
    static final String SETTINGS_FILE = "./settings.txt";
    
    
    Jsettings() {
        //Scanner sc = new Scanner(SETTINGS_FILE);
        autosave = true;
        autoload = true;
        savePath = "./jtask-data.txt";
        saveFile = new File(savePath);
    }
    
    public Boolean autosaveEnabled() {
        return autosave;
    }
    
    public void setAutosave(Boolean newValue) {
        autosave = newValue;
    } 
    
    public Boolean autoloadEnabled() {
        return autoload;
    }
    
    public void setAutoload(Boolean newValue) {
        autoload = newValue;
    }
    
    public String getSavePath(){
        return savePath;
    }
    
    public File getSaveFile(){
        return saveFile;
    }
}
