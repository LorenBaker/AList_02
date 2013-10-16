package com.lbconsulting.alist_02.database;

import java.util.ArrayList;

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
	public static final String[] PROJECTION_ALL = { COL_ID, COL_ITEM_NAME,
			COL_SELECTED };

	public static final String CONTENT_PATH = TABLE_MASTER_LIST_ITEMS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_MASTER_LIST_ITEMS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + "vnd.lbconsulting." + TABLE_MASTER_LIST_ITEMS;
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_ITEM_NAME = COL_ITEM_NAME + " ASC";
	public static final String SORT_ORDER_SELECTED_ASC = COL_SELECTED
			+ " ASC, " + SORT_ORDER_ITEM_NAME;
	public static final String SORT_ORDER_SELECTED_DESC = COL_SELECTED
			+ " DESC, " + SORT_ORDER_ITEM_NAME;

	public static final int SELECTED_TRUE = 1;
	public static final int SELECTED_FALSE = 0;

	public static final String WHERE_SELECTED = COL_SELECTED + "="
			+ SELECTED_TRUE;
	public static final String WHERE_NOT_SELECTED = COL_SELECTED + "="
			+ SELECTED_FALSE;

	// Database creation SQL statements
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_MASTER_LIST_ITEMS + " (" + COL_ID
			+ " integer primary key autoincrement, " + COL_ITEM_NAME
			+ " text collate nocase, " + COL_SELECTED + " integer DEFAULT 0"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Apples')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Bacon')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Baked Beans')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Balsamic Vinegar')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Bananas')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Beer')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Black Olives')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Blue Cheese')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Blueberries')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Bread')");
		sqlStatements
				.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID
						+ ", " + COL_ITEM_NAME + ") values (NULL, 'Broccoli')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Buns')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Buttermilk')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Carrots')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Cereal')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Cheese')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Chicken')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Chicken Broth')");
		sqlStatements
				.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID
						+ ", " + COL_ITEM_NAME + ") values (NULL, 'Cinnamon')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Corn')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Cottage Cheese')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Cream Cheese')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Distilled Water')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Eggs')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Garlic')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Ground Beef')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Hummus')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Kleenex')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Lemons')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Limes')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Manicotti Noodles')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Mustard')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Olive Oil')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Parmesan Cheese')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Peanut Butter')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Pickles')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Pineapple')");
		sqlStatements
				.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID
						+ ", " + COL_ITEM_NAME + ") values (NULL, 'Potatoes')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Relish')");
		sqlStatements
				.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID
						+ ", " + COL_ITEM_NAME + ") values (NULL, 'Satsumas')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Sour Cream')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'String Cheese')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Toilet Paper')");
		sqlStatements
				.add("insert into " + TABLE_MASTER_LIST_ITEMS + "(" + COL_ID
						+ ", " + COL_ITEM_NAME + ") values (NULL, 'Tomatoes')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME
				+ ") values (NULL, 'Toothpaste')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Tuna')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Vanilla')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Vinegar')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Wine')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Yogurt')");
		sqlStatements.add("insert into " + TABLE_MASTER_LIST_ITEMS + "("
				+ COL_ID + ", " + COL_ITEM_NAME + ") values (NULL, 'Avocado')");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TAG, TABLE_MASTER_LIST_ITEMS
				+ ": Upgrading database from version " + oldVersion
				+ " to version " + newVersion + ".");
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
			Log.e(TAG, "An Exception error occurred in ResetSelectedColumn. ",
					e);
		}
		return numberOfUpdatedRecords;
	}

	public static void SetSelectedColumn(Context context, long listTitleID) {
		try {
			@SuppressWarnings("resource")
			Cursor allListTableItemsCursor = Lists_Items_Bridge_Table
					.getAllItems(context, listTitleID);
			if (allListTableItemsCursor != null
					&& allListTableItemsCursor.getCount() > 0) {
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
					colIndex = allListTableItemsCursor
							.getColumnIndexOrThrow(Lists_Items_Bridge_Table.COL_ITEM_ID);
					masterListItemID = allListTableItemsCursor
							.getLong(colIndex);
					String[] whereArgs = { String.valueOf(masterListItemID) };
					numberOfRecordsUpdated = cr.update(uri, values, where,
							whereArgs);
					if (numberOfRecordsUpdated != 1) {
						Log.e(TAG,
								"SetSelectedColumn: MasterListItemID = "
										+ masterListItemID
										+ "; incorrect number of MasterListItemsTable records included in ListTable with ID = "
										+ listTitleID);
						totalNumberOfRecordsUpdated = totalNumberOfRecordsUpdated
								+ numberOfRecordsUpdated;
					} else {
						totalNumberOfRecordsUpdated++;
					}

				} while (allListTableItemsCursor.moveToNext());

				if (allListTableItemsCursor.getCount() != totalNumberOfRecordsUpdated) {
					Log.e(TAG,
							"SetSelectedColumn: Incorrect number of total records set as selected in the ");
				}
			}

			AListUtilities.closeQuietly(allListTableItemsCursor);
		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in SetSelectedColumn. ", e);
		}
	}

	public static void ClearAllSelectedItems(Context context, long listTitleID) {
		Lists_Items_Bridge_Table.DeleteAllItems(context, listTitleID);
		ResetSelectedColumn(context);
	}

}
