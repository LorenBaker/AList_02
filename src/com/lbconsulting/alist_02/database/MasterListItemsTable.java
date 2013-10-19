package com.lbconsulting.alist_02.database;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class MasterListItemsTable {
	private static final String TAG = AListUtilities.TAG;

	// MasterListItems data table
	public static final String TABLE_MASTER_LIST_ITEMS = "tblMasterListItems";
	public static final String COL_ID = "_id";
	public static final String COL_ITEM_NAME = "itemName";
	public static final String COL_SELECTED = "selected";
	public static final String COL_DATE_TIME_LAST_USED = "dateTimeLastUsed";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_ITEM_NAME, COL_SELECTED, COL_DATE_TIME_LAST_USED };

	public static final String CONTENT_PATH = TABLE_MASTER_LIST_ITEMS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_MASTER_LIST_ITEMS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_MASTER_LIST_ITEMS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_ITEM_NAME = COL_ITEM_NAME + " ASC";
	public static final String SORT_ORDER_SELECTED_ASC = COL_SELECTED + " ASC, " + SORT_ORDER_ITEM_NAME;
	public static final String SORT_ORDER_SELECTED_DESC = COL_SELECTED + " DESC, " + SORT_ORDER_ITEM_NAME;

	public static final int SELECTED_TRUE = 1;
	public static final int SELECTED_FALSE = 0;

	public static final String WHERE_SELECTED = COL_SELECTED + "=" + SELECTED_TRUE;
	public static final String WHERE_NOT_SELECTED = COL_SELECTED + "=" + SELECTED_FALSE;

	// Database creation SQL statements
	private static final String DATABASE_CREATE =
			"create table " + TABLE_MASTER_LIST_ITEMS
					+ " ("
					+ COL_ID + " integer primary key autoincrement, "
					+ COL_ITEM_NAME + " text collate nocase, "
					+ COL_SELECTED + " integer DEFAULT 0, "
					+ COL_DATE_TIME_LAST_USED + " integer"
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		Calendar rightNow = Calendar.getInstance();
		long currentDateTimeInMillis = rightNow.getTimeInMillis();

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Apples', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bacon', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Baked Beans', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Balsamic Vinegar', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bananas', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Beer', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Black Olives', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Blue Cheese', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Blueberries', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bread', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Broccoli', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buns', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buttermilk', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Carrots', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cereal', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cheese', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Chicken', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Chicken Broth', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cinnamon', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Corn', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cottage Cheese', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cream Cheese', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Distilled Water', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Eggs', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Garlic', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Ground Beef', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Hummus', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Kleenex', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Lemons', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Limes', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Manicotti Noodles', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Mustard', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Olive Oil', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Parmesan Cheese', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Peanut Butter', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pickles', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pineapple', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Potatoes', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Relish', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Satsumas', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Sour Cream', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'String Cheese', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Toilet Paper', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Tomatoes', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Toothpaste', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Tuna', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Vanilla', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Vinegar', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Wine', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Yogurt', " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Avocado', " + currentDateTimeInMillis + ")");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_MASTER_LIST_ITEMS + ": Upgrading database from version " + oldVersion + " to version "
				+ newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_LIST_ITEMS);
		onCreate(database);
	}

	public static int ResetSelectedColumn(Context context) {
		int numberOfUpdatedRecords = 0;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			ContentValues values = new ContentValues();
			values.put(COL_SELECTED, SELECTED_FALSE);
			String where = COL_SELECTED + " = " + SELECTED_TRUE;
			numberOfUpdatedRecords = cr.update(uri, values, where, null);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ResetSelectedColumn. ", e);
		}
		return numberOfUpdatedRecords;
	}

	public static void SetSelectedColumn(Context context, long listTitleID) {
		try {
			@SuppressWarnings("resource")
			Cursor allListTableItemsCursor = ListsTable.getAllItems(context, listTitleID);
			if (allListTableItemsCursor != null && allListTableItemsCursor.getCount() > 0) {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_ID + " = ?";
				ContentValues values = new ContentValues();
				values.put(COL_SELECTED, SELECTED_TRUE);

				long masterListItemID = -1;
				int colIndex = -1;
				int numberOfRecordsUpdated = -1;
				int totalNumberOfRecordsUpdated = 0;

				do {
					colIndex = allListTableItemsCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_ITEM_ID);
					masterListItemID = allListTableItemsCursor.getLong(colIndex);
					String[] whereArgs = { String.valueOf(masterListItemID) };
					numberOfRecordsUpdated = cr.update(uri, values, where, whereArgs);
					if (numberOfRecordsUpdated != 1) {
						Log.e(TAG, "SetSelectedColumn: MasterListItemID = " + masterListItemID
								+ "; incorrect number of MasterListItemsTable records included in ListTable with ID = "
								+ listTitleID);
						totalNumberOfRecordsUpdated = totalNumberOfRecordsUpdated + numberOfRecordsUpdated;
					} else {
						totalNumberOfRecordsUpdated++;
					}

				} while (allListTableItemsCursor.moveToNext());

				if (allListTableItemsCursor.getCount() != totalNumberOfRecordsUpdated) {
					Log.e(TAG, "SetSelectedColumn: Incorrect number of total records set as selected in the ");
				}
			}

			AListUtilities.closeQuietly(allListTableItemsCursor);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in SetSelectedColumn. ", e);
		}
	}

	public static void ClearAllSelectedItems(Context context, long listTitleID) {
		ListsTable.DeleteAllItems(context, listTitleID);
		ResetSelectedColumn(context);
	}

}
