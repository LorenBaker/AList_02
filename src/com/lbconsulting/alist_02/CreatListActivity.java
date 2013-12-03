package com.lbconsulting.alist_02;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lbconsulting.alist_02.R.color;
import com.lbconsulting.alist_02.adapters.ListPreviewArrayAdapter;
import com.lbconsulting.alist_02.adapters.ListTypesCursorAdapter;
import com.lbconsulting.alist_02.database.AListDatabaseHelper;
import com.lbconsulting.alist_02.database.ListTitlesTable;
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
	private LinearLayout newListTypeLinearLayout = null;

	private ActionBar actionBar = null;

	// CreateListActivity Variables
	private boolean verbose = true;
	private long activeListID = -1;
	private long activeListTypeID = -1;

	private int backgroundColor;
	private int normalTextColor;
	private int strikeoutTextColor;

	//private int spnListTypesPosition = -1;
	private boolean isNewListCreated = false;

	// The loaders' unique ids.
	// Loader ids are specific to the Activity
	private static final int LIST_TYPES_LOADER_ID = 3;
	private LoaderManager loaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> createListActivityCallbacks;
	private ListTypesCursorAdapter listTypesAdapter;
	private AListDatabaseHelper database = null;

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
		this.verbose = storedStates.getBoolean("verbose", true);
		this.activeListID = storedStates.getLong("activeListID", 1);
		this.activeListTypeID = storedStates.getLong("activeListTypeID", 1); // 1 = Groceries		
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
		//actionBar.setTitle("AList");
		actionBar.setTitle("Create A New List");

		// Initialize the controls
		txtNewListTitle = (EditText) findViewById(R.id.txtNewListTitle);
		txtNewListTypeName = (EditText) findViewById(R.id.txtNewListTypeName);
		spnListTypes = (Spinner) findViewById(R.id.spnListTypes);

		btnNewListType = (Button) findViewById(R.id.btnNewListType);
		btnAddListType = (Button) findViewById(R.id.btnAddListType);
		btnSelectColors = (Button) findViewById(R.id.btnSelectColors);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCreateNewList = (Button) findViewById(R.id.btnCreateNewList);

		newListTypeLinearLayout = (LinearLayout) findViewById(R.id.newListTypeLinearLayout);

		lstPreview = (ListView) findViewById(R.id.lstPreview);
		String[] previewList = { "List Item Normal Text", "List Item Strikeout Text" };
		ListPreviewArrayAdapter listPreviewArrayAdapter = new ListPreviewArrayAdapter(this, previewList);
		this.setNewListColors();
		listPreviewArrayAdapter.setColors(this.backgroundColor, this.normalTextColor, this.strikeoutTextColor);
		lstPreview.setAdapter(listPreviewArrayAdapter);

		// set up laderManager
		loaderManager = getLoaderManager();
		createListActivityCallbacks = this;
		// Note: using null for the cursor. The masterList and listTitle cursors
		// loaded via onLoadFinished.
		loaderManager.initLoader(LIST_TYPES_LOADER_ID, null, createListActivityCallbacks);

		this.setNewListColors();
		listTypesAdapter = new ListTypesCursorAdapter(this, null, 0);
		spnListTypes.setAdapter(listTypesAdapter);
		spnListTypes.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				activeListTypeID = id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				activeListTypeID = -1;
			}

		});

		// set button Click Listeners

		btnNewListType.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				newListTypeLinearLayout.setVisibility(View.VISIBLE);
				txtNewListTypeName.setFocusableInTouchMode(true);
				txtNewListTypeName.requestFocus();
			}
		});

		btnAddListType.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				CreateNewListType();
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
				// exit to the MasterListActivity
				onBackPressed();
			}
		});

		btnCreateNewList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// validate the selected list type
				if (activeListTypeID < 0) {
					String msg = "Unable to create a new List.\nNo list type provided!\nPlease try again.";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					isNewListCreated = false;
					return;
				}

				// validate the list title input
				String proposedListTitle = txtNewListTitle.getText().toString().trim();
				if (proposedListTitle != null && proposedListTitle.length() > 0) {
					// inputs are valid
					CreateNewList(proposedListTitle);
				} else {
					String msg = "Unable to create a new List.\nNo list list title provided!\nPlease try again.";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					isNewListCreated = false;
					return;
				}

				if (isNewListCreated) {
					// exit to the MasterListActivity
					onBackPressed();
				}
			}

		});

	}

	private void CreateNewListType() {
		ContentResolver cr = getContentResolver();
		Cursor listTypesCursor = null;
		String proposedListType = txtNewListTypeName.getText().toString().trim();

		if (proposedListType != null && proposedListType.length() > 0) {

			// check if the proposedListType is already in the ListTypesTable
			Uri uri = ListTypesTable.CONTENT_URI;
			String[] projection = ListTypesTable.PROJECTION_ALL;
			String selection = ListTypesTable.COL_LIST_TYPE + " = ?";
			String selectionArgs[] = { proposedListType };
			String sortOrder = ListTypesTable.SORT_ORDER_LIST_TYPE;
			listTypesCursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);

		} else {
			Log.e(TAG, "ERROR in CreateNewListType: no ListTypeName provided!");
			String msg = "Unable to create a new List Type.\nNo ListType provided!\nPlease try again.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			return;
		}

		if (listTypesCursor != null) {
			if (listTypesCursor.getCount() > 0) {
				// Proposed list type already in the database ... so make it active
				this.activeListTypeID = listTypesCursor.getLong(listTypesCursor
						.getColumnIndexOrThrow(ListTypesTable.COL_ID));
				listTypesCursor.close();
				spnListTypes.setSelection(AListUtilities.getIndex(spnListTypes, this.activeListTypeID));
				newListTypeLinearLayout.setVisibility(View.GONE);
				txtNewListTypeName.setText("");
				return;
			} else {
				// Proposed list type is not in the database ... so add it

				Uri uri = ListTypesTable.CONTENT_URI;
				ContentValues values = new ContentValues();
				values.put(ListTypesTable.COL_LIST_TYPE, proposedListType);
				Uri listTypeUri = cr.insert(uri, values);
				this.activeListTypeID = Long.parseLong(listTypeUri.getLastPathSegment());

				if (verbose) {
					String msg = "List: " + "\"" + proposedListType + "\"" + " added to the database.";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}

				listTypesCursor.close();
				newListTypeLinearLayout.setVisibility(View.GONE);
				txtNewListTypeName.setText("");

				return;
			}

		} else {
			Log.e(TAG,
					"ERROR in CreateNewListType: Exception in query. Unable to determine if proposed list type is in the database!");
			String msg = "Unable to create a new List Type.\nUnable to determine if \""
					+ proposedListType
					+ "\" is already in the database!\nPlease try again.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

			return;
		}
	}

	private void setNewListColors() {

		// Open a WritableDatabase database to support the insert transaction
		database = new AListDatabaseHelper(this);
		SQLiteDatabase db = database.getWritableDatabase();
		// get the next Id for the lists title table
		String nextIDQuery = "name='" + ListTitlesTable.TABLE_LIST_TITLES + "'";
		long nextId = -1;
		Cursor nextIDCursor = db
				.query("SQLITE_SEQUENCE", new String[] { "*" }, nextIDQuery, null, null, null, null);
		if (nextIDCursor != null) {
			if (nextIDCursor.getCount() > 0) {
				nextIDCursor.moveToFirst();
				nextId = nextIDCursor.getLong(nextIDCursor.getColumnIndexOrThrow("seq"));
				nextId++;
				nextIDCursor.close();
			} else {
				nextId = 1;
			}

			Resources res = this.getResources();
			if (nextId > 0) {
				// select the new list title's colors

				int selector = (int) nextId % 6;
				switch (selector) {
				case 0:
					this.backgroundColor = res.getColor(color.ghostwhite);
					this.normalTextColor = res.getColor(R.color.black);
					this.strikeoutTextColor = res.getColor(R.color.darkgray);
					break;
				case 1:
					this.backgroundColor = res.getColor(R.color.darkblue);
					this.normalTextColor = res.getColor(R.color.white);
					this.strikeoutTextColor = res.getColor(R.color.lightgrey);
					break;
				case 2:
					this.backgroundColor = res.getColor(R.color.darkgreen);
					this.normalTextColor = res.getColor(R.color.white);
					this.strikeoutTextColor = res.getColor(R.color.lightgrey);
					break;
				case 3:
					this.backgroundColor = res.getColor(R.color.darksalmon);
					this.normalTextColor = res.getColor(R.color.white);
					this.strikeoutTextColor = res.getColor(R.color.darkgreen);
					break;
				case 4:
					this.backgroundColor = res.getColor(R.color.hotpink);
					this.normalTextColor = res.getColor(R.color.black);
					this.strikeoutTextColor = res.getColor(R.color.darkgray);
					break;
				case 5:
					this.backgroundColor = res.getColor(R.color.lavender);
					this.normalTextColor = res.getColor(R.color.black);
					this.strikeoutTextColor = res.getColor(R.color.lightgrey);
					break;
				default:
					this.backgroundColor = res.getColor(R.color.darkblue);
					this.normalTextColor = res.getColor(R.color.white);
					this.strikeoutTextColor = res.getColor(R.color.lightgoldenrodyellow);
					break;
				}
			} else {
				this.backgroundColor = res.getColor(R.color.darkblue);
				this.normalTextColor = res.getColor(R.color.white);
				this.strikeoutTextColor = res.getColor(R.color.lightgrey);
			}
		}
	}

	private void CreateNewList(String proposedListTitle) {
		ContentResolver cr = getContentResolver();

		// check if the newListTitle is already in the ListTitlesTable
		Cursor listTitlesCursor = null;
		Uri uri = ListTitlesTable.CONTENT_URI;
		String[] projection = ListTitlesTable.PROJECTION_ALL;
		String selection = ListTitlesTable.COL_LIST_TITLE_NAME + " = ?";
		String selectionArgs[] = { proposedListTitle };
		String sortOrder = ListTitlesTable.SORT_ORDER_LIST_TITLE;
		try {
			listTitlesCursor = cr.query(uri, projection, selection, selectionArgs,
					sortOrder);
		} catch (Exception e) {
			Log.e(TAG, "AddNewList: Exception in query.", e);
			String msg = "Unable to create a new List.\nUnable to determine if \""
					+ proposedListTitle
					+ "\" is already in the database!\nPlease try again.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			if (listTitlesCursor != null) {
				listTitlesCursor.close();
			}
			this.isNewListCreated = false;
			return;
		}

		if (listTitlesCursor != null
				&& listTitlesCursor.getCount() > 0) {
			// newListTitle already in ListTitlesTable
			// so make it active
			this.activeListID = listTitlesCursor.getLong(
					listTitlesCursor.getColumnIndexOrThrow(ListTitlesTable.COL_ID));
			this.activeListTypeID = listTitlesCursor.getLong(
					listTitlesCursor.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TYPE_ID));

			this.isNewListCreated = true;

			if (verbose) {
				String msg = "\"" + proposedListTitle + "\" already exists in the database!";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}

		} else {
			// newListTitle not in ListTitlesTable .. so add it.
			uri = ListTitlesTable.CONTENT_URI;
			ContentValues values = new ContentValues();
			values.put(ListTitlesTable.COL_LIST_TITLE_NAME, proposedListTitle);
			values.put(ListTitlesTable.COL_LIST_TYPE_ID, this.activeListTypeID);

			values.put(ListTitlesTable.COL_BACKGROUND_COLOR, this.backgroundColor);
			values.put(ListTitlesTable.COL_NORMAL_TEXT_COLOR, this.normalTextColor);
			values.put(ListTitlesTable.COL_STRIKEOUT_TEXT_COLOR, this.strikeoutTextColor);

			Uri activeListUri = cr.insert(uri, values);
			this.activeListID = Long.parseLong(activeListUri.getLastPathSegment());

			this.isNewListCreated = true;

			if (verbose) {
				String msg = "List: " + "\"" + proposedListTitle + "\"" + " added to the database.";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}

		if (listTitlesCursor != null) {
			listTitlesCursor.close();
		}
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
