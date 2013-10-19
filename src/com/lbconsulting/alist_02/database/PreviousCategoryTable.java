package com.lbconsulting.alist_02.database;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class PreviousCategoryTable {
	private static final String TAG = AListUtilities.TAG;

	// PreviousCategoryTable data table and constants
	public static final String TABLE_PREVIOUS_CATEGORY = "tblPreviousCategory";
	public static final String COL_ID = "_id";
	public static final String COL_LIST_TITLE_ID = "listTitleID";
	public static final String COL_MASTER_LIST_ITEM_ID = "masterListItemID";
	public static final String COL_CATEGORY_ID = "categoryID";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_TITLE_ID, COL_MASTER_LIST_ITEM_ID, COL_CATEGORY_ID };

	public static final String CONTENT_PATH = TABLE_PREVIOUS_CATEGORY;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_PREVIOUS_CATEGORY;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_PREVIOUS_CATEGORY;

	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	// Database creation SQL statements
	private static final String DATABASE_CREATE =
			"create table " + TABLE_PREVIOUS_CATEGORY
					+ " ("
					+ COL_ID + " integer primary key autoincrement, "
					+ COL_LIST_TITLE_ID + " integer not null references "
					+ ListTitlesTable.TABLE_LIST_TITLES + " (" + ListTitlesTable.COL_ID + "), "
					+ COL_MASTER_LIST_ITEM_ID + " integer not null references "
					+ MasterListItemsTable.TABLE_MASTER_LIST_ITEMS + " (" + MasterListItemsTable.COL_ID + "), "
					+ COL_CATEGORY_ID + " integer"
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_PREVIOUS_CATEGORY + ": Upgrading database from version " + oldVersion + " to version "
				+ newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PREVIOUS_CATEGORY);
		onCreate(database);
	}
}
