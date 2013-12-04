package com.lbconsulting.alist_02.database;

import android.content.ContentResolver;
import android.content.ContentValues;
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
	public static final String COL_ID = "_id";//0
	public static final String COL_LIST_TITLE_NAME = "listTitle";//1
	public static final String COL_TXT_LIST_ITEM = "txtListItem";//2

	public static final String COL_LIST_TYPE_ID = "listTypeID";//3
	public static final String COL_ACTIVE_CATEGORY_ID = "activeCategoryID";//4
	public static final String COL_SHOW_CATEGORIES = "showCategories";//5
	public static final String COL_LIST_ITEMS_SORT_ORDER = "listItemsSortOrder";//6

	public static final String COL_MASTERLISTVIEW_FIRST_VISIBLE_POSITION = "masterListViewFirstVisiblePosition";//7
	public static final String COL_MASTERLISTVIEW_TOP = "masterListViewTop";//8
	public static final String COL_MASTERLIST_SORT_ORDER = "masterListSortOrder";//9

	public static final String COL_BACKGROUND_COLOR = "backgroundColor";//10
	public static final String COL_NORMAL_TEXT_COLOR = "normalTextColor";//11
	public static final String COL_STRIKEOUT_TEXT_COLOR = "strikeoutTextColor";//12
	public static final String COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT = "autoAddCategoriesOnStrikeout";//13

	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_TITLE_NAME, COL_TXT_LIST_ITEM, COL_LIST_TYPE_ID,
			COL_ACTIVE_CATEGORY_ID, COL_SHOW_CATEGORIES, COL_LIST_ITEMS_SORT_ORDER,
			COL_MASTERLISTVIEW_FIRST_VISIBLE_POSITION, COL_MASTERLISTVIEW_TOP, COL_MASTERLIST_SORT_ORDER,
			COL_BACKGROUND_COLOR, COL_NORMAL_TEXT_COLOR, COL_STRIKEOUT_TEXT_COLOR,
			COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT };

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
			+ COL_TXT_LIST_ITEM + " text collate nocase, "

			+ COL_LIST_TYPE_ID + " integer not null references "
			+ ListTypesTable.TABLE_LIST_TYPES + " (" + ListTypesTable.COL_ID + ") default 1, "

			+ COL_ACTIVE_CATEGORY_ID + " integer not null references "
			+ CategoriesTable.TABLE_CATEGORIES + " (" + CategoriesTable.COL_ID + ") default 1, "

			+ COL_SHOW_CATEGORIES + " integer default 0, " // default false
			+ COL_LIST_ITEMS_SORT_ORDER + " integer default 0, " // default Alphabetically
			+ COL_MASTERLISTVIEW_FIRST_VISIBLE_POSITION + " integer default 0, " // default 
			+ COL_MASTERLISTVIEW_TOP + " integer default 0, " // default 
			+ COL_MASTERLIST_SORT_ORDER + " integer default 0, " // default Alphabetical

			+ COL_BACKGROUND_COLOR + " integer default 0, "
			+ COL_NORMAL_TEXT_COLOR + " integer default 0,"
			+ COL_STRIKEOUT_TEXT_COLOR + " integer default 0, "
			+ COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT + " integer default 1" // default true
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, "tblListTitles: Upgrading database from version " + oldVersion + " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_TITLES);
		onCreate(database);
	}

	public static Cursor getListTitlesCursor(Context context, long listTitleItemID) {
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
			Log.e(TAG, "An Exception error occurred in ListTitlesTable: getListTitlesCursor. ", e);
		}
		return cursor;
	}

	public static Cursor getListTitlesCursor(Context context) {

		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = SORT_ORDER_LIST_TITLE;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListTitlesTable: getListTitlesCursor. ", e);
		}
		return cursor;
	}

	/*	public static long getListTypeID(Context context, long listTitleItemID) {
			long listTypeID = -1;
			Cursor cursor = getListTitlesCursor(context, listTitleItemID);
			if (cursor != null && cursor.getCount() > 0) {
				listTypeID = cursor.getLong(cursor.getColumnIndex(MasterListItemsTable.COL_LIST_TYPE_ID));
			}
			if (cursor != null) {
				cursor.close();
			}
			return listTypeID;
		}*/

	/*	public static void setActiveCategoryID(Context context, long categoryID, long listID) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = ListTitlesTable.CONTENT_URI;

			ContentValues values = new ContentValues();
			values.put(COL_ACTIVE_CATEGORY_ID, categoryID);
			String selection = ListTitlesTable.COL_ID + " = ?";
			String[] selectionArgs = new String[] { String.valueOf(listID) };
			int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				Log.e(TAG, "More than one ListTitle records occurred in ListTitlesTable: setActiveCategoryID.");
			}
		}*/

	/*	public static long getActiveCategoryId(Context context, long listID) {
			long activeCategoryID = 1;
			Cursor listsCursor = getListTitlesCursor(context, listID);
			//ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(ListTitlesTable.CONTENT_URI, String.valueOf(listID));
			Cursor acitveListCursor = cr.query(uri, null, null, null, null);
			if (listsCursor != null) {
				activeCategoryID = listsCursor.getLong(listsCursor.getColumnIndexOrThrow(COL_ACTIVE_CATEGORY_ID));
			}
			if (listsCursor != null) {
				listsCursor.close();
			}
			return activeCategoryID;
		}*/

	public static void setIntItem(Context context, long listID, String ColumnName, int value) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = ListTitlesTable.CONTENT_URI;

		ContentValues values = new ContentValues();
		values.put(ColumnName, value);
		String selection = ListTitlesTable.COL_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(listID) };
		int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
		if (numberOfUpdatedRecords != 1) {
			Log.e(TAG, "More than one ListTitle records occurred in ListTitlesTable: setLongItem.");
		}
	}

	public static int getIntItem(Context context, long listID, String ColumnName) {
		int intResult = -1;
		Cursor listsCursor = getListTitlesCursor(context, listID);
		if (listsCursor != null) {
			int position = listsCursor.getColumnIndexOrThrow(ColumnName);

			switch (position) {
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
				intResult = listsCursor.getInt(position);
				break;

			default:
				break;
			}
			listsCursor.close();
		}
		return intResult;
	}

	public static void setLongItem(Context context, long listID, String ColumnName, long value) {
		if (ColumnName.equals(COL_ID)) {
			return;
		}
		ContentResolver cr = context.getContentResolver();
		Uri uri = ListTitlesTable.CONTENT_URI;

		ContentValues values = new ContentValues();
		values.put(ColumnName, value);
		String selection = ListTitlesTable.COL_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(listID) };
		int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
		if (numberOfUpdatedRecords != 1) {
			Log.e(TAG, "More than one ListTitle record occurred in ListTitlesTable: setLongItem.");
		}
	}

	public static long getLongItem(Context context, long listID, String ColumnName) {
		long longResult = -1;
		Cursor listsCursor = getListTitlesCursor(context, listID);
		if (listsCursor != null) {
			int position = listsCursor.getColumnIndexOrThrow(ColumnName);

			switch (position) {
			case 0:
				longResult = listsCursor.getLong(position);
				break;
			case 3:
			case 4:
				longResult = listsCursor.getLong(position);
				break;

			default:
				break;
			}
			listsCursor.close();
		}
		return longResult;
	}

	public static void setStringItem(Context context, long listID, String ColumnName, String value) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = ListTitlesTable.CONTENT_URI;

		ContentValues values = new ContentValues();
		values.put(ColumnName, value);
		String selection = ListTitlesTable.COL_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(listID) };
		int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
		if (numberOfUpdatedRecords != 1) {
			Log.e(TAG, "More than one ListTitle records occurred in ListTitlesTable: setStringItem.");
		}
	}

	public static String getStringItem(Context context, long listID, String ColumnName) {
		String stringResult = "";
		Cursor listsCursor = getListTitlesCursor(context, listID);
		if (listsCursor != null) {
			int position = listsCursor.getColumnIndexOrThrow(ColumnName);

			switch (position) {
			case 1:
			case 2:
				stringResult = listsCursor.getString(position);
				break;

			default:
				break;
			}
			listsCursor.close();
		}
		return stringResult;
	}

	public static void DeleteList(Context context, long listTitleID) {

		ListsTable.DeleteListTitleItems(context, listTitleID);
		PreviousCategoryTable.DeleteListTitleItems(context, listTitleID);
		MasterListItemsTable.ResetSelectedColumn(context);
		DeleteListTitleItem(context, listTitleID);
	}

	private static void DeleteListTitleItem(Context context, long listTitleID) {
		if (listTitleID > 0) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_ID + " = ?";
			String[] selectionArgs = new String[] { String.valueOf(listTitleID) };
			cr.delete(uri, where, selectionArgs);
		}
	}
}
