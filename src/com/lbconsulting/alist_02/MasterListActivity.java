package com.lbconsulting.alist_02;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.lbconsulting.alist_02.adapters.ListTitlesCursorAdapter;
import com.lbconsulting.alist_02.adapters.ListTypesCursorAdapter;
import com.lbconsulting.alist_02.adapters.MasterListCursorAdapter;
import com.lbconsulting.alist_02.database.ListTitlesTable;
import com.lbconsulting.alist_02.database.ListTypesTable;
import com.lbconsulting.alist_02.database.ListsTable;
import com.lbconsulting.alist_02.database.MasterListItemsTable;
import com.lbconsulting.alist_02.database.PreviousCategoryTable;

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
	private final boolean autoAddItem = true;
	private boolean verbose = false;
	public long activeListID = -1;
	public long activeListTypeID = -1;
	private String activeListTypeName = "";
	private int masterListSortOrder = SORT_ORDER_ALPHABETICAL;
	private int masterListViewFirstVisiblePosition = 0;
	private int masterListViewTop = 0;
	private int numberOfSelectedItems = 0;

	private boolean flagFirstTimeThruListTitlesLoader = true;
	private final int spnListTitlesPosition = -1;

	// AList module variables
	private LoaderManager loaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> masterListCallbacks;

	private ListView masterListView = null;
	private MasterListCursorAdapter masterListAdapter;
	private ListTitlesCursorAdapter listTitlesAdapter;
	private ListTypesCursorAdapter listTypesAdapter;

	private static Button btnAddToMasterList = null;
	private static Button btnShowListsActivity = null;
	private EditText txtListItem = null;
	private static Spinner spnListTitles = null;
	private static LinearLayout layoutListTitle = null;
	private static LinearLayout layoutListItem = null;
	// private static TextView txtStartingHint = null;

	private ActionBar actionBar = null;
	private long proposedListTypeId;

	/*private static LinearLayout layoutListType = null;*/

	// /////////////////////////////////////////////////////////////////////////////
	// MasterListActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (L)
			Log.i(TAG, "MasterListActivity onCreate Starting.");

		/*this.flagFirstTimeThruMasterListLoader = true;*/
		this.flagFirstTimeThruListTitlesLoader = true;
		// To keep this method simple place onCreate code at doCreate
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

		// Get the between instance stored values
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);

		// Set application states
		/*this.autoAddItem = storedStates.getBoolean("autoAddItem", true);*/
		this.verbose = storedStates.getBoolean("verbose", false);
		this.activeListID = storedStates.getLong("activeListID", -1);
		/*this.activeListTypeID = storedStates.getLong("activeListTypeID", -1);*/

		/*this.masterListSortOrder = storedStates.getInt("masterListSortOrder", this.SORT_ORDER_ALPHABETICAL);*/
		/*this.txtListItem.setText(storedStates.getString("txtListItem", null));*/

		/*this.masterListViewFirstVisiblePosition = storedStates.getInt("masterListViewFirstVisiblePosition", 0);
		this.masterListViewTop = storedStates.getInt("masterListViewTop", 0);*/

		/*this.spnListTitlesPosition = storedStates.getInt("spnListTitlesPosition", -1);*/

		if (this.activeListID > 0) {
			this.ActivateList(this.activeListID);
		} else {
			this.AddNewList();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "MasterListActivity onPause" + (isFinishing() ? " Finishing" : ""));

		// Store values between instances here
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		/*applicationStates.putBoolean("autoAddItem", autoAddItem);*/
		applicationStates.putBoolean("verbose", verbose);
		applicationStates.putLong("activeListID", this.activeListID);

		ListTitlesTable.setIntItem(this, activeListID, ListTitlesTable.COL_MASTERLIST_SORT_ORDER,
				this.masterListSortOrder);

		ListTitlesTable.setStringItem(this, activeListID, ListTitlesTable.COL_TXT_LIST_ITEM, txtListItem.getText()
				.toString().trim());

		ListTitlesTable.setIntItem(this, activeListID, ListTitlesTable.COL_MASTERLISTVIEW_FIRST_VISIBLE_POSITION,
				this.masterListView.getFirstVisiblePosition());

		View v = masterListView.getChildAt(0);
		this.masterListViewTop = (v == null) ? 0 : v.getTop();
		ListTitlesTable.setIntItem(this, activeListID, ListTitlesTable.COL_MASTERLISTVIEW_TOP, this.masterListViewTop);

		// Commit to storage
		applicationStates.commit();
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

		actionBar = getActionBar();
		actionBar.setTitle("AList");

		// Initialize the controls
		txtListItem = (EditText) findViewById(R.id.txtListItem);
		masterListView = (ListView) findViewById(R.id.lstItems);
		btnAddToMasterList = (Button) findViewById(R.id.btnAddToMasterList);
		btnShowListsActivity = (Button) findViewById(R.id.btnShowListsActivity);
		spnListTitles = (Spinner) findViewById(R.id.spnListTitle);
		layoutListTitle = (LinearLayout) findViewById(R.id.linearLayoutListTitle);
		layoutListItem = (LinearLayout) findViewById(R.id.linearLayoutListItem);
		// txtStartingHint = (TextView) findViewById(R.id.txtStartingHint);

		/*		if (this.activeListID < 1) {
					layoutListTitle.setVisibility(View.INVISIBLE);
					layoutListItem.setVisibility(View.INVISIBLE);
					//txtStartingHint.setVisibility(View.VISIBLE);
				}*/

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
					if (autoAddItem && activeListID > 0 && newMasterListItemID > 0) {
						AddItemToActiveList(newMasterListItem, newMasterListItemID);
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
				loaderManager.restartLoader(MASTER_LIST_LOADER_ID, null, masterListCallbacks);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Do nothing

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Do nothing

			}

		});

		// setup btnAddToMasterList
		btnAddToMasterList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String newMasterListItem = txtListItem.getText().toString().trim();
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
		btnShowListsActivity.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showListsActivityIntent = new Intent(MasterListActivity.this, ShowListsActivity.class);
				MasterListActivity.this.startActivity(showListsActivityIntent);
			}
		});

		// setup masterListView Listeners
		masterListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Cursor masterListItemCursor = getMasterListItem(id, null);
				if (masterListItemCursor != null) {
					masterListViewFirstVisiblePosition = masterListView.getFirstVisiblePosition();
					int isSelected = masterListItemCursor.getInt(masterListItemCursor
							.getColumnIndexOrThrow(MasterListItemsTable.COL_SELECTED));
					if (isSelected == 1) {
						// the clicked masterListItemCursor is already selected ... so
						// remove it from the active list.
						RemoveItemFromActiveList(masterListItemCursor.getString(masterListItemCursor
								.getColumnIndexOrThrow(MasterListItemsTable.COL_ITEM_NAME)), id);
						// TODO: find the items new position.
					} else {
						// the clicked masterListItemCursor has not been selected for the
						// active list ... so add it.
						if (view instanceof LinearLayout) {
							txtListItem.setText("");
							LinearLayout linearLayout = (LinearLayout) view;
							TextView txtView = (TextView) linearLayout.getChildAt(0);
							String newListItem = txtView.getText().toString();
							AddItemToActiveList(newListItem, id);
						}
					}
					masterListItemCursor.close();
				}
			}
		});

		masterListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
				EditOrDeleteMasterListItem(id);
				return true;
			}
		});

		// setup spnListTitles Listener
		spnListTitles.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				ActivateList(id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				layoutListTitle.setVisibility(View.INVISIBLE);
				layoutListItem.setVisibility(View.INVISIBLE);
				//txtStartingHint.setVisibility(View.VISIBLE);
				AddNewList();
				activeListID = -1;
			}

		});

		// set up masterListView adapter and loader
		loaderManager = getLoaderManager();

		masterListAdapter = new MasterListCursorAdapter(this, null, 0);
		masterListView.setAdapter(masterListAdapter);

		listTitlesAdapter = new ListTitlesCursorAdapter(this, null, 0);
		spnListTitles.setAdapter(listTitlesAdapter);

		/*listTypesAdapter = new ListTypesCursorAdapter(this, null, 0);*/
		/*spnListTypes.setAdapter(listTypesAdapter);*/

		masterListCallbacks = this;
		// Note: using null for the cursor. The masterList and listTitle cursors
		// loaded via onLoadFinished.
		loaderManager.initLoader(MASTER_LIST_LOADER_ID, null, masterListCallbacks);
		loaderManager.initLoader(LIST_TITLES_LOADER_ID, null, masterListCallbacks);
		//loaderManager.initLoader(LIST_TYPES_LOADER_ID, null, masterListCallbacks);
	} // End doCreate

	protected void EditOrDeleteMasterListItem(final long itemID) {
		// TODO create dialog giving the user the choice to: Delete or Edit the selected item, or Cancel

		AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
		inputDialog.setTitle("Edit or permanently delete item");

		final EditText txtItemName = new EditText(this);
		txtItemName.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		Cursor itemNameCursor = MasterListItemsTable.getMasterListItemsCursor(this, itemID);

		// get the active list info
		final int isSelected = itemNameCursor.getInt(itemNameCursor
				.getColumnIndexOrThrow(MasterListItemsTable.COL_SELECTED));
		String selectedListItemName = itemNameCursor.getString(itemNameCursor
				.getColumnIndexOrThrow(MasterListItemsTable.COL_ITEM_NAME));
		itemNameCursor.close();

		txtItemName.setText(selectedListItemName);

		inputDialog.setView(txtItemName);

		inputDialog.setPositiveButton("SAVE Item",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						String revisedItemName = txtItemName.getText().toString().trim();
						if (revisedItemName.length() > 0) {
							// update the selected item's name
							Uri uri = Uri.withAppendedPath(MasterListItemsTable.CONTENT_URI, String.valueOf(itemID));
							ContentValues values = new ContentValues();
							values.put(MasterListItemsTable.COL_ITEM_NAME, revisedItemName);
							String selection = null;
							String[] selectionArgs = null;
							ContentResolver cr = getContentResolver();
							cr.update(uri, values, selection, selectionArgs);
						}
					}
				});

		inputDialog.setNeutralButton("DELETE Item",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						MasterListItemsTable.DeleteItem(getBaseContext(), itemID);
						if (isSelected == 1) {
							numberOfSelectedItems--;
							setActionBarSubtitle(activeListTypeName, numberOfSelectedItems);
						}
					}
				});

		inputDialog.setNegativeButton(this.getString(R.string.Cancel), null);
		inputDialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.master_list, menu);
		if (L) {
			Log.i(TAG, "MasterListActivity onCreateOptionsMenu");
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
			this.EditActiveListTitle();
			return true;

		case R.id.deleteActiveList:
			DeleteList(this.activeListID);
			return true;

		case R.id.masterListSorting:
			ShowMasterListSortOrderSelectionPopUp();
			return true;

		case R.id.deleteAllMasterListItems:
			MasterListItemsTable.DeleteALLitems(this, activeListTypeID);
			setActionBarSubtitle(activeListTypeName, 0);
			return true;

		case R.id.about:
			// TODO code menu about
			// startActivity(new Intent(this, About.class));
			msg = "About under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			return true;

		case R.id.masterListSettings:
			// TODO code menu masterListSettings
			msg = "Master List Settings under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void EditActiveListTitle() {

		AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
		inputDialog.setTitle("Edit List Title:");

		final EditText txtRevisedListTitle = new EditText(this);
		txtRevisedListTitle.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		Cursor activeListTitleCursor = ListTitlesTable.getListTitlesCursor(this, activeListID);

		// get the active list title name
		String activeListTitleName = activeListTitleCursor.getString(activeListTitleCursor
				.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TITLE_NAME));
		activeListTitleCursor.close();
		txtRevisedListTitle.setText(activeListTitleName);

		inputDialog.setView(txtRevisedListTitle);

		inputDialog.setPositiveButton(this.getString(R.string.OK),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						String revisedListTitle = txtRevisedListTitle.getText().toString().trim();
						if (revisedListTitle.length() > 0) {
							// update the active list title name
							Uri uri = Uri.withAppendedPath(ListTitlesTable.CONTENT_URI, String.valueOf(activeListID));
							ContentValues values = new ContentValues();
							values.put(ListTitlesTable.COL_LIST_TITLE_NAME, revisedListTitle);
							String selection = null;
							String[] selectionArgs = null;
							ContentResolver cr = getContentResolver();
							cr.update(uri, values, selection, selectionArgs);
						}
					}
				});

		inputDialog.setNegativeButton(this.getString(R.string.Cancel), null);
		inputDialog.show();

	}

	private void AddItemToActiveList(String newMasterListItem, long newMasterListItemID) {
		String msg = "";

		if (this.activeListID < 0) {
			msg = "No List selected.\nPlease create or select a List before adding items.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			return;
		}

		// Check that masterListItemID is not already included in the ListsTable
		long listID = ListsTable.FindListID(this, this.activeListID, newMasterListItemID);
		if (listID > 0) {
			/*if (verbose) {
				msg = "\"" + newMasterListItem + "\"" + " is already included in the active list!";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}*/
		}
		else {
			// newMasterListItemID is not included in the active list ... so add it
			ContentResolver cr = this.getContentResolver();
			ContentValues values = new ContentValues();

			values.put(ListsTable.COL_LIST_TITLE_ID, this.activeListID);
			values.put(ListsTable.COL_MASTER_LIST_ITEM_ID, newMasterListItemID);

			Uri newListUri = cr.insert(ListsTable.CONTENT_URI, values);
			long newListItemID = Long.parseLong(newListUri.getLastPathSegment());

			// make the manual sort order equal to the newListID 
			// i.e. added to the bottom of the list.
			values = new ContentValues();
			values.put(ListsTable.COL_MANUAL_SORT_ORDER, newListItemID);

			// Check if newMasterListItem has previously used a category
			long previousCategoryID = PreviousCategoryTable.getPreviousCategoryID(this, this.activeListID,
					newMasterListItemID);
			if (previousCategoryID > 0) {
				// previously used category found
				values.put(ListsTable.COL_CATEGORY_ID, previousCategoryID);
			}

			// update Manual Sort Order and Category of the newly created List row
			cr.update(newListUri, values, null, null);

			// indicate that newMasterListItem is in the active list
			// update date_time last used
			Uri uri = Uri.withAppendedPath(MasterListItemsTable.CONTENT_URI, String.valueOf(newMasterListItemID));
			values = new ContentValues();
			values.put(MasterListItemsTable.COL_SELECTED, MasterListItemsTable.SELECTED_TRUE);
			long currentDateTime = Calendar.getInstance().getTimeInMillis();
			values.put(MasterListItemsTable.COL_DATE_TIME_LAST_USED, currentDateTime);
			cr.update(uri, values, null, null);

			this.numberOfSelectedItems++;
			this.setActionBarSubtitle(this.activeListTypeName, this.numberOfSelectedItems);

			if (verbose) {
				msg = "\"" + newMasterListItem + "\"" + " added to the active List.\nDate: "
						+ AListUtilities.formatDateTime(currentDateTime);
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}

	} // End AddItemToActiveList

	private void RemoveItemFromActiveList(String masterListItem,
			long masterListItemID) {
		String msg = null;
		ContentResolver cr = getContentResolver();

		if (this.activeListID < 0) {
			msg = "No List selected.\nPlease create or select a List before adding items.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			return;
		}

		int numberOfRowsDeleted = ListsTable.RemoveItem(this, this.activeListID, masterListItemID);
		if (numberOfRowsDeleted != 1) {
			Log.e(TAG,
					"RemoveItemFromActiveList: Incorrect number of rows deleted from the ListsTable! \n"
							+ "Active List ID = "
							+ this.activeListID
							+ "; Master List Item = " + masterListItem);
		}
		else {
			// reset the "selected field" in the master List table for
			// newMasterListItem
			Uri uri = Uri.withAppendedPath(MasterListItemsTable.CONTENT_URI, String.valueOf(masterListItemID));
			ContentValues values = new ContentValues();
			values.put(MasterListItemsTable.COL_SELECTED, MasterListItemsTable.SELECTED_FALSE);
			cr.update(uri, values, null, null);

			this.numberOfSelectedItems--;
			this.setActionBarSubtitle(this.activeListTypeName, this.numberOfSelectedItems);

			if (verbose) {
				msg = "\"" + masterListItem + "\" removed from the active list.";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private long AddItemToMasterList(String proposedMasterListItem) {

		long proposedMasterListItemID = MasterListItemsTable.FindMasterListItemID(
				this, proposedMasterListItem, this.activeListTypeID);
		if (proposedMasterListItemID > 0) {
			// the master list contains the proposed newMasterListItem
			if (verbose) {
				if (!autoAddItem) {
					String msg = "\"" + proposedMasterListItem + "\" already exists in the database!";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			// the master list does not contain the proposed newMasterListItem ... so add it

			// Verify that activeListTypeID is not less than 1. If so ... notify the user. 
			if (activeListTypeID < 1) {
				String title = "AList";
				String message = "Cannot create master list item " + "\"" + proposedMasterListItem + "\""
						+ ". No List Type provided.";
				int style = DialogBox.OK_ONLY;
				DialogBox msgBox = new DialogBox(this, title, message, style);
				msgBox.Show();
				return -1;
			}

			// add the proposedMasterListItem to the database
			ContentResolver cr = this.getContentResolver();
			ContentValues values = new ContentValues();
			values.put(MasterListItemsTable.COL_ITEM_NAME, proposedMasterListItem);
			values.put(MasterListItemsTable.COL_LIST_TYPE_ID, this.activeListTypeID);
			values.put(MasterListItemsTable.COL_DATE_TIME_LAST_USED, Calendar.getInstance().getTimeInMillis());

			Uri newMasterListItemURI = cr.insert(MasterListItemsTable.CONTENT_URI, values);
			proposedMasterListItemID = Long.parseLong(newMasterListItemURI.getLastPathSegment());

			if (verbose) {
				String msg = "\"" + proposedMasterListItem + "\" added to the master list.";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}
		return proposedMasterListItemID;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (L) {
			Log.i(TAG, "MasterListActivity onCreateLoader. id = " + id);
		}

		CursorLoader cursorLoader = null;
		Uri uri = null;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;

		switch (id) {
		case MASTER_LIST_LOADER_ID:
			// Create a new master list CursorLoader with the following query parameters.
			uri = MasterListItemsTable.CONTENT_URI;
			projection = MasterListItemsTable.PROJECTION_ALL;

			switch (this.masterListSortOrder) {
			case SORT_ORDER_ALPHABETICAL:
				sortOrder = MasterListItemsTable.SORT_ORDER_ITEM_NAME;
				break;

			case SORT_ORDER_SELECTED_AT_BOTTOM:
				sortOrder = MasterListItemsTable.SORT_ORDER_SELECTED_ASC;
				break;

			case SORT_ORDER_SELECTED_AT_TOP:
				sortOrder = MasterListItemsTable.SORT_ORDER_SELECTED_DESC;
				break;

			default:
				sortOrder = MasterListItemsTable.SORT_ORDER_ITEM_NAME;
				break;
			}
			// filter the cursor based on user typed text in txtListItem and the activeListTypeID
			String txtListItemText = txtListItem.getText().toString().trim();
			if (!txtListItemText.isEmpty()) {
				selection = MasterListItemsTable.COL_ITEM_NAME + " Like '%" + txtListItemText + "%'";
				/*if (this.activeListTypeID > 1) {*/
				selection = selection + " AND "
						+ MasterListItemsTable.COL_LIST_TYPE_ID + " = " + this.activeListTypeID;
				/*}*/
			}
			else {
				// txtListItemText is empty
				selection = MasterListItemsTable.COL_LIST_TYPE_ID + " = " + this.activeListTypeID;
			}

			try {
				cursorLoader = new CursorLoader(MasterListActivity.this, uri, projection, selection, selectionArgs,
						sortOrder);

			} catch (SQLiteException e) {
				Log.e(TAG, "MasterListActivity onCreateLoader: SQLiteException", e);
				return null;

			} catch (IllegalArgumentException e) {
				Log.e(TAG, "MasterListActivity onCreateLoader: IllegalArgumentException", e);
				return null;
			}
			break;

		case LIST_TITLES_LOADER_ID:
			// Create a new list titles CursorLoader with the following query parameters.
			uri = ListTitlesTable.CONTENT_URI;
			projection = ListTitlesTable.PROJECTION_ALL;
			sortOrder = ListTitlesTable.SORT_ORDER_LIST_TITLE;

			try {
				cursorLoader = new CursorLoader(MasterListActivity.this, uri, projection, selection, selectionArgs,
						sortOrder);

			} catch (SQLiteException e) {
				Log.e(TAG, "MasterListActivity onCreateLoader: SQLiteException", e);
				return null;

			} catch (IllegalArgumentException e) {
				Log.e(TAG, "MasterListActivity onCreateLoader: IllegalArgumentException", e);
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
			Log.i(TAG, "MasterListActivity onLoadFinished. loader id = " + loader.getId());
		}
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case MASTER_LIST_LOADER_ID:
			masterListAdapter.swapCursor(newCursor);
			masterListView.setSelectionFromTop(masterListViewFirstVisiblePosition, masterListViewTop);
			break;

		case LIST_TITLES_LOADER_ID:
			listTitlesAdapter.swapCursor(newCursor);
			spnListTitles.setSelection(AListUtilities.getIndex(spnListTitles, this.activeListID));

			/*			if (newCursor.getCount() > 0) {
							layoutListTitle.setVisibility(View.VISIBLE);
							layoutListItem.setVisibility(View.VISIBLE);
						}

						if (this.flagFirstTimeThruListTitlesLoader) {
							if (spnListTitlesPosition > -1) {
								spnListTitles.setSelection(this.spnListTitlesPosition);
							}
							this.flagFirstTimeThruListTitlesLoader = false;
						} else {
							spnListTitles.setSelection(AListUtilities.getIndex(spnListTitles, this.activeListID));
						}*/
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (L) {
			Log.i(TAG, "MasterListActivity onLoaderReset. loader id = " + loader.getId());
		}

		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with a null Cursor.
		switch (loader.getId()) {
		case MASTER_LIST_LOADER_ID:
			masterListAdapter.swapCursor(null);
			break;

		case LIST_TITLES_LOADER_ID:
			listTitlesAdapter.swapCursor(null);
			break;

		/*		case LIST_TYPES_LOADER_ID:
					listTypesAdapter.swapCursor(null);
					break;*/

		default:
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
			Log.e(TAG, "MasterListActivity getMasterListItem: SQLiteException", e);
			return null;

		} catch (IllegalArgumentException e) {
			Log.e(TAG, "MasterListActivity getMasterListItem: IllegalArgumentException", e);
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
						loaderManager.restartLoader(MASTER_LIST_LOADER_ID, null, masterListCallbacks);
					}
				});

		builder.show();
	}

	// /////////////////////////////////////////////////////////////////////////////
	// List code here
	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * Starts the CreatListActivity to allow the user to add a new List to
	 * ListTitlesTable.
	 */
	private void AddNewList() {
		Intent creatListActivityIntent = new Intent(MasterListActivity.this, CreatListActivity.class);
		MasterListActivity.this.startActivity(creatListActivityIntent);
	}

	private void ActivateList(long listID) {
		try {
			this.activeListID = listID;

			// TODO: get the activeListCursor to get all of these attributes to 
			// avoid successive cursor calls in the ListTitlesTable
			this.activeListTypeID = ListTitlesTable.getLongItem(this, listID, ListTitlesTable.COL_LIST_TYPE_ID);

			this.masterListSortOrder = ListTitlesTable.getIntItem(this, activeListID,
					ListTitlesTable.COL_MASTERLIST_SORT_ORDER);
			this.txtListItem.setText(ListTitlesTable.getStringItem(this, activeListID,
					ListTitlesTable.COL_TXT_LIST_ITEM));

			this.masterListViewFirstVisiblePosition = ListTitlesTable.getIntItem(this, activeListID,
					ListTitlesTable.COL_MASTERLISTVIEW_FIRST_VISIBLE_POSITION);
			this.masterListViewTop = ListTitlesTable.getIntItem(this, activeListID,
					ListTitlesTable.COL_MASTERLISTVIEW_TOP);

			this.SetLayoutBackgroundColor(listID);

			spnListTitles.setSelection(AListUtilities.getIndex(spnListTitles, this.activeListID));

			loaderManager.restartLoader(MASTER_LIST_LOADER_ID, null, masterListCallbacks);
			MasterListItemsTable.ResetSelectedColumn(this);
			this.numberOfSelectedItems = MasterListItemsTable.SetSelectedColumn(this, listID);
			this.activeListTypeName = ListTypesTable.getListTypeName(this, this.activeListTypeID);
			this.setActionBarSubtitle(this.activeListTypeName, this.numberOfSelectedItems);

		} catch (Exception e) {
			Log.e(TAG, "MasterListActivity: An Exception error occurred in ActivateList. ", e);
		}
	}

	private void setActionBarSubtitle(String listType, int numberOfItems) {
		if (numberOfItems > 0) {
			actionBar.setSubtitle(listType + " (" + AListUtilities.formatInt(numberOfItems) + ")");
		} else {
			actionBar.setSubtitle(listType);
		}

	}

	private void SetLayoutBackgroundColor(long listTitleID) {
		try {

			Cursor listTitlesCursor = ListsTable.getListTitlesCurosr(this, listTitleID);
			if (listTitlesCursor != null) {
				int spnListTitlesBackgroundColor = listTitlesCursor.getInt(listTitlesCursor
						.getColumnIndexOrThrow(ListTitlesTable.COL_BACKGROUND_COLOR));

				int spnListTitlesTextColor = listTitlesCursor.getInt(listTitlesCursor
						.getColumnIndexOrThrow(ListTitlesTable.COL_NORMAL_TEXT_COLOR));

				btnShowListsActivity.setTextColor(spnListTitlesTextColor);
				layoutListTitle.setBackgroundColor(spnListTitlesBackgroundColor);

			}
			if (listTitlesCursor != null) {
				listTitlesCursor.close();
			}
		} catch (Exception e) {
			Log.e(TAG,
					"An Exception error occurred in SetLayoutBackgroundColor. ",
					e);
		}
	}

	private void DeleteList(long listTitleID) {

		if (listTitleID < 1) {
			return;
		}

		// confirm user wants to delete the current active list.
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final String activeListTitle = ListsTable.getListTitle(this, listTitleID);
		String deleteListPrompt = "Delete list: " + "\"" + activeListTitle + "\" ?";

		alert.setTitle(deleteListPrompt);

		alert.setPositiveButton(getString(R.string.OK),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						long listIDforDeletion = activeListID;
						activeListID = ListsTable.FindNextListID(getBaseContext(), listIDforDeletion);
						ListTitlesTable.DeleteList(getBaseContext(), listIDforDeletion);
						String completeDeleteListPrompt = "List: " + "\"" + activeListTitle + "\" DELETED.";
						if (verbose) {
							Toast.makeText(getApplicationContext(), completeDeleteListPrompt, Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

		alert.setNegativeButton(getString(R.string.Cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						String cancelDeleteListPrompt = "Deleting of list: " + "\"" + activeListTitle + "\" CANCELED.";
						Toast.makeText(getApplicationContext(), cancelDeleteListPrompt, Toast.LENGTH_SHORT).show();
					}
				});

		alert.show();
	}
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

/*private void TestInserts() {
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
		values.put(ListTitlesTable.COL_LIST_TITLE_NAME,
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
		values.put(CategoriesTable.COL_CATEGORY_NAME,
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
		values.put(ListTitlesTable.COL_LIST_TITLE_NAME,
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
		values.put(CategoriesTable.COL_CATEGORY_NAME,
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
	 whereArgs = new String[] {"6", "7", "8"}; 
	cr.update(MasterListItemsTable.CONTENT_URI, values, where, whereArgs);

	values = new ContentValues();
	values.put(ListTitlesTable.COL_AUTO_ADD_CATEGORIES_ON_STRIKEOUT, i + 5);
	values.put(ListTitlesTable.COL_BACKGROUND_COLOR, "Background Color "
			+ String.valueOf(i + 5));
	values.put(ListTitlesTable.COL_NORMAL_TEXT_COLOR, "Normal Text Color "
			+ String.valueOf(i + 5));
	values.put(ListTitlesTable.COL_STRIKEOUT_TEXT_COLOR, "Strikeout Color "
			+ String.valueOf(i + 5));
	values.put(ListTitlesTable.COL_LIST_TITLE_NAME,
			"List Title " + String.valueOf(i + 5));
	values.put(ListTitlesTable.COL_SORT_ORDER_ID, i + 5);

	where = ListTitlesTable.COL_SORT_ORDER_ID + " = ? OR "
			+ ListTitlesTable.COL_SORT_ORDER_ID + " = ?";
	whereArgs = new String[] { "6", "7" };
	cr.update(ListTitlesTable.CONTENT_URI, values, where, whereArgs);
}
}
*/