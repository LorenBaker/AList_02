package com.lbconsulting.alist_02;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.widget.Spinner;

public class AListUtilities {
	/*
	 * public enum SortOrder { Alpha, Manual, Category }
	 */

	public static final String TAG = "AList";
	public static final Boolean L = true; // enable Logging

	public static int boolToInt(boolean b) {
		return b ? 1 : 0;
	}

	public static boolean intToBoolean(int value) {
		if (value == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static long boolToLong(boolean b) {
		return b ? 1 : 0;
	}

	public static boolean longToBoolean(long value) {
		if (value == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static void closeQuietly(Cursor cursor) {
		try {
			if (cursor != null) {
				cursor.close();
			}
		} catch (Exception e) {
			Log.e(TAG, "An error occurred closing Cursor. ", e);
		}
	}

	public static void closeQuietly(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			Log.e(TAG, "An error occurred closing connection. ", e);
		}
	}

	public static void closeQuietly(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			Log.e(TAG, "An error occurred closing statement. ", e);
		}
	}

	public static void closeQuietly(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			Log.e(TAG, "An error occurred closing result set. ", e);
		}
	}

	/**
	 * Execute all of the SQL statements in the ArrayList<String>.
	 * 
	 * @param db
	 *            The database on which to execute the statements.
	 * @param sqlStatements
	 *            An array of SQL statements to execute.
	 */
	public static void execMultipleSQL(SQLiteDatabase db,
			ArrayList<String> sqlStatements) {
		for (String statement : sqlStatements) {
			if (statement.trim().length() > 0) {
				db.execSQL(statement);
			}
		}
	}

	public static int GetColorInt(String ColorString) {
		int colorInt = 0;
		try {
			colorInt = Color.parseColor(ColorString);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in GetColorInt. ", e);
		}
		return colorInt;
	}

	public static String GetColorString(int ColorInt) {
		String colorString = null;
		try {
			colorString = String.format("#%06X", 0xFFFFFF & ColorInt);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in GetColorString. ", e);
		}
		return colorString;
	}

	public static int getIndex(Spinner spinner, long itemID) {
		Cursor spinnerItem = null;
		long spinnerItemID = -1;
		for (int i = 0; i < spinner.getCount(); i++) {
			spinnerItem = (Cursor) spinner.getItemAtPosition(i);
			spinnerItemID = spinnerItem.getLong(spinnerItem
					.getColumnIndexOrThrow("_id"));
			if (spinnerItemID == itemID) {
				/* closeQuietly(spinnerItem); */
				return i;
			}
		}
		// did not find the text in the spinner!
		closeQuietly(spinnerItem);
		return -1;
	}
}
