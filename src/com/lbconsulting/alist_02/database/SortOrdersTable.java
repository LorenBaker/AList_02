package com.lbconsulting.alist_02.database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.contentprovider.AListContentProvider;

public class SortOrdersTable {

	private static final String TAG = AListUtilities.TAG;

	// MasterListItems data table
	public static final String TABLE_SORT_ORDERS = "tblSortOrders";
	public static final String COL_ID = "_id";
	public static final String COL_SORT_ORDER_NAME = "sortOrderName";
	public static final String COL_SORT_ORDER_FIELD = "sortOrderField";
	public static final String[] PROJECTION_ALL = { COL_ID, COL_SORT_ORDER_NAME, COL_SORT_ORDER_FIELD };

	public static final String CONTENT_PATH = TABLE_SORT_ORDERS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_SORT_ORDERS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_SORT_ORDERS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_BY_MASTER_LIST_ITEM = "0";
	public static final String SORT_BY_CATEGORY = "1";
	public static final String SORT_BY_MANUAL = "2";

	// Database creation SQL statements
	private static final String DATABASE_CREATE =
			"create table " + TABLE_SORT_ORDERS + " ("
					+ COL_ID + " integer primary key autoincrement, "
					+ COL_SORT_ORDER_NAME + " text collate nocase, "
					+ COL_SORT_ORDER_FIELD + " text collate nocase "
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("insert into " + TABLE_SORT_ORDERS + " (" + COL_ID + ", " + COL_SORT_ORDER_NAME + ", "
				+ COL_SORT_ORDER_FIELD + ") values (NULL, 'List Item', '" + MasterListItemsTable.COL_ITEM_NAME + "')");
		sqlStatements.add("insert into " + TABLE_SORT_ORDERS + " (" + COL_ID + ", " + COL_SORT_ORDER_NAME + ", "
				+ COL_SORT_ORDER_FIELD + ") values (NULL, 'Manual', '" + List_Item_Table.COL_MANUAL_SORT_ORDER
				+ "')");
		sqlStatements.add("insert into " + TABLE_SORT_ORDERS + " (" + COL_ID + ", " + COL_SORT_ORDER_NAME + ", "
				+ COL_SORT_ORDER_FIELD + ") values (NULL, 'Category', '" + CategoriesTable.COL_CATEGORY + ", "
				+ MasterListItemsTable.COL_ITEM_NAME + "')");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TAG, TABLE_SORT_ORDERS + ": Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SORT_ORDERS);
		onCreate(database);
	}
}
