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

public class ListsTable {
	private static final String TAG = AListUtilities.TAG;

	// Lists data table
	public static final String TABLE_LISTS = "tblLists";
	public static final String COL_ID = "_id";
	public static final String COL_LIST_TITLE_ID = "listTitleID";
	public static final String COL_MASTER_LIST_ITEM_ID = "masterListItemID";
	public static final String COL_STRUCK_OUT = "struckOut";
	public static final String COL_MANUAL_SORT_ORDER = "manualSortOrder";
	public static final String COL_CATEGORY_ID = "categoryID";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_LIST_TITLE_ID, COL_MASTER_LIST_ITEM_ID, COL_STRUCK_OUT,
			COL_MANUAL_SORT_ORDER, COL_CATEGORY_ID };

	public static final String CONTENT_PATH = TABLE_LISTS;
	public static final String CONTENT_LIST_WITH_CATEGORY = "listWithCategory";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LISTS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LISTS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);
	public static final Uri LIST_WITH_CATEGORY_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/"
			+ CONTENT_LIST_WITH_CATEGORY);

	// Database creation SQL statements
	private static final String DATABASE_CREATE =
			"create table " + TABLE_LISTS
					+ " ("
					+ COL_ID + " integer primary key autoincrement, "
					+ COL_LIST_TITLE_ID + " integer not null references "
					+ ListTitlesTable.TABLE_LIST_TITLES + " (" + ListTitlesTable.COL_ID + "), "
					+ COL_MASTER_LIST_ITEM_ID + " integer not null references "
					+ MasterListItemsTable.TABLE_MASTER_LIST_ITEMS + " (" + MasterListItemsTable.COL_ID + "), "
					+ COL_CATEGORY_ID + " integer not null references " + CategoriesTable.TABLE_CATEGORIES
					+ " (" + CategoriesTable.COL_ID + ") default 1, "
					+ COL_STRUCK_OUT + " integer default 0, "
					+ COL_MANUAL_SORT_ORDER + " integer default -1 "
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		//TODO: For test purposes
		/*		ArrayList<String> sqlStatements = new ArrayList<String>();
				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 1,1,1,1,1)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 2,23,2,1,2)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 3,26,3,0,3)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 1,18,4,0,4)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 2,55,5,1,5)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 3,52,6,0,6)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 1,40,7,0,7)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 2,54,8,0,8)");

				sqlStatements.add("insert into " + TABLE_LISTS + " ("
						+ COL_ID + ", "
						+ COL_LIST_TITLE_ID + ", "
						+ COL_MASTER_LIST_ITEM_ID + ", "
						+ COL_CATEGORY_ID + ", "
						+ COL_STRUCK_OUT + ", "
						+ COL_MANUAL_SORT_ORDER
						+ ") VALUES (NULL, 3,55,9,1,9)");

				AListUtilities.execMultipleSQL(database, sqlStatements);*/
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_LISTS + ": Upgrading database from version " + oldVersion + " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
		onCreate(database);
	}

	public static Cursor getListTitlesCurosr(Context context, long listTitleID) {
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(ListTitlesTable.CONTENT_URI, String.valueOf(listTitleID));
			String[] projection = ListTitlesTable.PROJECTION_ALL;
			cursor = cr.query(uri, projection, null, null, null);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: getListTitleItem. ", e);
		}
		return cursor;
	}

	public static Cursor getListTitlesCurosr(Context context, long listTitleID, long masterListItemID) {
		Cursor cursor = null;

		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_TITLE_ID + "= ?" + " AND " + COL_MASTER_LIST_ITEM_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listTitleID), String.valueOf(masterListItemID) };
			cursor = cr.query(uri, projection, where, selectionArgs, null);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: getListTitlesCurosr.", e);
		}
		return cursor;
	}

	public static long FindListID(Context context, long listTitleID, long masterListItemID) {
		Cursor cursor = getListTitlesCurosr(context, listTitleID, masterListItemID);
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

	@SuppressWarnings("resource")
	public static String getListTitle(Context context, long listTitleID) {
		String listTitle = null;
		Cursor listTitlesCurosr = null;
		try {
			listTitlesCurosr = getListTitlesCurosr(context, listTitleID);
			if (listTitlesCurosr != null && listTitlesCurosr.getCount() > 0) {
				listTitle = listTitlesCurosr.getString(listTitlesCurosr
						.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TITLE_NAME));
			}

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: getListTitle.", e);
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
			Log.e(TAG, "An Exception error occurred in ListsTable: FindNextListID. ", e);
			return -1;
		}

		if (listTitlesCursor != null && listTitlesCursor.getCount() > 0) {
			long id = -1;
			boolean foundID = false;
			do {
				id = listTitlesCursor.getLong(listTitlesCursor.getColumnIndexOrThrow(ListTitlesTable.COL_ID));
				if (id == listTitleID) {
					foundID = true;
					break;
				}
			} while (listTitlesCursor.moveToNext());

			if (foundID) {
				if (listTitlesCursor.moveToNext()) {
					nextListTitleID = listTitlesCursor.getLong(listTitlesCursor
							.getColumnIndexOrThrow(ListTitlesTable.COL_ID));

				} else if (listTitlesCursor.moveToPrevious() && listTitlesCursor.moveToPrevious()) {

					nextListTitleID = listTitlesCursor.getLong(listTitlesCursor
							.getColumnIndexOrThrow(ListTitlesTable.COL_ID));
				}
			}
		}

		AListUtilities.closeQuietly(listTitlesCursor);
		return nextListTitleID;

	}

	public static int DeleteListTitleItems(Context context, long listTitleID) {
		int numberOfDeletedRecords = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_TITLE_ID + "= ?";
			String[] whereArgs = { String.valueOf(listTitleID) };
			numberOfDeletedRecords = cr.delete(uri, where, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: DeleteAllItems. ", e);
		}
		return numberOfDeletedRecords;
	}

	/*	public static int DeleteAllPreviousCategoryItems(Context context, long listTitleID) {
			int numberOfDeletedRecords = 0;
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = PreviousCategoryTable.CONTENT_URI;
				String where = PreviousCategoryTable.COL_LIST_TITLE_ID + "= ?";
				String[] whereArgs = { String.valueOf(listTitleID) };
				numberOfDeletedRecords = cr.delete(uri, where, whereArgs);
			} catch (Exception e) {
				Log.e(TAG, "An Exception error occurred in ListsTable: DeleteAllPreviousCategoryItems. ", e);
			}
			return numberOfDeletedRecords;
		}*/

	/*	public static void DeleteListTitleItem(Context context, long listTitleID) {
			ListTitlesTable.
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = Uri.withAppendedPath(ListTitlesTable.CONTENT_URI, String.valueOf(listTitleID));
				cr.delete(uri, null, null);
			} catch (Exception e) {
				Log.e(TAG, "An Exception error occurred in ListsTable: DeleteListTitle. ", e);
			}
		}*/

	public static Cursor getAllItems(Context context, long listTitleID) {
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_TITLE_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listTitleID) };
			cursor = cr.query(uri, projection, where, selectionArgs, null);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: getAllItems.", e);
		}
		return cursor;
	}

	public static int RemoveAllItems(Context context, long listID) {
		int numberOfRowsDeleted = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_TITLE_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listID) };
			numberOfRowsDeleted = cr.delete(uri, where, selectionArgs);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: RemoveItem.", e);
		}
		return numberOfRowsDeleted;
	}

	public static int RemoveItem(Context context, long listID, long masterListItemID) {
		int numberOfRowsDeleted = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_TITLE_ID + "= ?" + " AND " + COL_MASTER_LIST_ITEM_ID + "= ?";
			String[] selectionArgs = { String.valueOf(listID), String.valueOf(masterListItemID) };
			numberOfRowsDeleted = cr.delete(uri, where, selectionArgs);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: RemoveItem.", e);
		}
		return numberOfRowsDeleted;
	}

	public static void setStrikeOut(Context context, long listTitleID, long masterListItemID, boolean value) {
		ContentResolver cr = context.getContentResolver();
		int strikeOut = AListUtilities.boolToInt(value);
		Uri uri = CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(COL_STRUCK_OUT, strikeOut);
		String selection = COL_LIST_TITLE_ID + " = ? AND " + COL_MASTER_LIST_ITEM_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(listTitleID), String.valueOf(masterListItemID) };
		int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
		if (numberOfUpdatedRecords != 1) {
			Log.e(TAG, "More than one List records updated in ListsTable: setStrikeOut.");
		}
	}

	public static boolean getStrikeOut(Context context, long listTitleID, long masterListItemID) {
		boolean strikeOutValue = false;
		Cursor cursor = getListsCursor(context, listTitleID, masterListItemID);
		if (cursor != null) {
			int strikeOut = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STRUCK_OUT));
			strikeOutValue = AListUtilities.intToBoolean(strikeOut);
			cursor.close();
		}
		return strikeOutValue;
	}

	public static Cursor getListsCursor(Context context, long listTitleID, long masterListItemID) {
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String where = COL_LIST_TITLE_ID + " = ? AND " + COL_MASTER_LIST_ITEM_ID + " = ?";
			String[] selectionArgs = new String[] { String.valueOf(listTitleID), String.valueOf(masterListItemID) };
			cursor = cr.query(uri, projection, where, selectionArgs, null);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ListsTable: getListsCursor.", e);
		}
		return cursor;
	}

	public static void setCategoryID(Context context, long listTitleID, long masterListItemID, long categoryID) {

		ContentResolver cr = context.getContentResolver();
		Uri uri = CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(COL_CATEGORY_ID, categoryID);
		String selection = COL_LIST_TITLE_ID + " = ? AND " + COL_MASTER_LIST_ITEM_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(listTitleID), String.valueOf(masterListItemID) };
		int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
		if (numberOfUpdatedRecords != 1) {
			Log.e(TAG, "More than one List records updated in ListsTable: setCategoryID.");
		}
		PreviousCategoryTable.setPreviousCategoryID(context, listTitleID, masterListItemID, categoryID);
	}

	public static void DeleteMasterListItems(Context context, long masterListItemID) {
		if (masterListItemID > 0) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_MASTER_LIST_ITEM_ID + " = ?";
			String[] selectionArgs = new String[] { String.valueOf(masterListItemID) };
			cr.delete(uri, where, selectionArgs);
		}
	}

}
