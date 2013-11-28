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
	public static final String COL_CATEGORY_NAME = "categoryName";
	public static final String COL_LIST_TYPE_ID = "listTypeID";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_CATEGORY_NAME, COL_LIST_TYPE_ID };

	public static final String CONTENT_PATH = TABLE_CATEGORIES;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_CATEGORIES;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_CATEGORIES;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_CATEGORY = COL_CATEGORY_NAME + " ASC";

	// Database creation SQL statements
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CATEGORIES
			+ " ("
			+ COL_ID + " integer primary key autoincrement, "
			+ COL_CATEGORY_NAME + " text collate nocase,"
			+ COL_LIST_TYPE_ID + " integer not null references "
			+ ListTypesTable.TABLE_LIST_TYPES + " (" + ListTypesTable.COL_ID + ") default 1 "
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, '[None]', -1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Aisle 1', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Aisle 2', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Aisle 3', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Aisle 4', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Aisle 5', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Produce', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Dairy', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Meats', 1)");
		sqlStatements.add("insert into " + TABLE_CATEGORIES + " (" + COL_ID + ", " + COL_CATEGORY_NAME
				+ ", " + COL_LIST_TYPE_ID
				+ ") VALUES (NULL, 'Bakery', 1)");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_CATEGORIES + ": Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		onCreate(database);
	}
}
