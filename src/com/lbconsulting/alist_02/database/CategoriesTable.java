package com.lbconsulting.alist_02.database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class CategoriesTable {
	private static final String TAG = AListUtilities.TAG;

	// Categories data table
	public static final String TABLE_CATEGORIES = "tblCategories";
	public static final String COL_ID = "_id";
	public static final String COL_CATEGORY = "category";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_CATEGORY };

	public static final String CONTENT_PATH = TABLE_CATEGORIES;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_CATEGORIES;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_CATEGORIES;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_CATEGORY = COL_CATEGORY + " ASC";

	// Database creation SQL statements
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CATEGORIES
			+ " ("
			+ COL_ID + " integer primary key autoincrement, "
			+ COL_CATEGORY + " text collate nocase"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, '[None]')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Aisle 1')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Aisle 2')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Aisle 3')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Aisle 4')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Aisle 5')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Produce')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Dairy')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Meats')");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY
				+ ") VALUES (NULL, 'Bakery')");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_CATEGORIES + ": Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		onCreate(database);
	}
}
