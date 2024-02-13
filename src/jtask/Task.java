/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jtask;

import java.time.LocalDate;

/**
 * Represents a task.
 *
 * @author nesx64
 */
public class Task {

    private int key;
    private final String label;
    private final LocalDate beginning;
    private LocalDate end;
    private final TaskType taskType;
    private final String description;
    private Boolean done;

    private static int taskSize = 0;

    /**
     * <strong>Main constructor of Task class.</strong><br>
     * Initialize a new instance of a task.
     *
     * @param aLabel name of the specified task
     * @param aBeginDate date of the beginning of the task
     * @param aType type of the task
     * @param aDesc description of the task
     * @param aStatus current status of the task
     */
    public Task(String aLabel, LocalDate aBeginDate, TaskType aType, String aDesc, Boolean aStatus) {
        key = taskSize;
        label = aLabel;
        beginning = aBeginDate;
        end = null;
        taskType = aType;
        description = aDesc;
        done = aStatus;
        taskSize++;
    }

    /**
     * <strong> Secondary constructor of Task class.</strong> <br>
     * Initialize a new instance of a task that is already done.
     *
     * @param aLabel name of the specified task
     * @param aBeginDate date of the beginning of the task
     * @param anEndDate date of the end of the task.
     * @param aType type of the task
     * @param aDesc description of the task
     * @param aStatus current status of the task
     */
    public Task(String aLabel, LocalDate aBeginDate, LocalDate anEndDate, TaskType aType, String aDesc, Boolean aStatus) {
        key = taskSize;
        label = aLabel;
        beginning = aBeginDate;
        end = anEndDate;
        taskType = aType;
        description = aDesc;
        done = aStatus;
        taskSize++;
    }

    /**
     * Return the task key (identifier).
     *
     * @return the task key
     */
    public int getKey() {
        return key;
    }

    /**
     * <strong>Sets the key with a new given value. </strong> <br>
     * Should only be used when shifting list.
     *
     * @param newValue new value for key
     */
    public void setKey(int newValue) {
        key = newValue;
    }

    /**
     * Return the task label.
     *
     * @return the task label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Return the date of the beginning of the task.
     *
     * @return the beginning date
     */
    public LocalDate getBeginning() {
        return beginning;
    }

    /**
     * Return the end date, if the task ended.
     *
     * @return the end date
     */
    public LocalDate getEnd() {
        if (done) {
            return end;
        }
        return null;
    }

    /**
     * Return the type of the task.
     *
     * @return the type of the task
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * Return the task descripion.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return the current status of the task.
     *
     * @return the status of the task
     */
    public Boolean isDone() {
        return done;
    }

    /**
     * Set the current task to done status.
     *
     * @param endDate date of the end of the task
     */
    public void isOver(LocalDate endDate) {
        done = true;
        end = endDate;
    }

    /**
     * <strong> Display task infos with formatted output. </strong> <br>
     * End date is ignored if null or task done.
     */
    public void display() {
        if (done && end != null) {
            System.out.printf("Label: %s; Begin: %s; End: %s; Type: %s; Description: %s; Done: %s\n\n",
                    getLabel(), getBeginning(), getEnd(), getTaskType(), getDescription(), isDone());
        } else {
            System.out.printf("Label: %s; Begin: %s; Type: %s; Description: %s; Done: %s\n\n",
                    getLabel(), getBeginning(), getTaskType(), getDescription(), isDone());
        }
    }
}
