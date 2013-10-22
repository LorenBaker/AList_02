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

		//TODO: For testing
		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_PREVIOUS_CATEGORY + " ("
				+ COL_ID + ", "
				+ COL_LIST_TITLE_ID + ", "
				+ COL_MASTER_LIST_ITEM_ID + ", "
				+ COL_CATEGORY_ID
				+ ") VALUES (NULL, 2,23,2)");

		sqlStatements.add("insert into " + TABLE_PREVIOUS_CATEGORY + " ("
				+ COL_ID + ", "
				+ COL_LIST_TITLE_ID + ", "
				+ COL_MASTER_LIST_ITEM_ID + ", "
				+ COL_CATEGORY_ID
				+ ") VALUES (NULL, 3,26,3)");

		sqlStatements.add("insert into " + TABLE_PREVIOUS_CATEGORY + " ("
				+ COL_ID + ", "
				+ COL_LIST_TITLE_ID + ", "
				+ COL_MASTER_LIST_ITEM_ID + ", "
				+ COL_CATEGORY_ID
				+ ") VALUES (NULL, 1,18,4)");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_PREVIOUS_CATEGORY + ": Upgrading database from version " + oldVersion + " to version "
				+ newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PREVIOUS_CATEGORY);
		onCreate(database);
	}

	public static Cursor getPreviousCategoryCurosr(Context context, long listTitleID, long masterListItemID) {
		Cursor cursor = null;

		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_TITLE_ID + "= ?" + " AND " + COL_MASTER_LIST_ITEM_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listTitleID), String.valueOf(masterListItemID) };
			cursor = cr.query(uri, projection, where, selectionArgs, null);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in PreviousCategoryTable: getPreviousCategoryCurosr.", e);
		}
		return cursor;
	}

	public static long FindPreviousCategoryID(Context context, long listTitleID, long masterListItemID) {
		Cursor cursor = getPreviousCategoryCurosr(context, listTitleID, masterListItemID);
		if (cursor == null) {
			return -1;
		}
		if (cursor.getCount() > 0) {
			long listItemID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
			cursor.close();
			return listItemID;
		}
		else {
			cursor.close();
			return -1;
		}
	}
}
