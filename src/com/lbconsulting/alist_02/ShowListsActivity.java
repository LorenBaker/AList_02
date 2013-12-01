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
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

	private LinearLayout layoutNewCategory = null;

	private EditText txtCategoryName = null;

	private ListView lvListItems = null;
	private ActionBar actionBar = null;

	// ShowListsActivity Variables
	private boolean verbose = true;
	private long activeListID = 1;
	private long activeListTypeID = -1;
	private long activeCategoryID = -1;
	private boolean showCategories = true;
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
		//this.activeCategoryID = storedStates.getLong("activeCategoryID", 1); // 1 = [None]
		this.showCategories = storedStates.getBoolean("showCategories", true);
		this.listItemsSortOrder = storedStates.getInt("listItemsSortOrder", SORT_ORDER_ITEM_NAME);

		this.listTitlesCursor = ListTitlesTable.getListTitlesCursor(this);

		this.listTitlesPosition = AListUtilities.getPositionById(this.listTitlesCursor, this.activeListID);
		if (this.listTitlesPosition > -1) {
			this.listTitlesCursor.moveToPosition(this.listTitlesPosition);
			this.MakeListActive();
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
		//applicationStates.putLong("activeCategoryID", this.activeCategoryID);
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
				if (listTitlesCursor.moveToPrevious()) {
					MakeListActive();
				} else {
					listTitlesCursor.moveToLast();
					MakeListActive();
				}
			}
		});

		btnNextList = (Button) findViewById(R.id.btnNextList);
		btnNextList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listTitlesCursor.moveToNext()) {
					MakeListActive();
				} else {
					listTitlesCursor.moveToFirst();
					MakeListActive();
				}
			}
		});

		btnRemoveStruckOutItems = (Button) findViewById(R.id.btnRemoveStruckOutItems);
		btnRemoveStruckOutItems.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		txtCategoryName = (EditText) findViewById(R.id.txtCategoryName);
		layoutNewCategory = (LinearLayout) findViewById(R.id.layoutNewCategory);
		btnNewCategory = (Button) findViewById(R.id.btnNewCategory);
		btnNewCategory.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutNewCategory.setVisibility(View.VISIBLE);
				txtCategoryName.setFocusableInTouchMode(true);
				txtCategoryName.requestFocus();
			}
		});

		btnAddCategory = (Button) findViewById(R.id.btnAddCategory);
		btnAddCategory.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				CreateNewCategory();
			}

			private void CreateNewCategory() {
				ContentResolver cr = getContentResolver();
				Cursor categoriesCursor = null;
				String proposedCategory = txtCategoryName.getText().toString().trim();

				if (proposedCategory != null && proposedCategory.length() > 0) {

					// check if the proposedCategory is already in the CategoriesTable
					Uri uri = CategoriesTable.CONTENT_URI;
					String[] projection = CategoriesTable.PROJECTION_ALL;
					String selection = CategoriesTable.COL_CATEGORY_NAME + " = ?";
					String selectionArgs[] = { proposedCategory };
					String sortOrder = CategoriesTable.SORT_ORDER_CATEGORY;
					categoriesCursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);

				} else {
					Log.e(TAG, "ERROR in CreateNewCategory: no Category Name provided!");
					String msg = "Unable to create a new Category.\nNo Category provided!\nPlease try again.";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					return;
				}

				if (categoriesCursor != null) {
					if (categoriesCursor.getCount() > 0) {
						// Proposed Category already in the database ... so make it active
						activeCategoryID = categoriesCursor.getLong(categoriesCursor
								.getColumnIndexOrThrow(CategoriesTable.COL_ID));
						categoriesCursor.close();
						spnCategories.setSelection(AListUtilities.getIndex(spnCategories, activeCategoryID));
						layoutNewCategory.setVisibility(View.GONE);
						txtCategoryName.setText("");
						return;

					} else {
						// Proposed Category is not in the database ... so add it

						Uri uri = CategoriesTable.CONTENT_URI;
						ContentValues values = new ContentValues();
						values.put(CategoriesTable.COL_CATEGORY_NAME, proposedCategory);
						values.put(CategoriesTable.COL_LIST_TYPE_ID, activeListTypeID);
						Uri categoyNameUri = cr.insert(uri, values);
						activeCategoryID = Long.parseLong(categoyNameUri.getLastPathSegment());
						ListTitlesTable.setActiveCategoryID(getBaseContext(), activeCategoryID, activeListID);
						if (verbose) {
							String msg = "List: " + "\"" + proposedCategory + "\"" + " added to the database.";
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
						}

						categoriesCursor.close();
						layoutNewCategory.setVisibility(View.GONE);
						txtCategoryName.setText("");
						return;
					}

				} else {
					Log.e(TAG,
							"ERROR in CreateNewCategory: Exception in query. Unable to determine if proposed Category is in the database!");
					String msg = "Unable to create a new Category.\nUnable to determine if \""
							+ proposedCategory
							+ "\" is already in the database!\nPlease try again.";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

					return;
				}
			}
		});

		lvListItems = (ListView) findViewById(R.id.lvListItems);
		lvListItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				boolean strikOutValue = ListsTable.getStrikeOut(getBaseContext(), activeListID, id);
				if (strikOutValue) {
					// item is shown as struck out ... reverse it
					ListsTable.setStrikeOut(getBaseContext(), activeListID, id, false);

				} else {
					// item is shown as NOT struck out ... reverse it
					ListsTable.setStrikeOut(getBaseContext(), activeListID, id, true);
					int autoAddCategoriesOnStrikeout = listTitlesCursor.getInt(listTitlesCursor
							.getColumnIndex(ListTitlesTable.COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT));
					if (autoAddCategoriesOnStrikeout == 1) {
						ListsTable.setCategoryID(getBaseContext(), activeListID, id, activeCategoryID);
					}
				}
				loaderManager.restartLoader(LISTS_LOADER_ID, null, createListActivityCallbacks);
			}

		});

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
				ListTitlesTable.setActiveCategoryID(getBaseContext(), id, activeListID);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				activeCategoryID = 1; // 1 = [None]
			}

		});

		lvListsItemsAdapter = new ListsCursorAdapter(this, null, 0);
		lvListItems.setAdapter(lvListsItemsAdapter);

	}

	private void MakeListActive() {
		this.activeListID = this.listTitlesCursor.getLong(this.listTitlesCursor
				.getColumnIndexOrThrow(ListTitlesTable.COL_ID));
		this.activeListTypeID = this.listTitlesCursor.getLong(this.listTitlesCursor
				.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TYPE_ID));

		this.activeCategoryID = ListTitlesTable.getActiveCategoryId(this, activeListID);

		this.ActivateList();
		loaderManager.restartLoader(CATEGORIES_LOADER_ID, null, createListActivityCallbacks);
	}

	private void ActivateList() {
		if (this.listTitlesCursor != null) {
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
			uri = Uri.withAppendedPath(ListsTable.LIST_WITH_CATEGORY_URI, String.valueOf(activeListID));

			String tblMasterListItems_id = MasterListItemsTable.TABLE_MASTER_LIST_ITEMS + "."
					+ MasterListItemsTable.COL_ID;
			String itemName = MasterListItemsTable.COL_ITEM_NAME;
			String categoryName = CategoriesTable.COL_CATEGORY_NAME;
			String struckOut = ListsTable.COL_STRUCK_OUT;

			projection = new String[] { tblMasterListItems_id, itemName, categoryName, struckOut };

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
			lvListsItemsAdapter.setShowCategories(this.showCategories);
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
