package com.lbconsulting.alist_02;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_02.database.CategoriesTable;
import com.lbconsulting.alist_02.database.ListTitlesTable;
import com.lbconsulting.alist_02.database.ListsTable;
import com.lbconsulting.alist_02.database.MasterListItemsTable;
import com.lbconsulting.alist_02.database.PreviousCategoryTable;
import com.lbconsulting.alist_02.database.SortOrdersTable;

public class MasterListActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	// /////////////////////////////////////////////////////////////////////////////
	// AList Activity variables
	// /////////////////////////////////////////////////////////////////////////////

	// String for logging the class name
	public final String TAG = AListUtilities.TAG;
	private final boolean L = AListUtilities.L; // enable Logging

	private final int SORT_ORDER_ALPHABETICAL = 0;
	private final int SORT_ORDER_SELECTED_AT_BOTTOM = 1;
	private final int SORT_ORDER_SELECTED_AT_TOP = 2;

	// The loaders' unique ids.
	// Loader ids are specific to the Activity
	private static final int MASTER_LIST_LOADER_ID = 1;
	private static final int LIST_TITLES_LOADER_ID = 2;

	// Application preferences
	private boolean autoAddItem = true;
	private boolean verbose = true;
	public long activeListID = -1;
	private int masterListSortOrder = SORT_ORDER_ALPHABETICAL;
	private int masterListViewFirstVisiblePosition = 0;
	private int masterListViewTop = 0;
	private Boolean flagFirstTimeThruMasterListLoader = true;
	private boolean flagFirstTimeThruListTitlesLoader = true;
	private int spnListTitlesPosition = -1;

	// TODO create preferences for color, text size, etc for
	// MasterListActivity's views.
	private final int txtListItemTextColor = 0;
	private final int txtListItemBackgroundColor = 0;
	private int spnListTitlesTextColor = 0;
	private int spnListTitlesBackgroundColor = 0;

	// private Boolean flagOnStart=false;

	// AList module variables
	private LoaderManager loaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> masterListCallbacks;

	private ListView masterListView = null;
	private MasterListCursorAdapter masterListAdapter;
	private ListTitlesCursorAdapter listTitlesAdapter;

	private static Button btnAddToMasterList = null;
	private static Button btnStartListsViewActivity = null;
	private static EditText txtListItem = null;
	private static Spinner spnListTitles = null;
	private static LinearLayout layoutListTitle = null;

	// /////////////////////////////////////////////////////////////////////////////
	// MasterListActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (L)
			Log.i(TAG, "MasterListActivity onCreate Starting.");
		// To keep this method simple
		flagFirstTimeThruMasterListLoader = true;
		flagFirstTimeThruListTitlesLoader = true;
		doCreate(savedInstanceState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "MasterListActivity onCreate completed."
					+ (null != savedInstanceState ? " Restored state." : ""));
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Notification that the activity will be started
		if (L)
			Log.i(TAG, "MasterListActivity onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Notification that the activity is starting
		if (L)
			Log.i(TAG, "MasterListActivity onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Notification that the activity will interact with the user
		if (L)
			Log.i(TAG, "MasterListActivity onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "MasterListActivity onPause"
					+ (isFinishing() ? " Finishing" : ""));

		// Store values between instances here
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		applicationStates.putBoolean("autoAddItem", autoAddItem);
		applicationStates.putBoolean("verbose", verbose);
		applicationStates.putLong("activeListID", this.activeListID);
		applicationStates.putString("txtListItem", txtListItem.getText()
				.toString());
		applicationStates.putInt("masterListSortOrder", masterListSortOrder);

		applicationStates.putInt("masterListViewFirstVisiblePosition",
				masterListView.getFirstVisiblePosition());
		View v = masterListView.getChildAt(0);
		masterListViewTop = (v == null) ? 0 : v.getTop();
		applicationStates.putInt("masterListViewTop", masterListViewTop);
		applicationStates.putInt("spnListTitlesPosition",
				spnListTitles.getSelectedItemPosition());

		// Commit to storage
		applicationStates.commit();
		/*- See more at: http://eigo.co.uk/labs/managing-state-in-an-android-activity/#sthash.13x5kIOB.dpuf*/
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Notification that the activity is no longer visible
		if (L)
			Log.i(TAG, "MasterListActivity onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Notification the activity will be destroyed
		if (L)
			Log.i(TAG, "MasterListActivity onDestroy"
			// Are we finishing?
					+ (isFinishing() ? " Finishing" : ""));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Called when state should be saved

		if (L)
			Log.i(TAG, "MasterListActivity onSaveInstanceState");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "MasterListActivity onRestoreInstanceState."
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
			Log.i(TAG, "MasterListActivity onPostCreate"
					+ (null == savedState ? " Restored state" : ""));

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		// Notification that resuming the activity is complete
		if (L)
			Log.i(TAG, "MasterListActivity onPostResume");
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		// Notification that user navigated away from this activity
		if (L)
			Log.i(TAG, "MasterListActivity onUserLeaveHint");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Overrides of the implementations ComponentCallbacks methods in Activity
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public void onConfigurationChanged(Configuration newConfiguration) {
		super.onConfigurationChanged(newConfiguration);

		// This won't happen unless we declare changes we handle in the manifest
		if (L)
			Log.i(TAG, "MasterListActivity onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		// No guarantee this is called before or after other callbacks
		if (L)
			Log.i(TAG, "MasterListActivity onLowMemory");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// MasterListActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////
	private void doCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_master_list);

		// Initialize the controls
		txtListItem = (EditText) findViewById(R.id.txtListItem);
		masterListView = (ListView) findViewById(R.id.lstItems);
		btnAddToMasterList = (Button) findViewById(R.id.btnAddToMasterList);
		btnStartListsViewActivity = (Button) findViewById(R.id.btnStartListsViewActivity);
		spnListTitles = (Spinner) findViewById(R.id.spnListTitle);
		layoutListTitle = (LinearLayout) findViewById(R.id.linearLayoutListTitle);

		// Get the between instance stored values
		SharedPreferences storedStates = getPreferences(MODE_PRIVATE);

		// Set application states
		autoAddItem = storedStates.getBoolean("autoAddItem", true);
		verbose = storedStates.getBoolean("verbose", true);
		this.activeListID = storedStates.getLong("activeListID", -1);
		masterListSortOrder = storedStates.getInt("masterListSortOrder", 0);
		txtListItem.setText(storedStates.getString("txtListItem", null));
		masterListViewFirstVisiblePosition = storedStates.getInt(
				"masterListViewFirstVisiblePosition", 0);
		masterListViewTop = storedStates.getInt("masterListViewTop", 0);
		spnListTitlesPosition = storedStates
				.getInt("spnListTitlesPosition", -1);

		if (this.activeListID < 0) {
			layoutListTitle.setVisibility(View.INVISIBLE);
		}

		// setup txtListItem Listeners
		txtListItem.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
					String newMasterListItem = txtListItem.getText().toString()
							.trim();
					if (newMasterListItem.isEmpty()) {
						txtListItem.setText("");
						return true;
					}

					long newMasterListItemID = AddItemToMasterList(newMasterListItem);
					txtListItem.setText("");
					if (autoAddItem && activeListID > 0
							&& newMasterListItemID > 0) {
						AddItemToActiveList(newMasterListItem,
								newMasterListItemID);
					}
					return true;
				} else {
					return false;
				}
			}
		});

		txtListItem.addTextChangedListener(new TextWatcher() {
			// filter master list as the user inputs text
			@Override
			public void afterTextChanged(Editable s) {
				loaderManager.restartLoader(MASTER_LIST_LOADER_ID, null,
						masterListCallbacks);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Do nothing

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Do nothing

			}

		});

		// setup btnAddToMasterList
		btnAddToMasterList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String newMasterListItem = txtListItem.getText().toString()
						.trim();
				if (newMasterListItem.isEmpty()) {
					txtListItem.setText("");
					return;
				}

				long newMasterListItemID = AddItemToMasterList(newMasterListItem);
				txtListItem.setText("");
				if (autoAddItem && newMasterListItemID > 0) {
					AddItemToActiveList(newMasterListItem, newMasterListItemID);
				}
			}
		});

		// setup btnStartListsViewActivity
		btnStartListsViewActivity
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Code ListsViewActivity
						if (L) {
							String msg = "Start ListsViewActivity is under construction.";
							Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		// setup masterListView Listeners
		masterListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor masterListItem = getMasterListItem(id, null);
				int isSelected = masterListItem.getInt(masterListItem
						.getColumnIndexOrThrow(MasterListItemsTable.COL_SELECTED));
				if (isSelected == 1) {
					// the clicked masterListItem is already selected ... so
					// remove it from the active list.
					RemoveItemFromActiveList(
							masterListItem
									.getString(masterListItem
											.getColumnIndexOrThrow(MasterListItemsTable.COL_ITEM_NAME)),
							id);
				} else {
					// the clicked masterListItem has not been selected for the
					// active list ... so add it.
					if (view instanceof LinearLayout) {
						txtListItem.setText("");
						LinearLayout linearLayout = (LinearLayout) view;
						TextView txtView = (TextView) linearLayout
								.getChildAt(0);
						String newListItem = txtView.getText().toString();
						AddItemToActiveList(newListItem, id);
					}
				}
				AListUtilities.closeQuietly(masterListItem);
			}
		});

		masterListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO create dialog giving the user the choice to:
						// Delete or
						// Edit the selected item, or Cancel
						if (verbose) {
							String msg = "masterListView LONG CLICK: Position = "
									+ position + "; ID = " + id;
							Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_SHORT).show();
						}
						// Long Click handled.
						return true;
					}
				});

		// setup spnListTitles Listener
		spnListTitles.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				ActivateList(id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				layoutListTitle.setVisibility(View.INVISIBLE);
				activeListID = -1;
			}

		});

		// set up masterListView adapter and loader
		loaderManager = getLoaderManager();

		masterListAdapter = new MasterListCursorAdapter(this, null, 0);
		masterListView.setAdapter(masterListAdapter);

		listTitlesAdapter = new ListTitlesCursorAdapter(this, null, 0);
		spnListTitles.setAdapter(listTitlesAdapter);

		masterListCallbacks = this;
		// Note: using null for the cursor. The masterList and listTitle cursors
		// loaded via onLoadFinished.
		loaderManager.initLoader(MASTER_LIST_LOADER_ID, null,
				masterListCallbacks);
		loaderManager.initLoader(LIST_TITLES_LOADER_ID, null,
				masterListCallbacks);
	} // End doCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.master_list, menu);
		if (L) {
			Log.i(TAG, "onCreateOptionsMenu");
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String msg = null;

		switch (item.getItemId()) {

		case R.id.addNewList:
			AddNewList();
			return true;

		case R.id.clearAllSelectedItems:
			MasterListItemsTable.ClearAllSelectedItems(this, this.activeListID);
			return true;

		case R.id.editActiveListTitle:
			// TODO code menu editActiveListTitle
			msg = "Edit Active List Title under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();
			return true;

		case R.id.deleteActiveList:
			DeleteList(this.activeListID);
			return true;

		case R.id.masterListSorting:
			ShowMasterListSortOrderSelectionPopUp();
			return true;

		case R.id.deleteAllMasterListItems:
			// TODO code menu deleteAllMasterListItems
			msg = "Delete All Master List Items under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();
			return true;

		case R.id.about:
			// TODO code menu about
			// startActivity(new Intent(this, About.class));
			msg = "About under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();
			return true;

		case R.id.masterListSettings:
			// TODO code menu masterListSettings
			msg = "Master List Settings under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void AddItemToActiveList(String newMasterListItem,
			long newMasterListItemID) {
		String msg = null;
		ContentResolver cr = getContentResolver();

		if (this.activeListID < 0) {
			msg = "No List selected.\nPlease create or select a List before adding items.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();
			return;
		}

		// Check that masterListItemID is not already included in the ListsTable
		// for current active list.
		String[] projection = { ListsTable.COL_ID };
		String selection = ListsTable.COL_LIST_TITLE_ID + " = ? AND "
				+ ListsTable.COL_MASTER_LIST_ITEM_ID + " = ?";
		String[] selectionArgs = { String.valueOf(this.activeListID),
				String.valueOf(newMasterListItemID) };
		Cursor includedInListsTableCursor = cr.query(ListsTable.CONTENT_URI,
				projection, selection, selectionArgs, null);

		if (includedInListsTableCursor.getCount() > 0) {
			if (verbose) {
				msg = "\"" + newMasterListItem + "\""
						+ " is already included in the active list!";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
			}
			AListUtilities.closeQuietly(includedInListsTableCursor);
			return;
		} else {
			AListUtilities.closeQuietly(includedInListsTableCursor);

			// newMasterListItemID is not included in the active list ... so add
			// it
			ContentValues values = new ContentValues();
			values.put(ListsTable.COL_LIST_TITLE_ID, this.activeListID);
			values.put(ListsTable.COL_MASTER_LIST_ITEM_ID, newMasterListItemID);

			Uri newListUri = cr.insert(ListsTable.CONTENT_URI, values);
			long newListID = Long.parseLong(newListUri.getLastPathSegment());

			// make the manual sort order equal to the newListID --- i.e. added
			// to the bottom of the list.
			values = new ContentValues();
			values.put(ListsTable.COL_MANUAL_SORT_ORDER, newListID);

			// Check if newMasterListItem has previously used a category
			projection = new String[] { PreviousCategoryTable.COL_CATEGORY_ID };
			selection = PreviousCategoryTable.COL_LIST_TITLE_ID + " = ? AND "
					+ PreviousCategoryTable.COL_MASTER_LIST_ITEM_ID + " = ?";
			Uri uri = PreviousCategoryTable.CONTENT_URI;
			Cursor previousCategoryCursor = cr.query(uri, projection,
					selection, selectionArgs, null);

			if (previousCategoryCursor.getCount() > 0) {
				// previously used category found
				Long previousCategoryID = previousCategoryCursor
						.getLong(previousCategoryCursor
								.getColumnIndexOrThrow(PreviousCategoryTable.COL_CATEGORY_ID));
				values.put(ListsTable.COL_CATEGORY_ID, previousCategoryID);
			}

			// update Manual Sort Order and Category of the newly created List
			// row
			cr.update(newListUri, values, null, null);
			AListUtilities.closeQuietly(previousCategoryCursor);

			// indicate that newMasterListItem is in the active list
			uri = Uri.withAppendedPath(MasterListItemsTable.CONTENT_URI,
					String.valueOf(newMasterListItemID));
			values = new ContentValues();
			values.put(MasterListItemsTable.COL_SELECTED,
					MasterListItemsTable.SELECTED_TRUE);
			cr.update(uri, values, null, null);

			if (verbose) {
				msg = "\"" + newMasterListItem + "\""
						+ " added to the active List.";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
			}
		}

	} // End AddItemToActiveList

	private void RemoveItemFromActiveList(String masterListItem,
			long masterListItemID) {
		String msg = null;
		ContentResolver cr = getContentResolver();

		if (this.activeListID < 0) {
			msg = "No List selected.\nPlease create or select a List before adding items.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();
			return;
		}

		int numberOfRowsDeleted = ListsTable.RemoveItem(this,
				this.activeListID, masterListItemID);
		if (numberOfRowsDeleted != 1) {
			Log.e(TAG,
					"RemoveItemFromActiveList: Incorrect number of rows deleted from the ListsTable! /n"
							+ "Active List ID = "
							+ this.activeListID
							+ "; Master List Item = " + masterListItem);
		} else {
			// reset the "selected field" in the master List table for
			// newMasterListItem
			Uri uri = Uri.withAppendedPath(MasterListItemsTable.CONTENT_URI,
					String.valueOf(masterListItemID));
			ContentValues values = new ContentValues();
			values.put(MasterListItemsTable.COL_SELECTED,
					MasterListItemsTable.SELECTED_FALSE);
			cr.update(uri, values, null, null);
			if (verbose) {
				msg = "\"" + masterListItem
						+ "\" removed from the active list.";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private long AddItemToMasterList(String newMasterListItem) {

		ContentResolver cr = null;
		Cursor includedInMasterListItemsTableCursor = null;
		long newMasterListItemID = -1;
		try {
			cr = getContentResolver();
			includedInMasterListItemsTableCursor = cr.query(
					MasterListItemsTable.CONTENT_URI,
					new String[] { MasterListItemsTable.COL_ID },
					MasterListItemsTable.COL_ITEM_NAME + " = ?",
					new String[] { newMasterListItem }, null);
		} catch (SQLiteException e1) {
			Log.e(TAG, "AddItemToMasterList: SQLiteException", e1);
			return -1;

		} catch (IllegalArgumentException e1) {
			Log.e(TAG, "AddItemToMasterList: IllegalArgumentException", e1);
			return -1;
		}

		if (includedInMasterListItemsTableCursor.getCount() > 0) {
			// the master list contains the proposed newMasterListItem
			newMasterListItemID = includedInMasterListItemsTableCursor
					.getLong(includedInMasterListItemsTableCursor
							.getColumnIndexOrThrow(MasterListItemsTable.COL_ID));

			if (verbose) {
				if (!autoAddItem) {
					String msg = "\"" + newMasterListItem
							+ "\" already exists in the database!";
					Toast.makeText(getApplicationContext(), msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			// the proposed newMasterListItem is not in the master list ... so
			// add it
			try {
				ContentValues values = new ContentValues();
				values.put(MasterListItemsTable.COL_ITEM_NAME,
						newMasterListItem);
				Uri newMasterListItemURI = cr.insert(
						MasterListItemsTable.CONTENT_URI, values);
				newMasterListItemID = Long.parseLong(newMasterListItemURI
						.getLastPathSegment());

				if (verbose) {
					String msg = "\"" + newMasterListItem
							+ "\" added to the master list.";
					Toast.makeText(getApplicationContext(), msg,
							Toast.LENGTH_SHORT).show();
				}
			} catch (SQLiteException e) {
				Log.e(TAG, "AddItemToMasterList: SQLiteException", e);
				return -1;
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "AddItemToMasterList: IllegalArgumentException", e);
				return -1;
			}
		}
		AListUtilities.closeQuietly(includedInMasterListItemsTableCursor);
		return newMasterListItemID;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "onCreateLoader. id = " + id);

		CursorLoader cursorLoader = null;
		Uri uri = null;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;

		switch (id) {
		case MASTER_LIST_LOADER_ID:
			// Create a new master list CursorLoader with the following query
			// parameters.
			uri = MasterListItemsTable.CONTENT_URI;
			projection = MasterListItemsTable.PROJECTION_ALL;

			switch (masterListSortOrder) {
			case SORT_ORDER_ALPHABETICAL:
				sortOrder = MasterListItemsTable.SORT_ORDER_ITEM_NAME;
				break;

			case SORT_ORDER_SELECTED_AT_BOTTOM:
				sortOrder = MasterListItemsTable.SORT_ORDER_SELECTED_ASC;
				break;

			case SORT_ORDER_SELECTED_AT_TOP:
				sortOrder = MasterListItemsTable.SORT_ORDER_SELECTED_DESC;
				break;
			}
			// filter the cursor based on user typed text in txtListItem
			String masterListItem = txtListItem.getText().toString().trim();
			if (!masterListItem.isEmpty()) {
				selection = MasterListItemsTable.COL_ITEM_NAME + " Like '%"
						+ masterListItem + "%'";
			}

			try {
				cursorLoader = new CursorLoader(MasterListActivity.this, uri,
						projection, selection, selectionArgs, sortOrder);

			} catch (SQLiteException e) {
				Log.e(TAG, "onCreateLoader: SQLiteException", e);
				return null;

			} catch (IllegalArgumentException e) {
				Log.e(TAG, "onCreateLoader: IllegalArgumentException", e);
				return null;
			}
			break;

		case LIST_TITLES_LOADER_ID:
			// Create a new list titles CursorLoader with the following query
			// parameters.
			uri = ListTitlesTable.CONTENT_URI;
			projection = ListTitlesTable.PROJECTION_ALL;
			sortOrder = ListTitlesTable.SORT_ORDER_LIST_TITLE;

			try {
				cursorLoader = new CursorLoader(MasterListActivity.this, uri,
						projection, selection, selectionArgs, sortOrder);

			} catch (SQLiteException e) {
				Log.e(TAG, "onCreateLoader: SQLiteException", e);
				return null;

			} catch (IllegalArgumentException e) {
				Log.e(TAG, "onCreateLoader: IllegalArgumentException", e);
				return null;
			}
			break;
		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		if (L) {
			Log.i(TAG, "onLoadFinished. loader id = " + loader.getId());
		}
		// The asynchronous load is complete and the newCursor is now available
		// for use.
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case MASTER_LIST_LOADER_ID:
			masterListAdapter.swapCursor(newCursor);

			if (flagFirstTimeThruMasterListLoader) {
				masterListView.setSelectionFromTop(
						masterListViewFirstVisiblePosition, masterListViewTop);
				flagFirstTimeThruMasterListLoader = false;
			} else {
				masterListView.setSelectionFromTop(0, 0);
			}
			break;

		case LIST_TITLES_LOADER_ID:
			listTitlesAdapter.swapCursor(newCursor);
			if (newCursor.getCount() > 0) {
				layoutListTitle.setVisibility(View.VISIBLE);
			}

			if (flagFirstTimeThruListTitlesLoader) {
				if (spnListTitlesPosition > -1) {
					spnListTitles.setSelection(spnListTitlesPosition);
				}
				flagFirstTimeThruListTitlesLoader = false;
			} else {
				spnListTitles.setSelection(AListUtilities.getIndex(
						spnListTitles, this.activeListID));
			}
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (L) {
			Log.i(TAG, "onLoaderReset. loader id = " + loader.getId());
		}

		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with a null
		// Cursor.
		switch (loader.getId()) {
		case MASTER_LIST_LOADER_ID:
			masterListAdapter.swapCursor(null);
			break;

		case LIST_TITLES_LOADER_ID:
			listTitlesAdapter.swapCursor(null);
			break;
		}
	}

	private Cursor getMasterListItem(long masterListItemID, String[] projection) {
		Cursor cursor = null;

		try {
			ContentResolver cr = getContentResolver();
			Uri uri = Uri.withAppendedPath(MasterListItemsTable.CONTENT_URI,
					String.valueOf(masterListItemID));
			if (null == projection) {
				projection = MasterListItemsTable.PROJECTION_ALL;
			}
			cursor = cr.query(uri, projection, null, null, null);
			return cursor;

		} catch (SQLiteException e) {
			Log.e(TAG, "getMasterListItem: SQLiteException", e);
			return null;

		} catch (IllegalArgumentException e) {
			Log.e(TAG, "getMasterListItem: IllegalArgumentException", e);
			return null;
		}
	}

	public void ShowMasterListSortOrderSelectionPopUp() {
		// Items you would like to list as options.
		final String[] items = { getString(R.string.sort_alphabetical),
				getString(R.string.sort_selected_at_bottom),
				getString(R.string.sort_selected_at_top) };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MasterListActivity.this);
		builder.setTitle(getString(R.string.sort_dialog_title));
		builder.setSingleChoiceItems(items, masterListSortOrder,
				new DialogInterface.OnClickListener() {
					// When you click the radio button
					@Override
					public void onClick(DialogInterface dialog, int item) {
						masterListSortOrder = item;
					}
				});

		builder.setPositiveButton(getString(R.string.set_sort_order),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						loaderManager.restartLoader(MASTER_LIST_LOADER_ID,
								null, masterListCallbacks);
					}
				});

		builder.show();
	}

	// /////////////////////////////////////////////////////////////////////////////
	// List code here
	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds a new List to ListTitlesTable.
	 */
	private void AddNewList() {
		// Show a dialog so the user can input the new List's Title.
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.newListTitlePrompt));

		// Set an EditText view to get user input
		final EditText txtNewListTitleInput = new EditText(this);
		txtNewListTitleInput
				.setHint(getString(R.string.txtNewListTitleInputHint));
		txtNewListTitleInput.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
				| InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		alert.setView(txtNewListTitleInput);

		alert.setPositiveButton(getString(R.string.btnCreateNewList),
				new DialogInterface.OnClickListener() {

					@SuppressWarnings("resource")
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {

						String newListTitle = txtNewListTitleInput.getText()
								.toString().trim();
						// validate the String input
						if (null != newListTitle && newListTitle.length() > 0) {
							// create a new list
							ContentResolver cr = getContentResolver();

							// check if the newListTitle is already in
							// ListTitlesTable
							Cursor includedInListTitlesTableCursor = null;
							try {
								includedInListTitlesTableCursor = cr
										.query(ListTitlesTable.CONTENT_URI,
												new String[] { ListTitlesTable.COL_ID },
												ListTitlesTable.COL_LIST_TITLE
														+ " = ?",
												new String[] { newListTitle },
												null);
							} catch (Exception e) {
								Log.e(TAG, "AddNewList: Exception in query.", e);
							}

							if (includedInListTitlesTableCursor != null
									&& includedInListTitlesTableCursor
											.getCount() > 0) {
								// newListTitle already in ListTitlesTable
								// and make it active
								includedInListTitlesTableCursor.moveToFirst();
								activeListID = includedInListTitlesTableCursor
										.getLong(includedInListTitlesTableCursor
												.getColumnIndexOrThrow(ListTitlesTable.COL_ID));
								spnListTitles.setSelection(AListUtilities
										.getIndex(spnListTitles, activeListID));

								if (verbose) {
									String msg = "\""
											+ newListTitle
											+ "\" already exists in the database!";
									Toast.makeText(getApplicationContext(),
											msg, Toast.LENGTH_SHORT).show();
								}

							} else {
								// newListTitle not in ListTitlesTable .. so add
								// it.
								Uri uri = ListTitlesTable.CONTENT_URI;
								ContentValues values = new ContentValues();
								values.put(ListTitlesTable.COL_LIST_TITLE,
										newListTitle);
								Uri activeListUri = cr.insert(uri, values);
								activeListID = Long.parseLong(activeListUri
										.getLastPathSegment());

								if (verbose) {
									String msg = "List: " + "\"" + newListTitle
											+ "\"" + " added to the database.";
									Toast.makeText(getApplicationContext(),
											msg, Toast.LENGTH_SHORT).show();
								}
							}
							AListUtilities
									.closeQuietly(includedInListTitlesTableCursor);
						}

						else {
							String msg = "No list title provided!/nPlease try creating a new list again.";
							Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_SHORT).show();
						}

					}

				});

		alert.setNegativeButton(getString(R.string.Cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						if (verbose) {
							String msg = "Creating new list CANCELED.";
							Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		alert.show();
	}

	private void ActivateList(long listTitleID) {
		try {
			this.activeListID = listTitleID;
			SetLayoutBackgroundColor(listTitleID);
			MasterListItemsTable.ResetSelectedColumn(this);
			MasterListItemsTable.SetSelectedColumn(this, listTitleID);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in ActivateList. ", e);
		}
	}

	private void SetLayoutBackgroundColor(long listTitleID) {
		try {
			@SuppressWarnings("resource")
			Cursor listTitlesCursor = ListsTable.getListTitlesCurosr(this,
					listTitleID);
			if (listTitlesCursor != null) {
				String strBackgroundColor = listTitlesCursor
						.getString(listTitlesCursor
								.getColumnIndexOrThrow(ListTitlesTable.COL_BACKGROUND_COLOR));
				spnListTitlesBackgroundColor = AListUtilities
						.GetColorInt(strBackgroundColor);

				String strListTitlesTextColor = listTitlesCursor
						.getString(listTitlesCursor
								.getColumnIndexOrThrow(ListTitlesTable.COL_NORMAL_TEXT_COLOR));
				spnListTitlesTextColor = AListUtilities
						.GetColorInt(strListTitlesTextColor);

				btnStartListsViewActivity.setTextColor(spnListTitlesTextColor);
				layoutListTitle
						.setBackgroundColor(spnListTitlesBackgroundColor);

			}
			AListUtilities.closeQuietly(listTitlesCursor);
		} catch (Exception e) {
			Log.e(TAG,
					"An Exception error occurred in SetLayoutBackgroundColor. ",
					e);
		}
	}

	private void DeleteList(long listTitleID) {

		if (listTitleID < 0) {
			return;
		}

		// confirm user wants to delete the current active list.
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		String activeListTitle = ListsTable.getListTitle(this, listTitleID);
		String deleteListPrompt = "Delete list: " + "\"" + activeListTitle
				+ "\" ?";

		alert.setTitle(deleteListPrompt);

		alert.setPositiveButton(getString(R.string.OK),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						String activeListTitle = ListsTable.getListTitle(
								getBaseContext(), activeListID);
						doDeleteList(activeListID);
						String deleteListPrompt = "List: " + "\""
								+ activeListTitle + "\" DELETED.";
						Toast.makeText(getApplicationContext(),
								deleteListPrompt, Toast.LENGTH_SHORT).show();
					}
				});

		alert.setNegativeButton(getString(R.string.Cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						String activeListTitle = ListsTable.getListTitle(
								getBaseContext(), activeListID);
						String cancelDeleteListPrompt = "Deleting of list: "
								+ "\"" + activeListTitle + "\" CANCELED.";
						Toast.makeText(getApplicationContext(),
								cancelDeleteListPrompt, Toast.LENGTH_SHORT)
								.show();
					}
				});

		alert.show();
	}

	private void doDeleteList(long listTitleID) {
		this.activeListID = ListsTable.FindNextListID(this, listTitleID);
		ListsTable.DeleteAllItems(this, listTitleID);
		ListsTable.DeleteAllPreviousCategoryItems(this, listTitleID);
		MasterListItemsTable.ResetSelectedColumn(this);
		ListsTable.DeleteListTitleItem(this, listTitleID);
	}

	/** Deletes the Active List from the database. */
	/*
	 * private void DeleteActiveList() {
	 * 
	 * final String activeListTitle = db.getListTitle(this.activeListID); final
	 * String msgQuestion = "Delete list: " +"\"" + activeListTitle + "\" ?";
	 * final String msgDeletedResult = "List: " +"\"" + activeListTitle +"\"" +
	 * " deleted."; final String msgDeleteCancled = "Deletion of List: " +"\"" +
	 * activeListTitle +"\"" + " cancled."; if(verbose) { // Verify that the
	 * user wants to delete the Active List AlertDialog.Builder alert = new
	 * AlertDialog.Builder(this);
	 * 
	 * alert.setTitle(msgQuestion);
	 * 
	 * alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) {
	 * if(db.deleteList(this.activeListID)) { showFirstList();
	 * Toast.makeText(getApplicationContext(), msgDeletedResult,
	 * Toast.LENGTH_SHORT).show(); } } });
	 * 
	 * alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) { //
	 * Canceled. Toast.makeText(getApplicationContext(), msgDeleteCancled,
	 * Toast.LENGTH_SHORT).show(); } });
	 * 
	 * alert.show(); } else { if (db.deleteList(this.activeListID)) {
	 * Toast.makeText(getApplicationContext(), msgDeletedResult,
	 * Toast.LENGTH_SHORT).show(); } } }
	 */

	private void TestInserts() {
		// muliti row Uris

		ContentResolver cr = getContentResolver();
		ContentValues values = null;
		int i = 1;

		do {
			values = new ContentValues();
			values.put(MasterListItemsTable.COL_ITEM_NAME, "MasterListItem "
					+ String.valueOf(i));
			cr.insert(MasterListItemsTable.CONTENT_URI, values);

			values = new ContentValues();
			values.put(ListTitlesTable.COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT, i);
			values.put(ListTitlesTable.COL_BACKGROUND_COLOR,
					"Background Color " + String.valueOf(i));
			values.put(ListTitlesTable.COL_NORMAL_TEXT_COLOR,
					"Normal Text Color " + String.valueOf(i));
			values.put(ListTitlesTable.COL_STRIKEOUT_TEXT_COLOR,
					"Strikeout Color " + String.valueOf(i));
			values.put(ListTitlesTable.COL_LIST_TITLE,
					"List Title " + String.valueOf(i));
			values.put(ListTitlesTable.COL_SORT_ORDER_ID, i);
			cr.insert(ListTitlesTable.CONTENT_URI, values);

			values = new ContentValues();
			values.put(ListsTable.COL_CATEGORY_ID, i);
			values.put(ListsTable.COL_LIST_TITLE_ID, i);
			values.put(ListsTable.COL_MASTER_LIST_ITEM_ID, i);
			values.put(ListsTable.COL_MANUAL_SORT_ORDER, i);
			values.put(ListsTable.COL_STRUCK_OUT, i);
			cr.insert(ListsTable.CONTENT_URI, values);

			values = new ContentValues();
			values.put(CategoriesTable.COL_CATEGORY,
					"Category " + String.valueOf(i));
			cr.insert(CategoriesTable.CONTENT_URI, values);

			values = new ContentValues();
			values.put(PreviousCategoryTable.COL_CATEGORY_ID, i);
			values.put(PreviousCategoryTable.COL_LIST_TITLE_ID, i);
			values.put(PreviousCategoryTable.COL_MASTER_LIST_ITEM_ID, i);
			cr.insert(PreviousCategoryTable.CONTENT_URI, values);

			values = new ContentValues();
			values.put(SortOrdersTable.COL_SORT_ORDER_NAME, "Sort Order Name "
					+ String.valueOf(i));
			values.put(SortOrdersTable.COL_SORT_ORDER_FIELD,
					"Sort Order Field " + String.valueOf(i));
			cr.insert(SortOrdersTable.CONTENT_URI, values);

			i++;
		} while (i < 5);
	}

	private void TestUpDates() {
		TestInserts();
		ContentResolver cr = getContentResolver();
		ContentValues values = null;
		int i = 1;

		do {
			values = new ContentValues();
			values.put(MasterListItemsTable.COL_ITEM_NAME, "MasterListItem "
					+ String.valueOf(i + 5));
			cr.update(Uri.withAppendedPath(MasterListItemsTable.CONTENT_URI,
					String.valueOf(i)), values, null, null);

			values = new ContentValues();
			values.put(ListTitlesTable.COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT,
					i + 5);
			values.put(ListTitlesTable.COL_BACKGROUND_COLOR,
					"Background Color " + String.valueOf(i + 5));
			values.put(ListTitlesTable.COL_NORMAL_TEXT_COLOR,
					"Normal Text Color " + String.valueOf(i + 5));
			values.put(ListTitlesTable.COL_STRIKEOUT_TEXT_COLOR,
					"Strikeout Color " + String.valueOf(i + 5));
			values.put(ListTitlesTable.COL_LIST_TITLE,
					"List Title " + String.valueOf(i + 5));
			values.put(ListTitlesTable.COL_SORT_ORDER_ID, i + 5);
			cr.update(
					Uri.withAppendedPath(ListTitlesTable.CONTENT_URI,
							String.valueOf(i)), values, null, null);

			values = new ContentValues();
			values.put(ListsTable.COL_CATEGORY_ID, i + 5);
			values.put(ListsTable.COL_LIST_TITLE_ID, i + 5);
			values.put(ListsTable.COL_MASTER_LIST_ITEM_ID, i + 5);
			values.put(ListsTable.COL_MANUAL_SORT_ORDER, i + 5);
			values.put(ListsTable.COL_STRUCK_OUT, i + 5);
			cr.update(
					Uri.withAppendedPath(ListsTable.CONTENT_URI,
							String.valueOf(i)), values, null, null);

			values = new ContentValues();
			values.put(CategoriesTable.COL_CATEGORY,
					"Category " + String.valueOf(i + 5));
			cr.update(
					Uri.withAppendedPath(CategoriesTable.CONTENT_URI,
							String.valueOf(i)), values, null, null);

			values = new ContentValues();
			values.put(PreviousCategoryTable.COL_CATEGORY_ID, i + 5);
			values.put(PreviousCategoryTable.COL_LIST_TITLE_ID, i + 5);
			values.put(PreviousCategoryTable.COL_MASTER_LIST_ITEM_ID, i + 5);
			cr.update(Uri.withAppendedPath(PreviousCategoryTable.CONTENT_URI,
					String.valueOf(i)), values, null, null);

			values = new ContentValues();
			values.put(SortOrdersTable.COL_SORT_ORDER_NAME, "Sort Order Name "
					+ String.valueOf(i + 5));
			values.put(SortOrdersTable.COL_SORT_ORDER_FIELD,
					"Sort Order Field " + String.valueOf(i + 5));
			cr.update(
					Uri.withAppendedPath(SortOrdersTable.CONTENT_URI,
							String.valueOf(i)), values, null, null);

			i++;

		} while (i < 5);

		i++;
		String where = null;
		String[] whereArgs = null;

		values = new ContentValues();
		values.put(MasterListItemsTable.COL_ITEM_NAME, "MasterListItem "
				+ String.valueOf(i + 5));
		where = MasterListItemsTable.COL_ITEM_NAME
				+ " = 'MasterListItem 6' OR "
				+ MasterListItemsTable.COL_ITEM_NAME
				+ " = 'MasterListItem 7' OR "
				+ MasterListItemsTable.COL_ITEM_NAME + " = 'MasterListItem 8'";
		/* whereArgs = new String[] {"6", "7", "8"}; */
		cr.update(MasterListItemsTable.CONTENT_URI, values, where, whereArgs);

		values = new ContentValues();
		values.put(ListTitlesTable.COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT, i + 5);
		values.put(ListTitlesTable.COL_BACKGROUND_COLOR, "Background Color "
				+ String.valueOf(i + 5));
		values.put(ListTitlesTable.COL_NORMAL_TEXT_COLOR, "Normal Text Color "
				+ String.valueOf(i + 5));
		values.put(ListTitlesTable.COL_STRIKEOUT_TEXT_COLOR, "Strikeout Color "
				+ String.valueOf(i + 5));
		values.put(ListTitlesTable.COL_LIST_TITLE,
				"List Title " + String.valueOf(i + 5));
		values.put(ListTitlesTable.COL_SORT_ORDER_ID, i + 5);

		where = ListTitlesTable.COL_SORT_ORDER_ID + " = ? OR "
				+ ListTitlesTable.COL_SORT_ORDER_ID + " = ?";
		whereArgs = new String[] { "6", "7" };
		cr.update(ListTitlesTable.CONTENT_URI, values, where, whereArgs);
	}
}
