package com.lbconsulting.alist_02;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.lbconsulting.alist_02.database.ListTypesTable;

public class CreatListActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	// String for logging the class name
	public final String TAG = AListUtilities.TAG;
	private final boolean L = AListUtilities.L; // enable Logging

	// CreateListActivity Views and related variables
	private EditText txtNewListTitle = null;
	private EditText txtNewListTypeName = null;
	private Spinner spnListTypes = null;
	private Button btnNewListType = null;
	private Button btnAddListType = null;
	private Button btnSelectColors = null;
	private Button btnCancel = null;
	private Button btnCreateNewList = null;
	private ListView lstPreview = null;

	private ActionBar actionBar = null;

	// CreateListActivity Variables
	public long activeListID = -1;
	public long activeListTypeID = 2;
	//private int spnListTypesPosition = -1;
	private final boolean isNewListCreated = false;

	// The loaders' unique ids.
	// Loader ids are specific to the Activity
	private static final int LIST_TYPES_LOADER_ID = 3;
	private LoaderManager loaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> createListActivityCallbacks;
	private ListTypesCursorAdapter listTypesAdapter;

	// /////////////////////////////////////////////////////////////////////////////
	// CreateListActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (L)
			Log.i(TAG, "CreateListActivity onCreate Starting.");

		// To keep this method simple place onCreate code at doCreate
		doCreate(savedInstanceState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "CreateListActivity onCreate completed."
					+ (null != savedInstanceState ? " Restored state." : ""));
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Notification that the activity will be started
		if (L)
			Log.i(TAG, "CreateListActivity onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Notification that the activity is starting
		if (L)
			Log.i(TAG, "CreateListActivity onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Notification that the activity will interact with the user
		if (L)
			Log.i(TAG, "CreateListActivity onResume");

		// Get the between instance stored values
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);

		// Set application states
		this.activeListID = storedStates.getLong("activeListID", 1);
		this.activeListTypeID = storedStates.getLong("activeListTypeID", 2); // 2 = Groceries		
		//this.txtNewListTitle.setText(storedStates.getString("txtNewListTitle", null));
		//this.spnListTypesPosition = storedStates.getInt("spnListTypesPosition", 1); // 1 = [None]

	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "CreateListActivity onPause" + (isFinishing() ? " Finishing" : ""));

		if (isNewListCreated) {
			// Store values between instances here
			SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
			SharedPreferences.Editor applicationStates = preferences.edit();

			applicationStates.putLong("activeListID", this.activeListID);
			applicationStates.putLong("activeListTypeID", this.activeListTypeID);
			//applicationStates.putString("txtNewListTitle", txtNewListTitle.getText().toString());
			//applicationStates.putInt("spnListTypesPosition", spnListTypes.getSelectedItemPosition());

			// Commit to storage
			applicationStates.commit();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Notification that the activity is no longer visible
		if (L)
			Log.i(TAG, "CreateListActivity onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Notification the activity will be destroyed
		if (L)
			Log.i(TAG, "CreateListActivity onDestroy"
					// Are we finishing?
					+ (isFinishing() ? " Finishing" : ""));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Called when state should be saved

		if (L)
			Log.i(TAG, "CreateListActivity onSaveInstanceState");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "CreateListActivity onRestoreInstanceState."
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
			Log.i(TAG, "CreateListActivity onPostCreate"
					+ (null == savedState ? " Restored state" : ""));

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		// Notification that resuming the activity is complete
		if (L)
			Log.i(TAG, "CreateListActivity onPostResume");
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		// Notification that user navigated away from this activity
		if (L)
			Log.i(TAG, "CreateListActivity onUserLeaveHint");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Overrides of the implementations ComponentCallbacks methods in Activity
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public void onConfigurationChanged(Configuration newConfiguration) {
		super.onConfigurationChanged(newConfiguration);

		// This won't happen unless we declare changes we handle in the manifest
		if (L)
			Log.i(TAG, "CreateListActivity onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		// No guarantee this is called before or after other callbacks
		if (L)
			Log.i(TAG, "CreateListActivity onLowMemory");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// End CreateListActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////

	private void doCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_list);

		actionBar = getActionBar();
		actionBar.setTitle("AList");
		actionBar.setSubtitle("Create List");

		// Initialize the controls
		txtNewListTitle = (EditText) findViewById(R.id.txtNewListTitle);
		txtNewListTypeName = (EditText) findViewById(R.id.txtNewListTypeName);
		spnListTypes = (Spinner) findViewById(R.id.spnListTypes);

		btnNewListType = (Button) findViewById(R.id.btnNewListType);
		btnAddListType = (Button) findViewById(R.id.btnAddListType);
		btnSelectColors = (Button) findViewById(R.id.btnSelectColors);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCreateNewList = (Button) findViewById(R.id.btnCreateNewList);

		lstPreview = (ListView) findViewById(R.id.lstPreview);
		String[] previewList = { "List Item Normal Text", "List Item Strikeout Text" };
		ListPreviewArrayAdapter listPreviewArrayAdapter = new ListPreviewArrayAdapter(this, previewList);
		lstPreview.setAdapter(listPreviewArrayAdapter);

		// set up laderManager
		loaderManager = getLoaderManager();
		createListActivityCallbacks = this;
		// Note: using null for the cursor. The masterList and listTitle cursors
		// loaded via onLoadFinished.
		/*loaderManager.initLoader(MASTER_LIST_LOADER_ID, null, createListActivityCallbacks);
		loaderManager.initLoader(LIST_TITLES_LOADER_ID, null, createListActivityCallbacks);*/
		loaderManager.initLoader(LIST_TYPES_LOADER_ID, null, createListActivityCallbacks);

		listTypesAdapter = new ListTypesCursorAdapter(this, null, 0);
		spnListTypes.setAdapter(listTypesAdapter);

		// set button Click Listeners

		btnNewListType.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: code btnNewListType onClick method

			}
		});

		btnAddListType.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: code btnAddListType onClick method

			}
		});

		btnSelectColors.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: code btnSelectColors onClick method

			}
		});

		btnCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isNewListCreated) {

				} else {
					onBackPressed();
				}
				// TODO: code btnCancel onClick method

			}
		});

		btnCreateNewList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: code btnCreateNewList onClick method

			}
		});

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (L) {
			Log.i(TAG, "onCreateLoader. id = " + id);
		}
		CursorLoader cursorLoader = null;
		Uri uri = null;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;

		switch (id) {
		case LIST_TYPES_LOADER_ID:
			// Create a new list types CursorLoader with the following query parameters.
			uri = ListTypesTable.CONTENT_URI;
			projection = ListTypesTable.PROJECTION_ALL;
			sortOrder = ListTypesTable.SORT_ORDER_LIST_TYPE;

			try {
				cursorLoader = new CursorLoader(CreatListActivity.this, uri, projection, selection, selectionArgs,
						sortOrder);

			} catch (SQLiteException e) {
				Log.e(TAG, "onCreateLoader: SQLiteException", e);
				return null;

			} catch (IllegalArgumentException e) {
				Log.e(TAG, "onCreateLoader: IllegalArgumentException", e);
				return null;
			}
			break;

		default:
			break;
		}

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		if (L) {
			Log.i(TAG, "onLoadFinished. loader id = " + loader.getId());
		}
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case LIST_TYPES_LOADER_ID:
			listTypesAdapter.swapCursor(newCursor);
			spnListTypes.setSelection(AListUtilities.getIndex(spnListTypes, this.activeListTypeID));
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (L) {
			Log.i(TAG, "onLoaderReset. loader id = " + loader.getId());
		}
		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with a null Cursor.
		switch (loader.getId()) {
		case LIST_TYPES_LOADER_ID:
			listTypesAdapter.swapCursor(null);
			break;

		default:
			break;
		}

	}

}
