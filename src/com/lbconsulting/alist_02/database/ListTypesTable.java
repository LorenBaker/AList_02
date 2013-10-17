package com.lbconsulting.alist_02.database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class ListTypesTable {
	private static final String TAG = AListUtilities.TAG;

	// MasterListItems data table
	public static final String TABLE_LIST_TYPES = "tblListTypes";
	public static final String COL_ID = "_id";
	public static final String COL_LIST_TYPE = "category";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_TYPE };

	public static final String CONTENT_PATH = TABLE_LIST_TYPES;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LIST_TYPES;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LIST_TYPES;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_LIST_TYPE = COL_LIST_TYPE + " ASC";

	// Database creation SQL statements
	private static final String DATABASE_CREATE =
			"create table " + TABLE_LIST_TYPES
					+ " ("
					+ COL_ID + " integer primary key autoincrement, "
					+ COL_LIST_TYPE + " text collate nocase"
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_LIST_TYPES + " (" + COL_ID + ", " + COL_LIST_TYPE
				+ ") VALUES (NULL, '[None]')");
		sqlStatements.add("insert into " + TABLE_LIST_TYPES + " (" + COL_ID + ", " + COL_LIST_TYPE
				+ ") VALUES (NULL, 'Groceries')");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_LIST_TYPES + ": Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_TYPES);
		onCreate(database);
	}
}
