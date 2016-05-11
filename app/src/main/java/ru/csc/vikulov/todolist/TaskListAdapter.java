package ru.csc.vikulov.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.csc.vikulov.todolist.contentprovider.FeedsTable;


public class TaskListAdapter extends CursorAdapter {

    private static final String TAG = "TaskListAdapter";

    private static final String TRUE = "TRUE";

    private static final String FALSE = "FALSE";

    public static final String CURSOR_ID = "cursor_id";

    private final LayoutInflater layoutInflater;

    public TaskListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        public CheckBox checkBox;
        public TextView textView;
        public TextView dateView;
        public CheckBox checkBoxStar;
        public LinearLayout textAll;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.task_item, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        holder.textView = (TextView) view.findViewById(R.id.textView);
        holder.dateView = (TextView) view.findViewById(R.id.textViewDate);
        holder.checkBoxStar = (CheckBox) view.findViewById(R.id.checkBoxStar);
        holder.textAll = (LinearLayout) view.findViewById(R.id.description);


        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final String title = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_TITLE));
        final String date = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_DATE));
        final String done = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_DONE));
        final String prior = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_PRIOR));
        final String _id = cursor.getString(cursor.getColumnIndex(FeedsTable._ID));

        final ViewHolder holder = (ViewHolder) view.getTag();

        if (holder != null) {
            holder.textView.setText(title);
            holder.dateView.setText(date);

            if (done.equals(TRUE)) {
                holder.checkBox.setChecked(true);
                holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.checkBox.setChecked(false);
                holder.textView.setPaintFlags(holder.textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (prior.equals(TRUE)) {
                holder.checkBoxStar.setChecked(true);
            } else {
                holder.checkBoxStar.setChecked(false);
            }

            holder.textAll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (holder.checkBox.isChecked()) {

                        new AlertDialog.Builder(context)
                                .setTitle(R.string.delete)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        context.getContentResolver().delete(
                                                Uri.withAppendedPath(MainActivity.ENTRIES_URI, _id),
                                                null,
                                                null);
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

                    } else {
                        final EditText taskDescription = new EditText(context);
                        taskDescription.setText(title);

                        new AlertDialog.Builder(context)
                                .setTitle(R.string.new_task)
                                .setView(taskDescription)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newTask = taskDescription.getText().toString();
                                        ContentValues values = new ContentValues();
                                        values.put(FeedsTable.COLUMN_TITLE, newTask);
                                        values.put(FeedsTable.COLUMN_DATE, date);
                                        values.put(FeedsTable.COLUMN_DONE, done);
                                        values.put(FeedsTable.COLUMN_PRIOR, prior);

                                        context.getContentResolver().update(
                                                Uri.withAppendedPath(MainActivity.ENTRIES_URI, _id),
                                                values,
                                                null,
                                                null);
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
                    }

                    return true;
                }
            });


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(FeedsTable.COLUMN_TITLE, title);
                    values.put(FeedsTable.COLUMN_DATE, date);
                    values.put(FeedsTable.COLUMN_PRIOR, prior);

                    if (holder.checkBox.isChecked()) {
                        values.put(FeedsTable.COLUMN_DONE, TRUE);
                    } else {
                        values.put(FeedsTable.COLUMN_DONE, FALSE);
                    }

                    context.getContentResolver().update(Uri.withAppendedPath(MainActivity.ENTRIES_URI, _id),
                            values, null, null);
                }
            });

            holder.checkBoxStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(FeedsTable.COLUMN_TITLE, title);
                    values.put(FeedsTable.COLUMN_DATE, date);
                    values.put(FeedsTable.COLUMN_DONE, done);

                    if (holder.checkBoxStar.isChecked()) {
                        values.put(FeedsTable.COLUMN_PRIOR, TRUE);
                    } else {
                        values.put(FeedsTable.COLUMN_PRIOR, FALSE);
                    }

                    context.getContentResolver().update(Uri.withAppendedPath(MainActivity.ENTRIES_URI, _id),
                            values, null, null);
                }
            });

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DescriptionActivity.class);
                    intent
                            .putExtra(CURSOR_ID, _id)
                    .putExtra(MainActivity.NEW_TASK, false);
                    context.startActivity(intent);
                }
            });
        }


    }
}
