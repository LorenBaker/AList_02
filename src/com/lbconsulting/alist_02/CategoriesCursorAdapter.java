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

import com.lbconsulting.alist_02.database.CategoriesTable;

public class CategoriesCursorAdapter extends CursorAdapter {
	/*public Context context;
	public LayoutInflater inflater;*/

	/*
	 * public ListTitlesCursorAdapter(Context context, int layout, Cursor c,
	 * String[] from, int[] to, int flags) { super(context, layout, c, from, to,
	 * flags);
	 */
	public CategoriesCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		/*this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Resources res = context.getResources();
		TextView itemName = null;

		switch (view.getId()) {
		case R.id.categoriesLinearLayout:
			LinearLayout v = (LinearLayout) view;

			itemName = (TextView) view.findViewById(R.id.txtCategories);
			itemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(CategoriesTable.COL_CATEGORY_NAME)));
			itemName.setTypeface(null, Typeface.BOLD_ITALIC);
			itemName.setTextColor(res.getColor(R.color.black));

			/*int textColor = cursor.getInt(cursor.getColumnIndexOrThrow(ListTitlesTable.COL_NORMAL_TEXT_COLOR));
			int backgroundColor = cursor.getInt(cursor.getColumnIndexOrThrow(ListTitlesTable.COL_BACKGROUND_COLOR));

			itemName.setTextColor(textColor);
			itemName.setBackgroundColor(backgroundColor);
			v.setBackgroundColor(backgroundColor);*/

			break;

		case R.id.categoriesDropdownLinearLayout:
			itemName = (TextView) view.findViewById(R.id.txtCategoriesDropdown);
			itemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(CategoriesTable.COL_CATEGORY_NAME)));
			itemName.setTypeface(null, Typeface.ITALIC);
			itemName.setTextColor(res.getColor(R.color.black));
			break;
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.categories_row, parent, false);
		return v;
	}

	@Override
	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		super.newDropDownView(context, cursor, parent);
		Resources res = context.getResources();
		View v = View.inflate(context, R.layout.categories_dropdown_row, null);
		return v;
	}
}
