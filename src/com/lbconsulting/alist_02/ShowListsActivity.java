package com.lbconsulting.alist_02;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
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

import com.lbconsulting.alist_02.database.AListDatabaseHelper;
import com.lbconsulting.alist_02.database.CategoriesTable;
import com.lbconsulting.alist_02.database.ListsTable;
import com.lbconsulting.alist_02.database.MasterListItemsTable;

public class ShowListsActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	// String for logging the class name
	public final String TAG = AListUtilities.TAG;
	private final boolean L = AListUtilities.L; // enable Logging

	// ShowListsActivity Views and related variables
	private Spinner spnCategories = null;

	private final Button btnPreviousList = null;
	private final Button btnNextList = null;
	private Button btnRemoveStruckOutItems = null;
	private final Button btnNewCategory = null;
	private final Button btnAddCategory = null;

	private final EditText txtCategoryName = null;

	private ListView lstListItems = null;
	private ActionBar actionBar = null;

	// ShowListsActivity Variables
	private boolean verbose = true;
	private long activeListID = -1;
	private long activeListTypeID = -1;
	private long activeCategoryID = -1;

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
	private ListsCursorAdapter listsAdapter;
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

		// Commit to storage
		applicationStates.commit();
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
		actionBar.setSubtitle("Show Lists");

		// Initialize the controls
		spnCategories = (Spinner) findViewById(R.id.spnCategories);

		btnRemoveStruckOutItems = (Button) findViewById(R.id.btnRemoveStruckOutItems);

		/*linearLayoutCategories = (LinearLayout) findViewById(R.id.linearLayoutCategories);*/

		lstListItems = (ListView) findViewById(R.id.lstListItems);

		// set up laderManager
		loaderManager = getLoaderManager();
		createListActivityCallbacks = this;
		// Note: using null for the cursor. The masterList and listTitle cursors
		// loaded via onLoadFinished.
		//loaderManager.initLoader(LISTS_LOADER_ID, null, createListActivityCallbacks);
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
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (L) {
			Log.i(TAG, "ShowLists onCreateLoader. id = " + id);
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
				Log.e(TAG, "ShowLists onCreateLoader: SQLiteException", e);
				return null;

			} catch (IllegalArgumentException e) {
				Log.e(TAG, "ShowLists onCreateLoader: IllegalArgumentException", e);
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

			uri = ListsTable.LIST_WITH_CATEGORY_URI;

			ArrayList<String> selectionArgsList = new ArrayList<String>();
			selectionArgsList.add(String.valueOf(activeListID));
			selectionArgs = (String[]) selectionArgsList.toArray();

			sortOrder = MasterListItemsTable.SORT_ORDER_ITEM_NAME;

			cursorLoader = new CursorLoader(ShowListsActivity.this, uri, projection, selection, selectionArgs,
					sortOrder);
			break;

		default:
			break;
		}

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		if (L) {
			Log.i(TAG, "ShowLists onLoadFinished. loader id = " + loader.getId());
		}
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case CATEGORIES_LOADER_ID:
			categoriesAdapter.swapCursor(newCursor);
			spnCategories.setSelection(AListUtilities.getIndex(spnCategories, this.activeCategoryID));
			break;

		case LISTS_LOADER_ID:

			break;

		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (L) {
			Log.i(TAG, "ShowLists onLoaderReset. loader id = " + loader.getId());
		}
		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with a null Cursor.
		switch (loader.getId()) {
		case CATEGORIES_LOADER_ID:
			categoriesAdapter.swapCursor(null);
			break;

		case LISTS_LOADER_ID:

			break;

		default:
			break;
		}

	}

}
