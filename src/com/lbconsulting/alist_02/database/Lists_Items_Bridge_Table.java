package com.lbconsulting.alist_02.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class Lists_Items_Bridge_Table {
	private static final String TAG = AListUtilities.TAG;

	// Lists data table
	public static final String TABLE_LISTS_ITEMS_BRIDGE = "tblListsItemsBridge";
	// public static final String COL_ID = "_id";
	public static final String COL_LIST_ID = "listID";
	public static final String COL_ITEM_ID = "ItemID";
	public static final String COL_STRUCK_OUT = "struckOut";
	public static final String COL_MANUAL_SORT_ORDER = "manualSortOrder";
	public static final String COL_CATEGORY_ID = "categoryID";
	public static final String[] PROJECTION_ALL = { COL_LIST_ID, COL_ITEM_ID,
			COL_STRUCK_OUT, COL_MANUAL_SORT_ORDER, COL_CATEGORY_ID };

	public static final String CONTENT_PATH = TABLE_LISTS_ITEMS_BRIDGE;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_LISTS_ITEMS_BRIDGE;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_LISTS_ITEMS_BRIDGE;
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	// Database creation SQL statements
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LISTS_ITEMS_BRIDGE + " (" + COL_LIST_ID
			+ " integer not null references "
			+ ListTitlesTable.TABLE_LIST_TITLES + "(" + ListTitlesTable.COL_ID
			+ "), " + COL_ITEM_ID + " integer not null references "
			+ MasterListItemsTable.TABLE_MASTER_LIST_ITEMS + "("
			+ MasterListItemsTable.COL_ID + "), " + COL_CATEGORY_ID
			+ " integer DEFAULT 1, " + COL_STRUCK_OUT + " integer DEFAULT 0, "
			+ COL_MANUAL_SORT_ORDER + " integer DEFAULT -1. " + "primary key ("
			+ COL_LIST_ID + ", " + COL_ITEM_ID + "), " + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TAG, TABLE_LISTS_ITEMS_BRIDGE
				+ ": Upgrading database from version " + oldVersion
				+ " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS_ITEMS_BRIDGE);
		onCreate(database);
	}

	public static Cursor getListTitlesCurosr(Context context, long listTitleID) {
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(ListTitlesTable.CONTENT_URI,
					String.valueOf(listTitleID));
			String[] projection = ListTitlesTable.PROJECTION_ALL;
			cursor = cr.query(uri, projection, null, null, null);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in getListTitleItem. ", e);
		}
		return cursor;
	}

	public static Cursor getListTitlesCurosr(Context context, long listTitleID,
			long masterListItemID) {
		Cursor cursor = null;

		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_ID + "= ?" + " AND " + COL_ITEM_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listTitleID),
					String.valueOf(masterListItemID) };
			cursor = cr.query(uri, projection, where, selectionArgs, null);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in getListTitlesCurosr.", e);
		}
		return cursor;
	}

	@SuppressWarnings("resource")
	public static String getListTitle(Context context, long listTitleID) {
		String listTitle = null;
		Cursor listTitlesCurosr = null;
		try {
			listTitlesCurosr = getListTitlesCurosr(context, listTitleID);
			if (listTitlesCurosr != null && listTitlesCurosr.getCount() > 0) {
				listTitle = listTitlesCurosr.getString(listTitlesCurosr
						.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TITLE));
			}

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in getListTitle.", e);
		}
		AListUtilities.closeQuietly(listTitlesCurosr);
		return listTitle;
	}

	@SuppressWarnings("resource")
	public static long FindNextListID(Context context, long listTitleID) {
		Cursor listTitlesCursor = null;
		long nextListTitleID = -1;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = ListTitlesTable.CONTENT_URI;
			String[] projection = ListTitlesTable.PROJECTION_ALL;
			String orderBy = ListTitlesTable.SORT_ORDER_LIST_TITLE;
			listTitlesCursor = cr.query(uri, projection, null, null, orderBy);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in FindNextListID. ", e);
			return -1;
		}

		if (listTitlesCursor != null && listTitlesCursor.getCount() > 0) {
			long id = -1;
			boolean foundID = false;
			do {
				id = listTitlesCursor.getLong(listTitlesCursor
						.getColumnIndexOrThrow(ListTitlesTable.COL_ID));
				if (id == listTitleID) {
					foundID = true;
					break;
				}
			} while (listTitlesCursor.moveToNext());

			if (foundID) {
				if (listTitlesCursor.moveToNext()) {
					nextListTitleID = listTitlesCursor.getLong(listTitlesCursor
							.getColumnIndexOrThrow(ListTitlesTable.COL_ID));

				} else if (listTitlesCursor.moveToPrevious()
						&& listTitlesCursor.moveToPrevious()) {

					nextListTitleID = listTitlesCursor.getLong(listTitlesCursor
							.getColumnIndexOrThrow(ListTitlesTable.COL_ID));
				}
			}
		}

		AListUtilities.closeQuietly(listTitlesCursor);
		return nextListTitleID;

	}

	public static int DeleteAllItems(Context context, long listTitleID) {
		int numberOfDeletedRecords = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + "= ?";
			String[] whereArgs = { String.valueOf(listTitleID) };
			numberOfDeletedRecords = cr.delete(uri, where, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in DeleteAllItems. ", e);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllPreviousCategoryItems(Context context,
			long listTitleID) {
		int numberOfDeletedRecords = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = PreviousCategoryTable.CONTENT_URI;
			String where = PreviousCategoryTable.COL_LIST_ID + "= ?";
			String[] whereArgs = { String.valueOf(listTitleID) };
			numberOfDeletedRecords = cr.delete(uri, where, whereArgs);
		} catch (Exception e) {
			Log.e(TAG,
					"An Exception error occurred in DeleteAllPreviousCategoryItems. ",
					e);
		}
		return numberOfDeletedRecords;
	}

	public static void DeleteListTitleItem(Context context, long listTitleID) {
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(ListTitlesTable.CONTENT_URI,
					String.valueOf(listTitleID));
			cr.delete(uri, null, null);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in DeleteListTitle. ", e);
		}
	}

	public static Cursor getAllItems(Context context, long listTitleID) {
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listTitleID) };
			cursor = cr.query(uri, projection, where, selectionArgs, null);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in getAllItems.", e);
		}
		return cursor;
	}

	public static int RemoveItem(Context context, long listID,
			long masterListItemID) {
		int numberOfRowsDeleted = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + "= ?" + " AND " + COL_ITEM_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listID),
					String.valueOf(masterListItemID) };
			numberOfRowsDeleted = cr.delete(uri, where, selectionArgs);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in RemoveItem.", e);
		}
		return numberOfRowsDeleted;
	}

}
