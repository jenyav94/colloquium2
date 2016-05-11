package ru.csc.vikulov.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.csc.vikulov.todolist.contentprovider.FeedsTable;
import ru.csc.vikulov.todolist.contentprovider.TagsTable;


public class TagListAdapter extends CursorAdapter {

    private static final String TAG = "TagListAdapter";

    private final LayoutInflater layoutInflater;

    public TagListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        public TextView textView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.task_item_2, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.textView = (TextView) view.findViewById(R.id.textView2);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final String title = cursor.getString(cursor.getColumnIndex(TagsTable.COLUMN_TASK));

        final ViewHolder holder = (ViewHolder) view.getTag();

        if (holder != null) {
            holder.textView.setText(title);
        }


    }
}
