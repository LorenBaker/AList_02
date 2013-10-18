package com.lbconsulting.alist_02.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
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
import com.lbconsulting.alist_02.database.List_Item_Table;
import com.lbconsulting.alist_02.database.ListsTable;
import com.lbconsulting.alist_02.database.MasterListItemsTable;
import com.lbconsulting.alist_02.database.PreviousCategoryTable;
import com.lbconsulting.alist_02.database.SortOrdersTable;

public class AListContentProvider extends ContentProvider {

	private final boolean L = AListUtilities.L; // enable Logging
	private final String TAG = AListUtilities.TAG;

	// Starting List Colors
	private static final String DARKBLUE = "#00008B";
	private static final String DARKGREEN = "#006400";
	private static final String DARKSALMON = "#E9967A";
	private static final String GHOSTWHITE = "#F8F8FF";
	private static final String HOTPINK = "#FF69B4";
	private static final String LAVENDER = "#E6E6FA";

	private static final String BLACK = "#000000";
	private static final String WHITE = "#FFFFFF";

	// AList database
	private AListDatabaseHelper database = null;

	// UriMatcher switch constants
	private static final int ITEMS_MULTI_ROWS = 10;
	private static final int ITEMS_SINGLE_ROW = 11;

	private static final int LISTS_MULTI_ROWS = 20;
	private static final int LISTS_SINGLE_ROW = 21;

	private static final int LIST_ITEM_MULTI_ROWS = 30;
	private static final int LIST_ITEM_SINGLE_ROW = 31;

	private static final int PREVIOUS_CATEGORY_MULTI_ROWS = 40;
	private static final int PREVIOUS_CATEGORY_SINGLE_ROW = 41;

	private static final int CATEGORIES_MULTI_ROWS = 50;
	private static final int CATEGORIES_SINGLE_ROW = 51;

	private static final int SORT_ORDERS_MULTI_ROWS = 60;
	private static final int SORT_ORDERS_SINGLE_ROW = 61;

