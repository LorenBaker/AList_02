package com.lbconsulting.alist_02.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.database.Cursor;
import android.text.TextUtils;

public class CSV {

	private static final String TAB = AciiToChar(9); // Tab
	private static final String LF = AciiToChar(10); // Linefeed
	private static final String CR = AciiToChar(13); // Carriage control
	private static final String QUALIFIER = AciiToChar(34); //Quotation mark
	private static final String DOUBLE_QUALIFIER = QUALIFIER + QUALIFIER; //Double Quotation mark
	private static final String DELIMITER = AciiToChar(44); //Comma

	private static String AciiToChar(int value) {
		String result = null;
		Character valueAsChar = null;
		if (value > -1 && value < 256) {
			valueAsChar = (char) value;
			result = Character.toString(valueAsChar);
		}
		return result;
	}

	public static ArrayList<String> CursorToCSV(Cursor cursor) {
		ArrayList<String> csvRecords = new ArrayList<String>();
		String record = "";

		// Iterate thru the cursor
		if (cursor.moveToFirst()) {
			do {
				record = getCSVRecordFromCursor(cursor);
				csvRecords.add(record);
			} while (cursor.moveToNext());
		}
		return csvRecords;
	}

	private static String getCSVRecordFromCursor(Cursor cursor) {
		String result = "";
		ArrayList<String> fields = new ArrayList<String>();
		String field = "";
		int columnType = 0;
		// assumes that the first field is _id
		// and not included in the csv string.
		if (cursor.getCount() > 1) {
			for (int i = 1; i < cursor.getCount(); i++) {
				field = "";
				columnType = cursor.getType(i);
				switch (columnType) {

				case Cursor.FIELD_TYPE_INTEGER:
					field = String.valueOf(cursor.getLong(i));
					break;

				case Cursor.FIELD_TYPE_FLOAT:
					field = Float.toString(cursor.getFloat(i));
					break;

				case Cursor.FIELD_TYPE_STRING:
					field = cursor.getString(i);
					if (field.contains(QUALIFIER)) {
						field.replace(QUALIFIER, DOUBLE_QUALIFIER);
						field = QUALIFIER + field + QUALIFIER;

					} else if (field.startsWith(" ") ||
							field.endsWith(" ") ||
							field.startsWith(TAB) ||
							field.endsWith(TAB) ||
							field.contains(LF) ||
							field.contains(CR)) {
						field = QUALIFIER + field + QUALIFIER;
					}
					break;
				default:
					break;
				}
				fields.add(field);
			}
		}
		result = TextUtils.join(DELIMITER, fields);
		return result;
	}

	public static ArrayList<ArrayList<String>> getRecordsFromFile(String filePath) throws Exception {
		ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
		String fileString = getStringFromFile(filePath);

		return records;
	}

	private static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	private static String getStringFromFile(String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		//Make sure you close all streams.
		fin.close();
		return ret;
	}
}
