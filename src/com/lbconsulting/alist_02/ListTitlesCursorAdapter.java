package com.lbconsulting.alist_02;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lbconsulting.alist_02.database.ListTitlesTable;

public class ListTitlesCursorAdapter extends CursorAdapter {
	public Context context;
	public LayoutInflater inflater;

	/*
	 * public ListTitlesCursorAdapter(Context context, int layout, Cursor c,
	 * String[] from, int[] to, int flags) { super(context, layout, c, from, to,
	 * flags);
	 */
	public ListTitlesCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Resources res = context.getResources();
		TextView itemName = null;

		switch (view.getId()) {
		case R.id.listTitlesLinearLayout:
			LinearLayout v = (LinearLayout) view;

			itemName = (TextView) view.findViewById(R.id.txtListTitle);
			itemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TITLE)));
			itemName.setTypeface(null, Typeface.BOLD_ITALIC);

			String strTextColor = cursor.getString(cursor.getColumnIndexOrThrow(ListTitlesTable.COL_NORMAL_TEXT_COLOR));
			String strBackgroundColor = cursor.getString(cursor
					.getColumnIndexOrThrow(ListTitlesTable.COL_BACKGROUND_COLOR));

			int textColor = AListUtilities.GetColorInt(strTextColor);
			int backgroundColor = AListUtilities.GetColorInt(strBackgroundColor);

			itemName.setTextColor(textColor);
			itemName.setBackgroundColor(backgroundColor);
			v.setBackgroundColor(backgroundColor);

			break;

		case R.id.listTitlesDropdownLinearLayout:
			itemName = (TextView) view.findViewById(R.id.txtListTitleDropdown);
			itemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_TITLE)));
			itemName.setTypeface(null, Typeface.ITALIC);
			itemName.setTextColor(res.getColor(R.color.black));
			break;
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.list_titles_row, parent, false);
		return v;
	}

	@Override
	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		super.newDropDownView(context, cursor, parent);
		Resources res = context.getResources();
		View v = View.inflate(context, R.layout.list_titles_dropdown_row, null);
		return v;
	}
}
