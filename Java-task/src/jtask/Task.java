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

    private final String label;
    private final LocalDate beginning;
    private LocalDate end;
    private final TaskType taskType;
    private final String description;
    private Boolean done;

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
        label = aLabel;
        beginning = aBeginDate;
        taskType = aType;
        description = aDesc;
        done = aStatus;
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
        label = aLabel;
        beginning = aBeginDate;
        end = anEndDate;
        taskType = aType;
        description = aDesc;
        done = aStatus;
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
    public Boolean getStatus() {
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
}
