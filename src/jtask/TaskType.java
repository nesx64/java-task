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

    /**
     * <strong> Checks if the input is a valid TaskType enum
     * element.</strong><br>
     *
     * @param input user input
     * @return true iff input is a TaskType
     */
    public static boolean isValidTaskType(String input) {
        switch (input) {
            case "CHORES", "JOB", "MEETING", "STUDY", "ENTERTAINMENT" -> {
                return true;
            }
        }
        return false;
    }

    /**
     * <strong> Return the corresponding TaskType from String input.
     * </strong><br>
     *
     * @param input user input
     * @return the corresponding TaskType from input
     */
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
