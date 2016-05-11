package ru.csc.vikulov.todolist.contentprovider;


import android.provider.BaseColumns;

public interface TagsTable extends BaseColumns {
    String TABLE_NAME = "tags";

    String COLUMN_TASK = "task";
    String COLUMN_TASK_ID = "task_id";
}
