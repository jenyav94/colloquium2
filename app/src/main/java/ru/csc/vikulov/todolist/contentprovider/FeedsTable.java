package ru.csc.vikulov.todolist.contentprovider;

import android.provider.BaseColumns;


public interface FeedsTable extends BaseColumns {
    String TABLE_NAME = "feeds";

    String COLUMN_TITLE = "title";
    String COLUMN_DESCRIPTION = "description";
    String COLUMN_DATE = "date";
    String COLUMN_DONE = "done";
    String COLUMN_PRIOR = "prior";
}
