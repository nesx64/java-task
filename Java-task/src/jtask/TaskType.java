/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package jtask;

/**
 *
 * @author nesx64
 */
public enum TaskType {
    CHORES, JOB, MEETING, STUDY, ENTERTAINMENT;
    
    public static boolean isValidTaskType(String input) {
        switch (input) {
            case "CHORES":
            case "JOB":
            case "MEETING":
            case "STUDY":
            case "ENTERTAINMENT":
                return true;
        }
        return false;
    }
    
    public static TaskType convertFileInputToTaskType(String input) {
        TaskType tt;
        switch (input) {
            case "CHORES" ->
                tt = CHORES;
            case "JOB" ->
                tt = JOB;
            case "MEETING" ->
                tt = MEETING;
            case "STUDY" ->
                tt = STUDY;
            case "ENTERTAINMENT" ->
                tt = ENTERTAINMENT;
            default ->
                tt = null;
        }
        return tt;
    }
}
