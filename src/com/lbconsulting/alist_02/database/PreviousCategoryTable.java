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
		/*ArrayList<String> sqlStatements = new ArrayList<String>();
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

		AListUtilities.execMultipleSQL(database, sqlStatements);*/
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

	public static long getPreviousCategoryID(Context context, long listTitleID, long masterListItemID) {
		Cursor cursor = getPreviousCategoryCurosr(context, listTitleID, masterListItemID);
		if (cursor == null) {
			return -1;
		}
		if (cursor.getCount() > 0) {
			long categoryID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CATEGORY_ID));
			cursor.close();
			return categoryID;
		}
		else {
			cursor.close();
			// return -1 so that the [None] category will not be added to the database
			return -1;
		}
	}

	public static void setPreviousCategoryID(Context context, long listTitleID, long masterListItemID, long categoryID) {
		Cursor cursor = getPreviousCategoryCurosr(context, listTitleID, masterListItemID);
		if (cursor != null) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			ContentValues values = new ContentValues();
			values.put(COL_CATEGORY_ID, categoryID);

			String selection = COL_LIST_TITLE_ID + " = ? AND " + COL_MASTER_LIST_ITEM_ID + " = ?";
			String[] selectionArgs = new String[] { String.valueOf(listTitleID), String.valueOf(masterListItemID) };

			if (cursor.getCount() > 0) {
				// a categoryID for this listTitleID-masterListItemID pair is already in the PreviousCategoryTable.
				// so update it if categoryID does not equal 1.
				// if categoryID equals 1, then delete the record from the PreviousCategoryTable
				if (categoryID != 1) {
					int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
					if (numberOfUpdatedRecords != 1) {
						Log.e(TAG, "More than one CategoryID records updated in PreviousCategoryTable: " +
								"setPreviousCategoryID.");
					}
				} else {
					// categoryID equals 1 ... delete the record from the PreviousCategoryTable
					int numberOfDeleteddRecords = cr.delete(uri, selection, selectionArgs);
					if (numberOfDeleteddRecords != 1) {
						Log.e(TAG, "More than one CategoryID records deleted from PreviousCategoryTable: " +
								"setPreviousCategoryID.");
					}
				}

			} else {
				// a categoryID for this listTitleID-masterListItemID pair is NOT in the PreviousCategoryTable.
				// so create it if categoryID does not equal 1 [None]
				if (categoryID != 1) {
					values.put(COL_LIST_TITLE_ID, listTitleID);
					values.put(COL_MASTER_LIST_ITEM_ID, masterListItemID);
					cr.insert(uri, values);
				}
			}
			cursor.close();
		}
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

	public static void DeleteListTitleItems(Context context, long listTitleID) {
		if (listTitleID > 0) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_TITLE_ID + " = ?";
			String[] selectionArgs = new String[] { String.valueOf(listTitleID) };
			cr.delete(uri, where, selectionArgs);
		}
	}
}
