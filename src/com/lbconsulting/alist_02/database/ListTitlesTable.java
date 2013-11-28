package com.lbconsulting.alist_02.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class ListTitlesTable {
	private static final String TAG = AListUtilities.TAG;

	// MasterListItems data table
	public static final String TABLE_LIST_TITLES = "tblListTitles";
	public static final String COL_ID = "_id";
	public static final String COL_LIST_TITLE_NAME = "listTitle";
	public static final String COL_LIST_TYPE_ID = "listTypeID";
	public static final String COL_SORT_ORDER_ID = "sortOrderID";
	public static final String COL_BACKGROUND_COLOR = "backgroundColor";
	public static final String COL_NORMAL_TEXT_COLOR = "normalTextColor";
	public static final String COL_STRIKEOUT_TEXT_COLOR = "strikeoutTextColor";
	public static final String COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT = "autoAddCategoriesOnStrikeout";

	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_TITLE_NAME, COL_LIST_TYPE_ID, COL_SORT_ORDER_ID,
			COL_BACKGROUND_COLOR,
			COL_NORMAL_TEXT_COLOR, COL_STRIKEOUT_TEXT_COLOR, COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT };

	public static final String CONTENT_PATH = TABLE_LIST_TITLES;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LIST_TITLES;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LIST_TITLES;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_LIST_TITLE = COL_LIST_TITLE_NAME + " ASC";

	// Database creation SQL statements
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LIST_TITLES
			+ " ("
			+ COL_ID + " integer primary key autoincrement, "
			+ COL_LIST_TITLE_NAME + " text collate nocase, "
			+ COL_LIST_TYPE_ID + " integer not null references "
			+ ListTypesTable.TABLE_LIST_TYPES + " (" + ListTypesTable.COL_ID + ") default 1, "
			+ COL_SORT_ORDER_ID + " integer default 0, " // default alphabetical
			+ COL_BACKGROUND_COLOR + " integer default 0, "
			+ COL_NORMAL_TEXT_COLOR + " integer default 0,"
			+ COL_STRIKEOUT_TEXT_COLOR + " integer default 0, "
			+ COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT + " integer default 1" // default true
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		// TODO: Remove -- used for testing
		/*		ArrayList<String> sqlStatements = new ArrayList<String>();
				sqlStatements.add("insert into " + TABLE_LIST_TITLES + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_NAME + ", "
						+ COL_LIST_TYPE_ID + ", " // 2=Groceries Type Items
						+ COL_SORT_ORDER_ID + ", "
						+ COL_BACKGROUND_COLOR + ", "
						+ COL_NORMAL_TEXT_COLOR + ", "
						+ COL_STRIKEOUT_TEXT_COLOR + ", "
						+ COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT
						+ ") VALUES (NULL, 'Safeway',2, 0,'#00008B', '#FFFFFF', '#D3D3D3', 1)");

				sqlStatements.add("insert into " + TABLE_LIST_TITLES + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_NAME + ", "
						+ COL_LIST_TYPE_ID + ", " // 2=Groceries Type Items
						+ COL_SORT_ORDER_ID + ", "
						+ COL_BACKGROUND_COLOR + ", "
						+ COL_NORMAL_TEXT_COLOR + ", "
						+ COL_STRIKEOUT_TEXT_COLOR + ", "
						+ COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT
						+ ") VALUES (NULL, 'QFC',2, 1,'#006400', '#000000', '#123456', 1)");

				sqlStatements.add("insert into " + TABLE_LIST_TITLES + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_NAME + ", "
						+ COL_LIST_TYPE_ID + ", " // 3=ToDo Type Items
						+ COL_SORT_ORDER_ID + ", "
						+ COL_BACKGROUND_COLOR + ", "
						+ COL_NORMAL_TEXT_COLOR + ", "
						+ COL_STRIKEOUT_TEXT_COLOR + ", "
						+ COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT
						+ ") VALUES (NULL, 'To Do List',3, 2,'#E9967A', '#FFFFFF', '#D3D3D3', 0)");

				AListUtilities.execMultipleSQL(database, sqlStatements);*/

	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, "tblListTitles: Upgrading database from version " + oldVersion + " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_TITLES);
		onCreate(database);
	}

	public static Cursor getListTitleItemsCursor(Context context, long listTitleItemID) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listTitleItemID));
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String selectionArgs[] = null;
		String sortOrder = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListTitlesTable: getListTitleItemsCursor. ", e);
		}
		return cursor;
	}

	public static long getListTypeID(Context context, long listTitleItemID) {
		long listTypeID = -1;
		Cursor cursor = getListTitleItemsCursor(context, listTitleItemID);
		if (cursor != null && cursor.getCount() > 0) {
			listTypeID = cursor.getLong(cursor.getColumnIndex(MasterListItemsTable.COL_LIST_TYPE_ID));
		}
		if (cursor != null) {
			cursor.close();
		}
		return listTypeID;
	}
}
