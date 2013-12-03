package com.lbconsulting.alist_02.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;

public class AListDatabaseHelper extends SQLiteOpenHelper {
	private final boolean L = AListUtilities.L; // enable Logging
	private final String TAG = AListUtilities.TAG;
	private final Context context;

	private static final String DATABASE_NAME = "AList.db";
	private static final int DATABASE_VERSION = 1;

	public AListDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		if (L)
			Log.i(TAG, "AListDatabaseHelper onCreate Starting.");
		MasterListItemsTable.onCreate(database);
		ListTitlesTable.onCreate(database);
		CategoriesTable.setDefalutCategoryValue(context);
		CategoriesTable.onCreate(database);
		ListTypesTable.onCreate(database);

		ListsTable.onCreate(database);
		PreviousCategoryTable.onCreate(database);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		MasterListItemsTable.onUpgrade(database, oldVersion, newVersion);
		ListTitlesTable.onUpgrade(database, oldVersion, newVersion);
		CategoriesTable.onUpgrade(database, oldVersion, newVersion);
		ListTypesTable.onUpgrade(database, oldVersion, newVersion);

		ListsTable.onUpgrade(database, oldVersion, newVersion);
		PreviousCategoryTable.onUpgrade(database, oldVersion, newVersion);

	}

}
