package com.lbconsulting.alist_02.database;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class ListsTable {
	private static final String TAG = AListUtilities.TAG;

	// MasterListItems data table
	public static final String TABLE_LISTS = "tblLists";
	public static final String COL_ID = "_id";
	public static final String COL_LIST_TITLE = "listTitle";
	public static final String COL_LIST_TYPE_ID = "listType";
	public static final String COL_SORT_ORDER_ID = "sortOrderID";
	public static final String COL_BACKGROUND_COLOR = "backgroundColor";
	public static final String COL_NORMAL_TEXT_COLOR = "normalTextColor";
	public static final String COL_STRIKEOUT_TEXT_COLOR = "strikeoutTextColor";
	public static final String COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT = "autoAddCategoriesOnStrikeout";

	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_TITLE, COL_LIST_TYPE_ID,
			COL_SORT_ORDER_ID, COL_BACKGROUND_COLOR, COL_NORMAL_TEXT_COLOR,
			COL_STRIKEOUT_TEXT_COLOR, COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT };

	public static final String CONTENT_PATH = TABLE_LISTS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_LISTS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_LISTS;
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_LIST_TITLE = COL_LIST_TITLE + " ASC";

	// Database creation SQL statements
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LISTS + " ("
			+ COL_ID + " integer primary key autoincrement, "
			+ COL_LIST_TITLE + " text collate nocase, "
			+ COL_LIST_TYPE_ID + " integer default 1, "
			+ COL_SORT_ORDER_ID + " integer default 0, "
			+ COL_BACKGROUND_COLOR + " text default '#0000FF', "
			+ COL_NORMAL_TEXT_COLOR + " text default '#FFFFFF', "
			+ COL_STRIKEOUT_TEXT_COLOR + " text default '#D3D3D3', "
			+ COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT + " integer default 1"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TAG, "tblListTitles: Upgrading database from version "
				+ oldVersion + " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
		onCreate(database);
	}
}
