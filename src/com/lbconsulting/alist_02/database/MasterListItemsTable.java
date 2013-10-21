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
	public static final String COL_ITEM_TYPE_ID = "itemTypeID";
	public static final String COL_SELECTED = "selected";
	public static final String COL_DATE_TIME_LAST_USED = "dateTimeLastUsed";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_ITEM_NAME, COL_ITEM_TYPE_ID, COL_SELECTED,
			COL_DATE_TIME_LAST_USED };

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
					+ COL_ITEM_TYPE_ID + " integer not null references "
					+ ListTypesTable.TABLE_LIST_TYPES + " (" + ListTypesTable.COL_ID + ") default 1, "
					+ COL_SELECTED + " integer default 0, "
					+ COL_DATE_TIME_LAST_USED + " integer"
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		Calendar rightNow = Calendar.getInstance();
		long currentDateTimeInMillis = rightNow.getTimeInMillis();

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Apples', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bacon', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Baked Beans', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Balsamic Vinegar', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bananas', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Beer', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Black Olives', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Blue Cheese', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Blueberries', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bread', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Broccoli', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buns', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buttermilk', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Carrots', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cereal', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cheese', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Chicken', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Chicken Broth', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cinnamon', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Corn', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cottage Cheese', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cream Cheese', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Distilled Water', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Eggs', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Garlic', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Ground Beef', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Hummus', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Kleenex', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Lemons', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Limes', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Manicotti Noodles', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Mustard', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Olive Oil', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Parmesan Cheese', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Peanut Butter', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pickles', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pineapple', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Potatoes', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Relish', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Satsumas', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Sour Cream', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'String Cheese', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Toilet Paper', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Tomatoes', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Toothpaste', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Tuna', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Vanilla', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Vinegar', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Wine', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Yogurt', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Avocado', 2, " + currentDateTimeInMillis + ")");

		// To Do List
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buy Groceries', 3, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pickup Laundry', 3, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Service The Car', 3, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_ITEM_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Prepare For Meeting', 3, " + currentDateTimeInMillis + ")");

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
