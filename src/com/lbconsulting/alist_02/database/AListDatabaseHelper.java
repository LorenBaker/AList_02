package com.lbconsulting.alist_02.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;

public class AListDatabaseHelper extends SQLiteOpenHelper {
	private final boolean L = AListUtilities.L; // enable Logging
	private final String TAG = AListUtilities.TAG;

	private static final String DATABASE_NAME = "AList.db";
	private static final int DATABASE_VERSION = 9;

	public AListDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		if (L)
			Log.i(TAG, "AListDatabaseHelper onCreate Starting.");

		MasterListItemsTable.onCreate(database);
		ListsTable.onCreate(database);
		CategoriesTable.onCreate(database);
		ListTypesTable.onCreate(database);

		Lists_Items_Bridge_Table.onCreate(database);
		PreviousCategoryTable.onCreate(database);
		SortOrdersTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		MasterListItemsTable.onUpgrade(database, oldVersion, newVersion);
		ListsTable.onUpgrade(database, oldVersion, newVersion);
		CategoriesTable.onUpgrade(database, oldVersion, newVersion);
		ListTypesTable.onUpgrade(database, oldVersion, newVersion);

		Lists_Items_Bridge_Table.onUpgrade(database, oldVersion, newVersion);
		PreviousCategoryTable.onUpgrade(database, oldVersion, newVersion);
		SortOrdersTable.onUpgrade(database, oldVersion, newVersion);
	}

}
