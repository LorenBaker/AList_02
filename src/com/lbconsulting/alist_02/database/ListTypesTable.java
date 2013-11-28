package com.lbconsulting.alist_02.database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class ListTypesTable {

	private static final String TAG = AListUtilities.TAG;

	// ListTypes data table
	public static final String TABLE_LIST_TYPES = "tblListTypes";
	public static final String COL_ID = "_id";
	public static final String COL_LIST_TYPE = "listType";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_TYPE };

	public static final String CONTENT_PATH = TABLE_LIST_TYPES;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LIST_TYPES;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LIST_TYPES;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_LIST_TYPE = COL_LIST_TYPE + " ASC";

	// Database creation SQL statements
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LIST_TYPES
			+ " ("
			+ COL_ID + " integer primary key autoincrement, "
			+ COL_LIST_TYPE + " text collate nocase"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		ArrayList<String> sqlStatements = new ArrayList<String>();
		/*sqlStatements.add("insert into " + TABLE_LIST_TYPES + " (" + COL_ID + ", " + COL_LIST_TYPE
				+ ") VALUES (NULL, '[None]')");*/
		sqlStatements.add("insert into " + TABLE_LIST_TYPES + " (" + COL_ID + ", " + COL_LIST_TYPE
				+ ") VALUES (NULL, 'Grocery Items')");
		sqlStatements.add("insert into " + TABLE_LIST_TYPES + " (" + COL_ID + ", " + COL_LIST_TYPE
				+ ") VALUES (NULL, 'To Do Items')");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_LIST_TYPES + ": Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_TYPES);
		onCreate(database);
	}

	private static Cursor getListTypeItemsCursor(Context context, long listID) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String selectionArgs[] = null;
		String sortOrder = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListTypesTable: getListTypeItemsCursor. ", e);
		}
		return cursor;
	}

	public static String getListTypeName(Context context, long listTypeID) {
		String listTypeName = "";
		Cursor cursor = getListTypeItemsCursor(context, listTypeID);
		if (cursor != null && cursor.getCount() > 0) {
			listTypeName = cursor.getString(cursor.getColumnIndex(COL_LIST_TYPE));
		}
		if (cursor != null) {
			cursor.close();
		}
		return listTypeName;
	}

}
