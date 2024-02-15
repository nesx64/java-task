package jtask;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a task.
 *
 * @author nesx64
 */
public class Task {

    private int key;
    private String label;
    private final LocalDate beginning;
    private LocalDate end;
    private final TaskType taskType;
    private String description;
    private Boolean done;

    private static int taskSize = 0;

    /**
     * <strong>Main constructor of Task class.</strong><br>
     * Initialize a new instance of a task.
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
     * Return the task label.
     *
     * @return the task label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the task label to the new given label.
     *
     * @param newLabel new label for the task.
     */
    public void setLabel(String newLabel) {
        label = newLabel;
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
     * Return the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the task description to the new given description.
     *
     * @param newDesc new description for the task.
     */
    public void setDesc(String newDesc) {
        description = newDesc;
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
    public void setToDone(LocalDate endDate) {
        done = true;
        end = endDate;
    }

    /**
     * Retrieves a Task inside a list of Tasks using its key.<br>
     * Returns null if not found.
     *
     * @param lst list of tasks where to search
     * @param key key of the element searched
     * @return the task if found
     */
    public static Task searchByKey(ArrayList<Task> lst, int key) {
        if (!lst.isEmpty()) {
            for (Task t : lst) {
                if (t.getKey() == key) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * Shift all tasks keys to the left inside the given list of tasks.
     * 
     * @param lst list of tasks to be shifted
     * @param start index where the shift will start
     */
    public static void shiftLeftTaskListKeys(ArrayList<Task> lst, int start) {
        int i =0;
        if (!lst.isEmpty()) {
            for (Task t : lst) {
                if (i > start) {
                    t.key--;
                }
                i++;
            }
        }
        taskSize--;
    }

    /**
     * <strong> Display task infos with formatted output. </strong> <br>
     * End date is ignored if null or task done.
     *
     * @param showKey if true, will also output task key.
     */
    public void display(boolean showKey) {
        if (showKey) {
            System.out.printf("Key: %d ", getKey());
        }
        if (done && end != null) {
            System.out.printf("Label: %s\n > Begin: %s; End: %s\n > Type: %s; Description: %s\n > Done: %s\n\n ---- \n\n",
                    getLabel(), getBeginning(), getEnd(), getTaskType(), getDescription(), isDone());
        } else {
            System.out.printf("Label: %s\n > Begin: %s\n > Type: %s; Description: %s\n > Done: %s\n\n ---- \n\n",
                    getLabel(), getBeginning(), getTaskType(), getDescription(), isDone());
        }
    }
}