	public static final String AUTHORITY = "com.lbconsulting.alist_02.contentprovider";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, MasterListItemsTable.CONTENT_PATH, ITEMS_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, MasterListItemsTable.CONTENT_PATH + "/#", ITEMS_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, ListsTable.CONTENT_PATH, LISTS_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, ListsTable.CONTENT_PATH + "/#", LISTS_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, List_Item_Table.CONTENT_PATH, LIST_ITEM_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, List_Item_Table.CONTENT_PATH + "/#", LIST_ITEM_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, PreviousCategoryTable.CONTENT_PATH, PREVIOUS_CATEGORY_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, PreviousCategoryTable.CONTENT_PATH + "/#", PREVIOUS_CATEGORY_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, CategoriesTable.CONTENT_PATH, CATEGORIES_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, CategoriesTable.CONTENT_PATH + "/#", CATEGORIES_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, SortOrdersTable.CONTENT_PATH, SORT_ORDERS_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, SortOrdersTable.CONTENT_PATH + "/#", SORT_ORDERS_SINGLE_ROW);

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

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String rowID = null;
		int deleteCount = 0;

		// Open a WritableDatabase database to support the delete transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {

		case ITEMS_MULTI_ROWS:
			// To return the number of deleted items you must specify a where
			// clause.
			// To delete all rows and return a value pass in "1".
			if (selection == null) {
				selection = "1";
			}

			// Perform the deletion
			deleteCount = db.delete(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, selection, selectionArgs);
			break;

		case ITEMS_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = MasterListItemsTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, selection, selectionArgs);
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

		case LIST_ITEM_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(List_Item_Table.TABLE_LIST_ITEM, selection, selectionArgs);
			break;

		case LIST_ITEM_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method delete: Deleting one row not supported by this method. Illegal URI: " + uri);

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY, selection, selectionArgs);
			break;

		case PREVIOUS_CATEGORY_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method delete: Deleting one row not supported by this method. Illegal URI: " + uri);

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

		case SORT_ORDERS_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(SortOrdersTable.TABLE_SORT_ORDERS, selection, selectionArgs);
			break;

		case SORT_ORDERS_SINGLE_ROW:
			rowID = uri.getLastPathSegment();
			selection = SortOrdersTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(SortOrdersTable.TABLE_SORT_ORDERS, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Method delete: Illegal URI: " + uri);
		}

		// Notify and observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			return MasterListItemsTable.CONTENT_TYPE;
		case ITEMS_SINGLE_ROW:
			return MasterListItemsTable.CONTENT_ITEM_TYPE;

		case LISTS_MULTI_ROWS:
			return ListsTable.CONTENT_TYPE;
		case LISTS_SINGLE_ROW:
			return ListsTable.CONTENT_ITEM_TYPE;

		case LIST_ITEM_MULTI_ROWS:
			return List_Item_Table.CONTENT_TYPE;
		case LIST_ITEM_SINGLE_ROW:
			return List_Item_Table.CONTENT_ITEM_TYPE;

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			return PreviousCategoryTable.CONTENT_TYPE;
		case PREVIOUS_CATEGORY_SINGLE_ROW:
			return PreviousCategoryTable.CONTENT_ITEM_TYPE;

		case CATEGORIES_MULTI_ROWS:
			return CategoriesTable.CONTENT_TYPE;
		case CATEGORIES_SINGLE_ROW:
			return CategoriesTable.CONTENT_ITEM_TYPE;

		case SORT_ORDERS_MULTI_ROWS:
			return SortOrdersTable.CONTENT_TYPE;
		case SORT_ORDERS_SINGLE_ROW:
			return SortOrdersTable.CONTENT_ITEM_TYPE;

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

		// Open a WritableDatabase database to support the insert transaction
		db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			newRowId = db.insertOrThrow(
					MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(MasterListItemsTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(MasterListItemsTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case ITEMS_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannon insert a new row with a single row URI. Illegal URI: " + uri);

		case LISTS_MULTI_ROWS:
			// Get the next List Id
			// So as to be able to select the new list's background color
			String nextIDQuery = "name='" + ListsTable.TABLE_LISTS + "'";
			long nextId = -1;
			Cursor nextIDCursor = db
					.query("SQLITE_SEQUENCE", new String[] { "*" }, nextIDQuery, null, null, null, null);
			if (nextIDCursor != null) {
				if (nextIDCursor.getCount() > 0) {
					// TODO: verify that this moveToFirst is needed .... done in
					// query?
					nextIDCursor.moveToFirst();
					nextId = nextIDCursor.getLong(nextIDCursor.getColumnIndexOrThrow("seq"));
					nextId++;
				} else {
					nextId = 1;
				}

				// TODO: Refactor -- make a method
				if (nextId > 0) {
					// select the new list title's colors
					String backgroundColor = DARKBLUE;
					String textColor = WHITE;

					int selector = (int) nextId % 6;
					switch (selector) {
					case 0:
						backgroundColor = GHOSTWHITE;
						textColor = BLACK;
						break;
					case 1:
						backgroundColor = DARKBLUE;
						textColor = WHITE;
						break;
					case 2:
						backgroundColor = DARKGREEN;
						textColor = WHITE;
						break;
					case 3:
						backgroundColor = DARKSALMON;
						textColor = WHITE;
						break;
					case 4:
						backgroundColor = HOTPINK;
						textColor = BLACK;
						break;
					case 5:
						backgroundColor = LAVENDER;
						textColor = BLACK;
						break;
					}

					if (values == null) {
						values = new ContentValues();
					}
					values.put(ListsTable.COL_BACKGROUND_COLOR, backgroundColor);
					values.put(ListsTable.COL_NORMAL_TEXT_COLOR, textColor);
				}

				AListUtilities.closeQuietly(nextIDCursor);
				newRowId = db.insertOrThrow(ListsTable.TABLE_LISTS, nullColumnHack, values);

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

		case LIST_ITEM_MULTI_ROWS:
			newRowId = db.insertOrThrow(List_Item_Table.TABLE_LIST_ITEM, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(List_Item_Table.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				Context context = getContext();
				ContentResolver cr = context.getContentResolver();
				try {
					cr.notifyChange(List_Item_Table.CONTENT_URI, null);
				} catch (Exception e) {
					String msg = e.getMessage();
				}

				// getContext().getContentResolver().notifyChange(List_Item_Table.CONTENT_URI, null);

				return newRowUri;
			}
			else {
				return null;
			}

		case LIST_ITEM_SINGLE_ROW:
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
			}
			else {
				return null;
			}
		case PREVIOUS_CATEGORY_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case CATEGORIES_MULTI_ROWS:
			newRowId = db.insertOrThrow(CategoriesTable.TABLE_CATEGORIES,
					nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(
						CategoriesTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(CategoriesTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case CATEGORIES_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case SORT_ORDERS_MULTI_ROWS:
			newRowId = db.insertOrThrow(SortOrdersTable.TABLE_SORT_ORDERS, nullColumnHack, values);
			if (newRowId > -1) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(SortOrdersTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(SortOrdersTable.CONTENT_URI, null);
				return newRowUri;
			}
			else {
				return null;
			}

		case SORT_ORDERS_SINGLE_ROW:
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
		case ITEMS_MULTI_ROWS:
			queryBuilder.setTables(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS);
			checkMasterListItemsColumnNames(projection);
			break;

		case ITEMS_SINGLE_ROW:
			queryBuilder.setTables(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS);
			checkMasterListItemsColumnNames(projection);
			queryBuilder.appendWhere(MasterListItemsTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case LISTS_MULTI_ROWS:
			queryBuilder.setTables(ListsTable.TABLE_LISTS);
			checkListTitlesColumnNames(projection);
			break;

		case LISTS_SINGLE_ROW:
			queryBuilder.setTables(ListsTable.TABLE_LISTS);
			checkListTitlesColumnNames(projection);
			queryBuilder.appendWhere(ListsTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case LIST_ITEM_MULTI_ROWS:
			queryBuilder.setTables(List_Item_Table.TABLE_LIST_ITEM);
			checkListsColumnNames(projection);
			break;

		case LIST_ITEM_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method query: query one row not supported by this method. Illegal URI: " + uri);

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			queryBuilder.setTables(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY);
			checkPreviousCategoryColumnNames(projection);
			break;

		case PREVIOUS_CATEGORY_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method query: query one row not supported by this method. Illegal URI: " + uri);

		case CATEGORIES_MULTI_ROWS:
			queryBuilder.setTables(CategoriesTable.TABLE_CATEGORIES);
			checkCategoriesColumnNames(projection);
			break;

		case CATEGORIES_SINGLE_ROW:
			queryBuilder.setTables(CategoriesTable.TABLE_CATEGORIES);
			checkCategoriesColumnNames(projection);
			queryBuilder.appendWhere(CategoriesTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		case SORT_ORDERS_MULTI_ROWS:
			queryBuilder.setTables(SortOrdersTable.TABLE_SORT_ORDERS);
			checkSortOrdersColumnNames(projection);
			break;

		case SORT_ORDERS_SINGLE_ROW:
			queryBuilder.setTables(SortOrdersTable.TABLE_SORT_ORDERS);
			checkSortOrdersColumnNames(projection);
			queryBuilder.appendWhere(SortOrdersTable.COL_ID + "=" + uri.getLastPathSegment());
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

			Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
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
		int updateCount = 0;

		// Open a WritableDatabase database to support the update transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, values, selection, selectionArgs);
			break;

		case ITEMS_SINGLE_ROW:
			// Limit update to a single row
			rowID = uri.getLastPathSegment();
			selection = MasterListItemsTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(MasterListItemsTable.TABLE_MASTER_LIST_ITEMS, values, selection, selectionArgs);
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

		case LIST_ITEM_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(List_Item_Table.TABLE_LIST_ITEM, values, selection, selectionArgs);
			break;

		case LIST_ITEM_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method delete: Updataing one row not supported by this method. Illegal URI: " + uri);

		case PREVIOUS_CATEGORY_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(PreviousCategoryTable.TABLE_PREVIOUS_CATEGORY, values, selection, selectionArgs);
			break;

		case PREVIOUS_CATEGORY_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method delete: Updataing one row not supported by this method. Illegal URI: " + uri);

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

		case SORT_ORDERS_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(SortOrdersTable.TABLE_SORT_ORDERS, values, selection, selectionArgs);
			break;

		case SORT_ORDERS_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = SortOrdersTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

			// Perform the update
			updateCount = db.update(SortOrdersTable.TABLE_SORT_ORDERS, values, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Method update: Unknown URI: " + uri);
		}

		// Notify and observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
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
			HashSet<String> requstedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(ListsTable.PROJECTION_ALL));

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
			HashSet<String> requstedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(List_Item_Table.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkListsColumnNames: Unknown ListsTable column name!");
			}
		}
	}

	private void checkPreviousCategoryColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(PreviousCategoryTable.PROJECTION_ALL));

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
			HashSet<String> requstedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(CategoriesTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkCategoriesColumnNames: Unknown CategoriesTable column name!");
			}
		}
	}

	private void checkSortOrdersColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(SortOrdersTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkSortOrdersColumnNames: Unknown SortOrdersTable column name!");
			}
		}
	}
}
