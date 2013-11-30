package com.lbconsulting.alist_02.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.lbconsulting.alist_02.AListUtilities;
import com.lbconsulting.alist_02.database.AListDatabaseHelper;
import com.lbconsulting.alist_02.database.CategoriesTable;
import com.lbconsulting.alist_02.database.ListTitlesTable;
import com.lbconsulting.alist_02.database.ListTypesTable;
import com.lbconsulting.alist_02.database.ListsTable;
import com.lbconsulting.alist_02.database.MasterListItemsTable;
import com.lbconsulting.alist_02.database.PreviousCategoryTable;

public class AListContentProvider extends ContentProvider {

	private final boolean L = AListUtilities.L; // enable Logging
	private final String TAG = AListUtilities.TAG;

	// AList database
	private AListDatabaseHelper database = null;

	// UriMatcher switch constants
	private static final int MASTER_LIST_MULTI_ROWS = 10;
	private static final int MASTER_LIST_SINGLE_ROW = 11;

	private static final int LIST_TITLE_MULTI_ROWS = 20;
	private static final int LIST_TITLE_SINGLE_ROW = 21;

	private static final int LISTS_MULTI_ROWS = 30;
	private static final int LISTS_SINGLE_ROW = 31;

	private static final int PREVIOUS_CATEGORY_MULTI_ROWS = 40;
	private static final int PREVIOUS_CATEGORY_SINGLE_ROW = 41;

	private static final int CATEGORIES_MULTI_ROWS = 50;
	private static final int CATEGORIES_SINGLE_ROW = 51;

	private static final int SORT_ORDERS_MULTI_ROWS = 60;
	private static final int SORT_ORDERS_SINGLE_ROW = 61;

	private static final int LIST_TYPE_MULTI_ROWS = 70;
	private static final int LIST_TYPE_SINGLE_ROW = 71;

	private static final int LIST_WITH_CATEGORY_SINGLE_ROW = 81;

	public static final String AUTHORITY = "com.lbconsulting.alist_02.contentprovider";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, MasterListItemsTable.CONTENT_PATH, MASTER_LIST_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, MasterListItemsTable.CONTENT_PATH + "/#", MASTER_LIST_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, ListTitlesTable.CONTENT_PATH, LIST_TITLE_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, ListTitlesTable.CONTENT_PATH + "/#", LIST_TITLE_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, ListsTable.CONTENT_PATH, LISTS_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, ListsTable.CONTENT_PATH + "/#", LISTS_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, PreviousCategoryTable.CONTENT_PATH, PREVIOUS_CATEGORY_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, PreviousCategoryTable.CONTENT_PATH + "/#", PREVIOUS_CATEGORY_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, CategoriesTable.CONTENT_PATH, CATEGORIES_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, CategoriesTable.CONTENT_PATH + "/#", CATEGORIES_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, ListTypesTable.CONTENT_PATH, LIST_TYPE_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, ListTypesTable.CONTENT_PATH + "/#", LIST_TYPE_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, ListsTable.CONTENT_LIST_WITH_CATEGORY + "/#", LIST_WITH_CATEGORY_SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {

		if (L)
			Log.i(TAG, "AListContentProvider onCreate Starting.");
		// Construct the underlying database
		// Defer opening the database until you need to perform
		// a query or other transaction.
		database = new AListDatabaseHelper(getContext());
		return true;
	}

	/*A content provider is created when its hosting process is created, 
	 * and remains around for as long as the process does, so there is 
	 * no need to close the database -- it will get closed as part of the 
	 * kernel cleaning up the process's resources when the process is killed.
	 */
	@SuppressWarnings("resource")
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String rowID = null;
		int deleteCount = 0;

		// Open a WritableDatabase database to support the delete transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case MASTER_LIST_MULTI_ROWS:
			// To return the number of deleted items you must specify a where clause.
			// To delete all rows and return a value pass in "1".
			if (selection == null) {
				selection = "1";
			}

			// Perform the deletion
			deleteCount = db.delete(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, selection, selectionArgs);
			break;

