package ru.csc.vikulov.todolist.model;


import android.content.ContentValues;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.csc.vikulov.todolist.contentprovider.FeedsTable;

public class Task {

    private String title;
    private String description;
    private String date;
    private String done;
    private String prior;
    private static final String FALSE = "FALSE";


    public Task(String title, String description, String date, String done, String prior) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.done = done;
        this.prior = prior;
    }

    public Task(String title, String description) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        this.title = title;
        this.description = description;
        this.date = format.format(new Date());
        this.done = FALSE;
        this.prior = FALSE;
    }

    public Task() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        this.title = "";
        this.description = "";
        this.date = format.format(new Date());
        this.done = FALSE;

        this.prior = FALSE;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getDone() {
        return done;
    }

    public String getPrior() {
        return prior;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public void setPrior(String prior) {
        this.prior = prior;
    }

    public ContentValues toCursor() {
        ContentValues values = new ContentValues();

        values.put(FeedsTable.COLUMN_TITLE, title);
        values.put(FeedsTable.COLUMN_DESCRIPTION, description);
        values.put(FeedsTable.COLUMN_DATE, date);
        values.put(FeedsTable.COLUMN_DONE, done);
        values.put(FeedsTable.COLUMN_PRIOR, prior);

        return values;
    }
}
