package com.lbconsulting.alist_02;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.lbconsulting.alist_02.adapters.CategoriesCursorAdapter;
import com.lbconsulting.alist_02.adapters.ListsCursorAdapter;
import com.lbconsulting.alist_02.database.AListDatabaseHelper;
import com.lbconsulting.alist_02.database.CategoriesTable;
import com.lbconsulting.alist_02.database.ListTitlesTable;
import com.lbconsulting.alist_02.database.ListsTable;
import com.lbconsulting.alist_02.database.MasterListItemsTable;

public class ShowListsActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	// String for logging the class name
	public final String TAG = AListUtilities.TAG;
	private final boolean L = AListUtilities.L; // enable Logging
	private final int SORT_ORDER_ITEM_NAME = 0;
	private final int SORT_ORDER_CATEGORY = 1;

	// ShowListsActivity Views and related variables
	private Spinner spnCategories = null;

	private Button btnPreviousList = null;
	private Button btnNextList = null;
	private Button btnRemoveStruckOutItems = null;
	private Button btnNewCategory = null;
	private Button btnAddCategory = null;

	private final EditText txtCategoryName = null;

	private ListView lvListItems = null;
	private ActionBar actionBar = null;

	// ShowListsActivity Variables
	private boolean verbose = true;
	private long activeListID = 1;
	private long activeListTypeID = -1;
	private long activeCategoryID = -1;
	private boolean showCategories = false;
	private int listItemsSortOrder = SORT_ORDER_ITEM_NAME;
	private Cursor listTitlesCursor = null;
	private int listTitlesPosition = -1;

	private int backgroundColor;
	private int normalTextColor;
	private int strikeoutTextColor;

	// The loaders' unique ids.
	// Loader ids are specific to the Activity
	private static final int CATEGORIES_LOADER_ID = 4;
	private static final int LISTS_LOADER_ID = 5;
	private LoaderManager loaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> createListActivityCallbacks;
	private CategoriesCursorAdapter categoriesAdapter;
	private ListsCursorAdapter lvListsItemsAdapter;
	private final AListDatabaseHelper database = null;

	// /////////////////////////////////////////////////////////////////////////////
	// ShowListsActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (L)
			Log.i(TAG, "ShowListsActivity onCreate Starting.");

		// To keep this method simple place onCreate code at doCreate
		doCreate(savedInstanceState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "ShowListsActivity onCreate completed."
					+ (null != savedInstanceState ? " Restored state." : ""));
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Notification that the activity will be started
		if (L)
			Log.i(TAG, "ShowListsActivity onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Notification that the activity is starting
		if (L)
			Log.i(TAG, "ShowListsActivity onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Notification that the activity will interact with the user
		if (L)
			Log.i(TAG, "ShowListsActivity onResume");

		// Get the between instance stored values
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);

		// Set application states
		this.verbose = storedStates.getBoolean("verbose", true);
		this.activeListID = storedStates.getLong("activeListID", 1);
		this.activeListTypeID = storedStates.getLong("activeListTypeID", 1); // 1 = Groceries
		this.activeCategoryID = storedStates.getLong("activeCategoryID", 1); // 1 = [None]
		this.showCategories = storedStates.getBoolean("showCategories", false);
		this.listItemsSortOrder = storedStates.getInt("listItemsSortOrder", SORT_ORDER_ITEM_NAME);

		ContentResolver cr = this.getContentResolver();
		Uri uri = ListTitlesTable.CONTENT_URI;
		String[] projection = ListTitlesTable.PROJECTION_ALL;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = ListTitlesTable.SORT_ORDER_LIST_TITLE;
		this.listTitlesCursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		this.listTitlesPosition = AListUtilities.getPositionById(this.listTitlesCursor, this.activeListID);
		if (this.listTitlesPosition > -1) {
			this.activateList();
		}
		loaderManager.restartLoader(CATEGORIES_LOADER_ID, null, createListActivityCallbacks);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "ShowListsActivity onPause" + (isFinishing() ? " Finishing" : ""));

		// Store values between instances here
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		applicationStates.putLong("activeListID", this.activeListID);
		applicationStates.putLong("activeListTypeID", this.activeListTypeID);
		applicationStates.putLong("activeCategoryID", this.activeCategoryID);
		applicationStates.putBoolean("showCategories", this.showCategories);
		applicationStates.putInt("listItemsSortOrder", this.listItemsSortOrder);

		// Commit to storage
		applicationStates.commit();
		if (this.listTitlesCursor != null) {
			this.listTitlesCursor.close();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Notification that the activity is no longer visible
		if (L)
			Log.i(TAG, "ShowListsActivity onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Notification the activity will be destroyed
		if (L)
			Log.i(TAG, "ShowListsActivity onDestroy"
					// Are we finishing?
					+ (isFinishing() ? " Finishing" : ""));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Called when state should be saved

		if (L)
			Log.i(TAG, "ShowListsActivity onSaveInstanceState");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "ShowListsActivity onRestoreInstanceState."
					+ (null != savedState ? " Restored state." : ""));
	}

	// /////////////////////////////////////////////////////////////////////////////
	// The minor lifecycle methods - you probably won't need these
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onPostCreate(Bundle savedState) {
		super.onPostCreate(savedState);
		/* if (null != savedState) restoreState(savedState); */

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "ShowListsActivity onPostCreate"
					+ (null == savedState ? " Restored state" : ""));

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		// Notification that resuming the activity is complete
		if (L)
			Log.i(TAG, "ShowListsActivity onPostResume");
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		// Notification that user navigated away from this activity
		if (L)
			Log.i(TAG, "ShowListsActivity onUserLeaveHint");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Overrides of the implementations ComponentCallbacks methods in Activity
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public void onConfigurationChanged(Configuration newConfiguration) {
		super.onConfigurationChanged(newConfiguration);

		// This won't happen unless we declare changes we handle in the manifest
		if (L)
			Log.i(TAG, "ShowListsActivity onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		// No guarantee this is called before or after other callbacks
		if (L)
			Log.i(TAG, "ShowListsActivity onLowMemory");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// End ShowListsActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////

	private void doCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_show_lists);

		actionBar = getActionBar();
		actionBar.setTitle("AList");
		//actionBar.setSubtitle("Show Lists");

		// Initialize the controls
		spnCategories = (Spinner) findViewById(R.id.spnCategories);

		spnCategories.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// TODO Auto-generated method stub
			}

		});

		btnPreviousList = (Button) findViewById(R.id.btnPreviousList);
		btnPreviousList.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		btnNextList = (Button) findViewById(R.id.btnNextList);
		btnNextList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		btnRemoveStruckOutItems = (Button) findViewById(R.id.btnRemoveStruckOutItems);
		btnRemoveStruckOutItems.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		btnNewCategory = (Button) findViewById(R.id.btnNewCategory);
		btnNewCategory.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		btnAddCategory = (Button) findViewById(R.id.btnAddCategory);
		btnAddCategory.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		lvListItems = (ListView) findViewById(R.id.lvListItems);

		// set up laderManager
		loaderManager = getLoaderManager();
		createListActivityCallbacks = this;
		// Note: using null for the cursor. The masterList and listTitle cursors
		// loaded via onLoadFinished.

		loaderManager.initLoader(LISTS_LOADER_ID, null, createListActivityCallbacks);
		loaderManager.initLoader(CATEGORIES_LOADER_ID, null, createListActivityCallbacks);

		categoriesAdapter = new CategoriesCursorAdapter(this, null, 0);
		spnCategories.setAdapter(categoriesAdapter);
		spnCategories.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				activeCategoryID = id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				activeCategoryID = 1; // 1 = [None]
			}

		});

		lvListsItemsAdapter = new ListsCursorAdapter(this, null, 0);
		lvListItems.setAdapter(lvListsItemsAdapter);

	}

	private void activateList() {
		this.listTitlesCursor.moveToPosition(this.listTitlesPosition);
		this.backgroundColor = this.listTitlesCursor.getInt(this.listTitlesCursor
				.getColumnIndexOrThrow(ListTitlesTable.COL_BACKGROUND_COLOR));
		this.normalTextColor = this.listTitlesCursor.getInt(this.listTitlesCursor
				.getColumnIndexOrThrow(ListTitlesTable.COL_NORMAL_TEXT_COLOR));
		this.strikeoutTextColor = this.listTitlesCursor.getInt(this.listTitlesCursor
				.getColumnIndexOrThrow(ListTitlesTable.COL_STRIKEOUT_TEXT_COLOR));
		this.lvListItems.setBackgroundColor(this.backgroundColor);
		String listName = this.listTitlesCursor.getString(this.listTitlesCursor
				.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TITLE_NAME));
		actionBar.setSubtitle(listName);

		loaderManager.restartLoader(LISTS_LOADER_ID, null, createListActivityCallbacks);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (L) {
			Log.i(TAG, "ShowListsActivity onCreateLoader. id = " + id);
		}
		CursorLoader cursorLoader = null;
		Uri uri = null;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;

		switch (id) {
		case CATEGORIES_LOADER_ID:
			// Create a new categories CursorLoader with the following query parameters.
			uri = CategoriesTable.CONTENT_URI;
			projection = CategoriesTable.PROJECTION_ALL;
			selection = CategoriesTable.COL_LIST_TYPE_ID + " = " + activeListTypeID
					+ " OR " + CategoriesTable.COL_LIST_TYPE_ID + " = -1";
			sortOrder = CategoriesTable.SORT_ORDER_CATEGORY;

			try {
				cursorLoader = new CursorLoader(ShowListsActivity.this, uri, projection, selection, selectionArgs,
						sortOrder);

			} catch (SQLiteException e) {
				Log.e(TAG, "ShowListsActivity onCreateLoader: SQLiteException", e);
				return null;

			} catch (IllegalArgumentException e) {
				Log.e(TAG, "ShowListsActivity onCreateLoader: IllegalArgumentException", e);
				return null;
			}
			break;

		case LISTS_LOADER_ID:
			/*		SELECT itemName, categoryName
					FROM tblLists
					JOIN tblMasterListItems, tblCategories
					ON tblLists.MasterListItemID= tblMasterListItems._Id
					WHERE tblLists.categoryID=tblCategories._Id
					 AND tblLists.listTitleID=1*/

			//if (activeListID > 0) {
			uri = Uri.withAppendedPath(ListsTable.LIST_WITH_CATEGORY_URI, String.valueOf(activeListID));

			projection = new String[] { "tblMasterListItems._id", "itemName", "categoryName", "struckOut" };

			switch (listItemsSortOrder) {

			case SORT_ORDER_CATEGORY:
				sortOrder = CategoriesTable.SORT_ORDER_CATEGORY + ", " + MasterListItemsTable.SORT_ORDER_ITEM_NAME;
				break;

			default:
				sortOrder = MasterListItemsTable.SORT_ORDER_ITEM_NAME;
				break;
			}

			cursorLoader = new CursorLoader(ShowListsActivity.this, uri, projection, selection, selectionArgs,
					sortOrder);
			//}
			break;

		default:
			break;
		}

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		if (L) {
			Log.i(TAG, "ShowListsActivity onLoadFinished. loader id = " + loader.getId());
		}
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case CATEGORIES_LOADER_ID:
			categoriesAdapter.swapCursor(newCursor);
			spnCategories.setSelection(AListUtilities.getIndex(spnCategories, this.activeCategoryID));
			break;

		case LISTS_LOADER_ID:
			lvListsItemsAdapter.setColors(this.backgroundColor, this.normalTextColor, this.strikeoutTextColor);
			lvListsItemsAdapter.swapCursor(newCursor);

			break;

		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (L) {
			Log.i(TAG, "ShowListsActivity onLoaderReset. loader id = " + loader.getId());
		}
		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with a null Cursor.
		switch (loader.getId()) {
		case CATEGORIES_LOADER_ID:
			categoriesAdapter.swapCursor(null);
			break;

		case LISTS_LOADER_ID:
			lvListsItemsAdapter.swapCursor(null);
			break;

		default:
			break;
		}

	}

}
