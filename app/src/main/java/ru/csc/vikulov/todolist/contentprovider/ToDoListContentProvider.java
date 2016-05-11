package ru.csc.vikulov.todolist.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.design.BuildConfig;
import android.text.TextUtils;
import android.util.Log;

public class ToDoListContentProvider extends ContentProvider {

    private final String LOG_TAG = "myLogs";

    private static final String CONTENT_PATH = "entries";
    private static final String TAGS_PATH = "tags";

    public static final String AUTHORITY = "ru.csc.vikulov.todolist.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri ENTRIES_URI = Uri.withAppendedPath(ToDoListContentProvider.CONTENT_URI, CONTENT_PATH);
    public static final Uri TAGS_URI = Uri.withAppendedPath(ToDoListContentProvider.CONTENT_URI, TAGS_PATH);

    public static final int ENTRIES = 1;
    public static final int ENTRIES_ID = 2;
    public static final int TAGS = 3;
    public static final int TAGS_ID = 4;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, ENTRIES);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", ENTRIES_ID);
        uriMatcher.addURI(AUTHORITY, TAGS_PATH, TAGS);
        uriMatcher.addURI(AUTHORITY, TAGS_PATH + "/#", TAGS_ID);
    }

    private DatabaseHelper databaseHelper;


    public ToDoListContentProvider() {
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "delete, " + uri.toString());
        }
        switch (uriMatcher.match(uri)) {
            case ENTRIES:
                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "URI_CONTACTS");
                }
                break;
            case ENTRIES_ID:
                String id = uri.getLastPathSegment();

                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                }

                if (TextUtils.isEmpty(selection)) {
                    selection = FeedsTable._ID + " = " + id;
                } else {
                    selection = selection + " AND " + FeedsTable._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int cnt = db.delete(FeedsTable.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return cnt;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "insert, " + uri.toString());
        }
        Uri resultUri = null;
        long rowID;

        switch (uriMatcher.match(uri)){
            case ENTRIES:
                rowID = databaseHelper.getWritableDatabase().insert(FeedsTable.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowID);

                break;
            case TAGS:
                rowID = databaseHelper.getWritableDatabase().insert(TagsTable.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowID);

                break;
        }

//        if (uriMatcher.match(uri) != ENTRIES)
//            throw new IllegalArgumentException("Wrong URI: " + uri);

//        long rowID = databaseHelper.getWritableDatabase().insert(FeedsTable.TABLE_NAME, null, values);
//        Uri resultUri = ContentUris.withAppendedId(uri, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);

        return resultUri;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "query, " + uri.toString());
        }

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case ENTRIES:

                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "ENTRIES");
                }

                builder.setTables(FeedsTable.TABLE_NAME);

//                if (TextUtils.isEmpty(sortOrder)) {
//                    sortOrder = FeedsTable.COLUMN_DONE + " , "
//                            + FeedsTable.COLUMN_DATE + " ASC , "
//                            + FeedsTable.COLUMN_PRIOR + " DESC";
//                }

                break;
            case ENTRIES_ID:
                String id = uri.getLastPathSegment();
                builder.setTables(FeedsTable.TABLE_NAME);

                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                }

                if (TextUtils.isEmpty(selection)) {
                    selection = FeedsTable._ID + " = " + id;
                } else {
                    selection = selection + " AND " + FeedsTable._ID + " = " + id;
                }
                break;
            case TAGS:
                builder.setTables(TagsTable.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "update, " + uri.toString());
        }

        switch (uriMatcher.match(uri)) {
            case ENTRIES:
                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "URI_CONTACTS");
                }
                break;
            case ENTRIES_ID:
                String id = uri.getLastPathSegment();

                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                }

                if (TextUtils.isEmpty(selection)) {
                    selection = FeedsTable._ID + " = " + id;
                } else {
                    selection = selection + " AND " + FeedsTable._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int cnt = db.update(FeedsTable.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return cnt;
    }
}
