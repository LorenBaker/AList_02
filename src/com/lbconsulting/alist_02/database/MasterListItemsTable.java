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
	public static final String COL_LIST_TYPE_ID = "listTypeID";
	public static final String COL_SELECTED = "selected";
	public static final String COL_DATE_TIME_LAST_USED = "dateTimeLastUsed";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_ITEM_NAME, COL_LIST_TYPE_ID, COL_SELECTED,
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
					+ COL_LIST_TYPE_ID + " integer not null references "
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
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Apples', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bacon', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Baked Beans', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Balsamic Vinegar', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bananas', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Beer', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Black Olives', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Blue Cheese', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Blueberries', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Bread', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Broccoli', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buns', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buttermilk', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Carrots', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cereal', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cheese', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Chicken', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Chicken Broth', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cinnamon', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Corn', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cottage Cheese', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Cream Cheese', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Distilled Water', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Eggs', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Garlic', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Ground Beef', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Hummus', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Kleenex', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Lemons', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Limes', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Manicotti Noodles', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Mustard', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Olive Oil', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Parmesan Cheese', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Peanut Butter', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pickles', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pineapple', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Potatoes', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Relish', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Satsumas', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Sour Cream', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'String Cheese', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Toilet Paper', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Tomatoes', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Toothpaste', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Tuna', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Vanilla', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Vinegar', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Wine', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Yogurt', 1, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Avocado', 1, " + currentDateTimeInMillis + ")");

		// To Do List
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Buy Groceries', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Pickup Laundry', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Service The Car', 2, " + currentDateTimeInMillis + ")");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID + ", " + COL_ITEM_NAME + ", "
				+ COL_LIST_TYPE_ID + ", " + COL_DATE_TIME_LAST_USED
				+ ") values (NULL, 'Prepare For Meeting', 2, " + currentDateTimeInMillis + ")");

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

	public static int SetSelectedColumn(Context context, long listTitleID) {
		int totalNumberOfRecordsUpdated = 0;
		try {

			Cursor allListTableItemsCursor = ListsTable.getAllItems(context, listTitleID);
			if (allListTableItemsCursor != null && allListTableItemsCursor.getCount() > 0) {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_ID + " = ?";
				ContentValues values = new ContentValues();
				values.put(COL_SELECTED, SELECTED_TRUE);

				long masterListItemID = -1;
				int colIndex = -1;
				int numberOfRecordsUpdated = 0;

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

			if (allListTableItemsCursor != null) {
				allListTableItemsCursor.close();
			}

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in SetSelectedColumn. ", e);
		}
		return totalNumberOfRecordsUpdated;
	}

	public static void ClearAllSelectedItems(Context context, long listTitleID) {
		ListsTable.DeleteListTitleItems(context, listTitleID);
		ResetSelectedColumn(context);
	}

	public static Cursor getMasterListItemsCursor(Context context, String itemName, long itemTypeID) {
		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = COL_ITEM_NAME + " = ? AND " + COL_LIST_TYPE_ID + " = ?";
		String selectionArgs[] = new String[] { itemName, String.valueOf(itemTypeID) };
		String sortOrder = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in MasterListItemsTable: getMasterListItemsCursor. ", e);
		}
		return cursor;
	}

	public static Cursor getMasterListItemsCursor(Context context, long masterListItemID) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(masterListItemID));
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String selectionArgs[] = null;
		String sortOrder = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in MasterListItemsTable: getMasterListItemsCursor. ", e);
		}
		return cursor;
	}

	private static Cursor getALLmasterListItemsCursor(Context context, long listTypeID) {
		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = COL_LIST_TYPE_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(listTypeID) };
		String sortOrder = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in MasterListItemsTable: getALLmasterListItemsCursor. ", e);
		}
		return cursor;
	}

	public static long getListTypeID(Context context, long masterListItemID) {
		long listTypeID = -1;
		Cursor cursor = getMasterListItemsCursor(context, masterListItemID);
		if (cursor != null && cursor.getCount() > 0) {
			listTypeID = cursor.getLong(cursor.getColumnIndex(MasterListItemsTable.COL_LIST_TYPE_ID));
		}
		if (cursor != null) {
			cursor.close();
		}
		return listTypeID;
	}

	public static long FindMasterListItemID(Context context, String itemName, long itemTypeID) {

		Cursor cursor = getMasterListItemsCursor(context, itemName, itemTypeID);

		if (cursor == null) {
			return -1;
		}

		if (cursor.getCount() > 0) {
			long newMasterListItemID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
			cursor.close();
			return newMasterListItemID;
		}
		else {
			// master list item not found
			cursor.close();
			return -1;
		}
	}

	public static void DeleteItem(Context context, long masterListItemID) {
		if (masterListItemID > 0) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(masterListItemID));
			String where = null;
			String[] selectionArgs = null;
			cr.delete(uri, where, selectionArgs);

			ListsTable.DeleteMasterListItems(context, masterListItemID);
			PreviousCategoryTable.DeleteMasterListItems(context, masterListItemID);
		}
	}

	public static void DeleteALLitems(Context context, long listTypeID) {
		if (listTypeID > 0) {
			Cursor cursor = getALLmasterListItemsCursor(context, listTypeID);
			if (cursor != null && cursor.getCount() > 0) {
				long masterListItemID = 0;
				do {
					masterListItemID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
					ListsTable.DeleteMasterListItems(context, masterListItemID);
					PreviousCategoryTable.DeleteMasterListItems(context, masterListItemID);
				} while (cursor.moveToNext());
				cursor.close();

				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_LIST_TYPE_ID + " = ?";
				String[] selectionArgs = new String[] { String.valueOf(listTypeID) };
				cr.delete(uri, where, selectionArgs);
			}
		}
	}

}
