package com.lbconsulting.alist_02.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class List_Item_Table {
	private static final String TAG = AListUtilities.TAG;

	public static final String TABLE_LIST_ITEM = "tblListItems";
	public static final String COL_ID = "_id";
	public static final String COL_LIST_ID = "listID";
	public static final String COL_ITEM_ID = "itemID";
	public static final String COL_STRUCK_OUT = "struckOut";
	public static final String COL_MANUAL_SORT_ORDER = "manualSortOrder";
	public static final String COL_CATEGORY_ID = "categoryID";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_ID, COL_ITEM_ID,
			COL_STRUCK_OUT, COL_MANUAL_SORT_ORDER, COL_CATEGORY_ID };

	public static final String CONTENT_PATH = TABLE_LIST_ITEM;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_LIST_ITEM;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_LIST_ITEM;
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	// Database creation SQL statements
	private static final String DATABASE_CREATE =
			"create table " + TABLE_LIST_ITEM + " ("
					+ COL_ID + " integer primary key autoincrement, "
					+ COL_LIST_ID + " integer not null references " + ListsTable.TABLE_LISTS
					+ "(" + ListsTable.COL_ID + "),"
					+ COL_ITEM_ID + " integer not null references " + MasterListItemsTable.TABLE_MASTER_LIST_ITEMS
					+ "(" + MasterListItemsTable.COL_ID + "),"
					+ COL_CATEGORY_ID + " integer not null references " + CategoriesTable.TABLE_CATEGORIES
					+ "(" + CategoriesTable.COL_ID + ") default 1,"
					+ COL_STRUCK_OUT + " integer default 0,"
					+ COL_MANUAL_SORT_ORDER + " integer default -1"
					+ ")";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TAG, TABLE_LIST_ITEM
				+ ": Upgrading database from version " + oldVersion
				+ " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ITEM);
		onCreate(database);
	}

	public static Cursor getListsCurosr(Context context, long listID) {
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(ListsTable.CONTENT_URI,
					String.valueOf(listID));
			String[] projection = ListsTable.PROJECTION_ALL;
			cursor = cr.query(uri, projection, null, null, null);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in getListTitleItem. ", e);
		}
		return cursor;
	}

	public static Cursor getListTitlesCurosr(Context context, long listID,
			long masterListItemID) {
		Cursor cursor = null;

		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_ID + "= ?" + " AND " + COL_ITEM_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listID),
					String.valueOf(masterListItemID) };
			cursor = cr.query(uri, projection, where, selectionArgs, null);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in getListTitlesCurosr.", e);
		}
		return cursor;
	}

	@SuppressWarnings("resource")
	public static String getListTitle(Context context, long listID) {
		String listTitle = null;
		Cursor listTitlesCurosr = null;
		try {
			listTitlesCurosr = getListsCurosr(context, listID);
			if (listTitlesCurosr != null && listTitlesCurosr.getCount() > 0) {
				listTitle = listTitlesCurosr.getString(listTitlesCurosr
						.getColumnIndexOrThrow(ListsTable.COL_LIST_TITLE));
			}

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in getListTitle.", e);
		}
		AListUtilities.closeQuietly(listTitlesCurosr);
		return listTitle;
	}

	@SuppressWarnings("resource")
	public static long FindNextListID(Context context, long listID) {
		Cursor listsCursor = null;
		long nextlistID = -1;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = ListsTable.CONTENT_URI;
			String[] projection = ListsTable.PROJECTION_ALL;
			String orderBy = ListsTable.SORT_ORDER_LIST_TITLE;
			listsCursor = cr.query(uri, projection, null, null, orderBy);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in FindNextListID. ", e);
			return -1;
		}

		if (listsCursor != null && listsCursor.getCount() > 0) {
			long id = -1;
			boolean foundID = false;
			do {
				id = listsCursor.getLong(listsCursor.getColumnIndexOrThrow(ListsTable.COL_ID));
				if (id == listID) {
					foundID = true;
					break;
				}
			} while (listsCursor.moveToNext());

			if (foundID) {
				if (listsCursor.moveToNext()) {
					nextlistID = listsCursor.getLong(listsCursor.getColumnIndexOrThrow(ListsTable.COL_ID));

				} else if (listsCursor.moveToPrevious() && listsCursor.moveToPrevious()) {
					nextlistID = listsCursor.getLong(listsCursor.getColumnIndexOrThrow(ListsTable.COL_ID));
				}
			}
		}

		AListUtilities.closeQuietly(listsCursor);
		return nextlistID;

	}

	public static int DeleteAllItems(Context context, long listID) {
		int numberOfDeletedRecords = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + "= ?";
			String[] whereArgs = { String.valueOf(listID) };
			numberOfDeletedRecords = cr.delete(uri, where, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in DeleteAllItems. ", e);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllPreviousCategoryItems(Context context, long listID) {
		int numberOfDeletedRecords = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = PreviousCategoryTable.CONTENT_URI;
			String where = PreviousCategoryTable.COL_LIST_ID + "= ?";
			String[] whereArgs = { String.valueOf(listID) };
			numberOfDeletedRecords = cr.delete(uri, where, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in DeleteAllPreviousCategoryItems. ", e);
		}
		return numberOfDeletedRecords;
	}

	public static void DeleteList(Context context, long listID) {
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(ListsTable.CONTENT_URI, String.valueOf(listID));
			cr.delete(uri, null, null);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in DeleteListTitle. ", e);
		}
	}

	public static Cursor getAllItems(Context context, long listID) {
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_ID + " = ?";
			String[] selectionArgs = { String.valueOf(listID) };
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
