package ru.csc.vikulov.todolist.model;


import android.content.ContentValues;

import ru.csc.vikulov.todolist.contentprovider.TagsTable;

public class SubTask {

    private String description;
    private String id;

    public SubTask(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public SubTask() {
        this.id = "";
        this.description = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentValues toCursor(){
        ContentValues values = new ContentValues();

        values.put(TagsTable.COLUMN_TASK_ID, id);
        values.put(TagsTable.COLUMN_TASK, description);

        return values;
    }
}
