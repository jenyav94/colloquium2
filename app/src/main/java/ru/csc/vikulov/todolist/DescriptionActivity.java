package ru.csc.vikulov.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.csc.vikulov.todolist.contentprovider.FeedsTable;
import ru.csc.vikulov.todolist.contentprovider.TagsTable;
import ru.csc.vikulov.todolist.contentprovider.ToDoListContentProvider;

public class DescriptionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String cursor_id;
    private EditText description;
    private EditText title;
    private ListView listView;
    private boolean newTask;
    private TagListAdapter cursorAdapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cursor_id = extras.getString(TaskListAdapter.CURSOR_ID);
            newTask = extras.getBoolean(MainActivity.NEW_TASK);
        }

        title = (EditText) findViewById(R.id.title_d);
        description = (EditText) findViewById(R.id.description_d);
        listView = (ListView) findViewById(R.id.listView_d);

        cursorAdapterListView = new TagListAdapter(this, null, 0);
        listView.setAdapter(cursorAdapterListView);

        if (!newTask) {
            getSupportLoaderManager().initLoader(1, null, this);
            getSupportLoaderManager().initLoader(2, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_add:
                final EditText taskDescription = new EditText(this);

                new AlertDialog.Builder(this)
                        .setTitle(R.string.new_task)
                        .setView(taskDescription)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newTag = taskDescription.getText().toString();
                                ContentValues values = new ContentValues();
                                values.put(TagsTable.COLUMN_TASK, newTag);
//                                if (!newTask){
//                                    values.put(TagsTable.COLUMN_TASK_ID, cursor_id);
//                                }
                                getContentResolver().insert(ToDoListContentProvider.TAGS_URI, values);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        ContentValues values = new ContentValues();

        String title_ = title.getText().toString();
        String description_ = description.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());

        values.put(FeedsTable.COLUMN_TITLE, title_);
        values.put(FeedsTable.COLUMN_DESCRIPTION, description_);
        values.put(FeedsTable.COLUMN_DATE, format.format(new Date()));
        values.put(FeedsTable.COLUMN_DONE, MainActivity.FALSE);
        values.put(FeedsTable.COLUMN_PRIOR, MainActivity.FALSE);

        if(newTask) {
            getContentResolver().insert(MainActivity.ENTRIES_URI, values);
        }
        else{
            getContentResolver().update(
                    Uri.withAppendedPath(MainActivity.ENTRIES_URI, cursor_id),
                    values,
                    null,
                    null);
        }
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 1:
                return new CursorLoader(this, Uri.withAppendedPath(MainActivity.ENTRIES_URI, cursor_id), null, null, null, null);
            case 2:
                return new CursorLoader(this, ToDoListContentProvider.TAGS_URI, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 1:

                while (data.moveToNext()) {
                    final String title_ = data.getString(data.getColumnIndex(FeedsTable.COLUMN_TITLE));
                    final String description_ = data.getString(data.getColumnIndex(FeedsTable.COLUMN_DESCRIPTION));

                    title.setText(title_);
                    description.setText(description_);
                }
                break;
            case 2:
                cursorAdapterListView.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case 1:
                break;
            case 2:
                cursorAdapterListView.swapCursor(null);
                break;
        }
    }
}