		case MASTER_LIST_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = MasterListItemsTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, selection, selectionArgs);
			break;

		case LIST_TITLE_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(ListTitlesTable.TABLE_LIST_TITLES, selection, selectionArgs);
			break;

		case LIST_TITLE_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ListTitlesTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(ListTitlesTable.TABLE_LIST_TITLES, selection, selectionArgs);
			break;

		case LISTS_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(ListsTable.TABLE_LISTS, selection, selectionArgs);
			break;

		case LISTS_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ListsTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(ListsTable.TABLE_LISTS, selection, selectionArgs);
			break;

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY, selection, selectionArgs);
			break;

		case PREVIOUS_CATEGORY_SINGLE_ROW:
			rowID = uri.getLastPathSegment();
			selection = PreviousCategoryTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY, selection, selectionArgs);
			break;

		case CATEGORIES_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(CategoriesTable.TABLE_CATEGORIES, selection, selectionArgs);
			break;

		case CATEGORIES_SINGLE_ROW:
			rowID = uri.getLastPathSegment();
			selection = CategoriesTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(CategoriesTable.TABLE_CATEGORIES, selection, selectionArgs);
			break;

		case LIST_TYPE_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(ListTypesTable.TABLE_LIST_TYPES, selection, selectionArgs);
			break;

		case LIST_TYPE_SINGLE_ROW:
			rowID = uri.getLastPathSegment();
			selection = ListTypesTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(ListTypesTable.TABLE_LIST_TYPES, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Method delete: Unknown URI: " + uri);
		}

		// Notify and observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case MASTER_LIST_MULTI_ROWS:
			return MasterListItemsTable.CONTENT_TYPE;
		case MASTER_LIST_SINGLE_ROW:
			return MasterListItemsTable.CONTENT_ITEM_TYPE;

		case LIST_TITLE_MULTI_ROWS:
			return ListTitlesTable.CONTENT_TYPE;
		case LIST_TITLE_SINGLE_ROW:
			return ListTitlesTable.CONTENT_ITEM_TYPE;

		case LISTS_MULTI_ROWS:
			return ListsTable.CONTENT_TYPE;
		case LISTS_SINGLE_ROW:
			return ListsTable.CONTENT_ITEM_TYPE;

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			return PreviousCategoryTable.CONTENT_TYPE;
		case PREVIOUS_CATEGORY_SINGLE_ROW:
			return PreviousCategoryTable.CONTENT_ITEM_TYPE;

		case CATEGORIES_MULTI_ROWS:
			return CategoriesTable.CONTENT_TYPE;
		case CATEGORIES_SINGLE_ROW:
			return CategoriesTable.CONTENT_ITEM_TYPE;

		case LIST_TYPE_MULTI_ROWS:
			return ListTypesTable.CONTENT_TYPE;
		case LIST_TYPE_SINGLE_ROW:
			return ListTypesTable.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Method getType. Unknown URI: " + uri);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = null;
		long newRowId = 0;
		String nullColumnHack = null;
		String[] sKeys = null;

		// Open a WritableDatabase database to support the insert transaction
		db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case MASTER_LIST_MULTI_ROWS:
			newRowId = db.insertOrThrow(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(MasterListItemsTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(MasterListItemsTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case MASTER_LIST_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannon insert a new row with a single row URI. Illegal URI: " + uri);

		case LIST_TITLE_MULTI_ROWS:
			newRowId = db.insertOrThrow(ListTitlesTable.TABLE_LIST_TITLES, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(ListTitlesTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(ListTitlesTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case LIST_TITLE_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case LISTS_MULTI_ROWS:
			newRowId = db.insertOrThrow(ListsTable.TABLE_LISTS, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(ListsTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(ListsTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case LISTS_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			newRowId = db.insertOrThrow(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(PreviousCategoryTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(PreviousCategoryTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case PREVIOUS_CATEGORY_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case CATEGORIES_MULTI_ROWS:
			newRowId = db.insertOrThrow(CategoriesTable.TABLE_CATEGORIES, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(CategoriesTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(CategoriesTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case CATEGORIES_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case LIST_TYPE_MULTI_ROWS:
			newRowId = db.insertOrThrow(ListTypesTable.TABLE_LIST_TYPES, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(ListTypesTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(ListTypesTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case LIST_TYPE_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		default:
			throw new IllegalArgumentException("Method insert: Unknown URI: " + uri);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case MASTER_LIST_MULTI_ROWS:
			queryBuilder.setTables(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS);
			checkMasterListItemsColumnNames(projection);
			break;

		case MASTER_LIST_SINGLE_ROW:
			queryBuilder.setTables(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS);
			checkMasterListItemsColumnNames(projection);
			queryBuilder.appendWhere(MasterListItemsTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case LIST_TITLE_MULTI_ROWS:
			queryBuilder.setTables(ListTitlesTable.TABLE_LIST_TITLES);
			checkListTitlesColumnNames(projection);
			break;

		case LIST_TITLE_SINGLE_ROW:
			queryBuilder.setTables(ListTitlesTable.TABLE_LIST_TITLES);
			checkListTitlesColumnNames(projection);
			queryBuilder.appendWhere(ListTitlesTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case LISTS_MULTI_ROWS:
			queryBuilder.setTables(ListsTable.TABLE_LISTS);
			checkListsColumnNames(projection);
			break;

		case LISTS_SINGLE_ROW:
			queryBuilder.setTables(ListsTable.TABLE_LISTS);
			checkListsColumnNames(projection);
			queryBuilder.appendWhere(ListsTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			queryBuilder.setTables(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY);
			checkPreviousCategoryColumnNames(projection);
			break;

		case PREVIOUS_CATEGORY_SINGLE_ROW:
			queryBuilder.setTables(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY);
			checkPreviousCategoryColumnNames(projection);
			queryBuilder.appendWhere(PreviousCategoryTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case CATEGORIES_MULTI_ROWS:
			queryBuilder.setTables(CategoriesTable.TABLE_CATEGORIES);
			checkCategoriesColumnNames(projection);
			break;

		case CATEGORIES_SINGLE_ROW:
			queryBuilder.setTables(CategoriesTable.TABLE_CATEGORIES);
			checkCategoriesColumnNames(projection);
			queryBuilder.appendWhere(CategoriesTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case LIST_TYPE_MULTI_ROWS:
			queryBuilder.setTables(ListTypesTable.TABLE_LIST_TYPES);
			checkListTypesColumnNames(projection);
			break;

		case LIST_TYPE_SINGLE_ROW:
			queryBuilder.setTables(ListTypesTable.TABLE_LIST_TYPES);
			checkListTypesColumnNames(projection);
			queryBuilder.appendWhere(ListTypesTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case LIST_WITH_CATEGORY_SINGLE_ROW:
			/*		SELECT 
			itemName, categoryName 
			FROM 
			tblLists JOIN tblMasterListItems, tblCategories ON tblLists.MasterListItemID= tblMasterListItems._Id
			WHERE 
			tblLists.categoryID=tblCategories._Id  AND tblLists.listTitleID=1*/

			/*ArrayList<String> newProjection = new ArrayList<String>();
			newProjection.add("itemName");
			newProjection.add("categoryName");
			projection = (String[]) newProjection.toArray();*/

			String tables = "tblLists JOIN tblMasterListItems, tblCategories ON tblLists.MasterListItemID= tblMasterListItems._Id";
			queryBuilder.setTables(tables);

			String where = "tblLists.categoryID=tblCategories._Id  " +
					"AND tblLists.listTitleID" + "=" + uri.getLastPathSegment();
			queryBuilder.appendWhere(where);
			break;

		default:
			throw new IllegalArgumentException("Method query. Unknown URI: " + uri);
		}

		// Execute the query on the database
		SQLiteDatabase db = null;
		try {
			db = database.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = database.getReadableDatabase();
		}

		if (null != db) {
			String groupBy = null;
			String having = null;
			Cursor cursor = null;
			try {
				cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
			} catch (Exception e) {
				Log.e(TAG, "An Exception error occurred in AListContentProvider:query. ", e);
			}

			if (null != cursor) {
				cursor.moveToFirst();
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
			}
			return cursor;
		} else {
			return null;
		}
	}

	@SuppressWarnings("resource")
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String rowID = null;
		String[] sKeys = null;
		int updateCount = 0;

		// Open a WritableDatabase database to support the update transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case MASTER_LIST_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, values, selection, selectionArgs);
			break;

		case MASTER_LIST_SINGLE_ROW:
			// Limit update to a single row
			rowID = uri.getLastPathSegment();
			selection = MasterListItemsTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, values, selection, selectionArgs);
			break;

		case LIST_TITLE_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(ListTitlesTable.TABLE_LIST_TITLES, values, selection, selectionArgs);
			break;

		case LIST_TITLE_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ListTitlesTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(ListTitlesTable.TABLE_LIST_TITLES, values, selection, selectionArgs);
			break;

		case LISTS_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(ListsTable.TABLE_LISTS, values, selection, selectionArgs);
			break;

		case LISTS_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ListsTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(ListsTable.TABLE_LISTS, values, selection, selectionArgs);
			break;

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY, values, selection, selectionArgs);
			break;

		case PREVIOUS_CATEGORY_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = PreviousCategoryTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY, values, selection, selectionArgs);
			break;

		case CATEGORIES_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(CategoriesTable.TABLE_CATEGORIES, values, selection, selectionArgs);
			break;

		case CATEGORIES_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = CategoriesTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(CategoriesTable.TABLE_CATEGORIES, values, selection, selectionArgs);
			break;

		case LIST_TYPE_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(ListTypesTable.TABLE_LIST_TYPES, values, selection, selectionArgs);
			break;

		case LIST_TYPE_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ListTypesTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(ListTypesTable.TABLE_LIST_TYPES, values, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Method update: Unknown URI: " + uri);
		}

		// Notify and observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}

	/* From: http://stackoverflow.com/questions/14090695/how-to-use-join-query-in-cursorloader-when-its-constructor-does-not-support-it
	 
	The Uri does not point to any table. It points to whatever you feel like pointing it to.

	Let's pretend that your two tables are Customer and Order. One customer may have many orders. 
	You want to execute a query to get all outstanding orders... but you want to join in some customer-related columns 
	that you will need, such as the customer's name.

	Let's further pretend that you already have 
	content://your.authority.goes.here/customer and content://your.authority.goes.here/order 
	defined to purely query those tables.

	You have two choices:

	(1)	Add the join of the customer's display name on your /order Uri. 
		Having another available column probably will not break any existing consumers 
		of the provider (though testing is always a good idea). This is what ContactsContract does -- 
		it joins in some base columns, like the contact's name, on pretty much all queries of all tables.
		
	(2)	Create content://your.authority.goes.here/orderWithCust 
		that does the same basic query as /order does, but contains your join. 
		In this case, you could have insert(), update(), and delete() throw some sort of RuntimeException, 
		to remind you that you should not be modifying data using /orderWithCust as a Uri.

	In the end, designing a ContentProvider Uri system is similar to designing a REST Web service's URL system. 
	In both cases, the join has to be done on the provider/server side, and so you may need to break 
	the one-table-to-one-URL baseline to offer up some useful joins.*/

	public Cursor getList(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return null;

	}

	private void checkMasterListItemsColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(MasterListItemsTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkMasterListItemsColumnNames: Unknown MasterListItemsTable column name!");
			}
		}
	}

	private void checkListTitlesColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(ListTitlesTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkListTitlesColumnNames: Unknown ListTitlesTable column name!");
			}
		}
	}

	private void checkListsColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(ListsTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException("Method checkListsColumnNames: Unknown ListsTable column name!");
			}
		}
	}

	private void checkPreviousCategoryColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(PreviousCategoryTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkPreferencesColumnNames: Unknown PreviousCategoryTable column name!");
			}
		}
	}

	private void checkCategoriesColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(CategoriesTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkCategoriesColumnNames: Unknown CategoriesTable column name!");
			}
		}
	}

	private void checkListTypesColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(ListTypesTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkSortOrdersColumnNames: Unknown ListTypesTable column name!");
			}
		}
	}

	/**
	 * A test package can call this to get a handle to the database underlying
	 * AListContentProvider, so it can insert test data into the database. The
	 * test case class is responsible for instantiating the provider in a test
	 * context; {@link android.test.ProviderTestCase2} does this during the call
	 * to setUp()
	 * 
	 * @return a handle to the database helper object for the provider's data.
	 */
	public AListDatabaseHelper getOpenHelperForTest() {
		return database;
	}
}
