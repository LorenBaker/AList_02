package com.lbconsulting.alist_02.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lbconsulting.alist_02.R;
import com.lbconsulting.alist_02.database.CategoriesTable;
import com.lbconsulting.alist_02.database.ListsTable;
import com.lbconsulting.alist_02.database.MasterListItemsTable;

public class ListsCursorAdapter extends CursorAdapter {
	public Context context;

	private boolean colorsSet = false;
	private int backgroundColor;
	private int normalTextColor;
	private int strikeoutTextColor;
	private boolean showCategories = false;

	public ListsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		this.context = context;
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {

		TextView txtListItemName = (TextView) view.findViewById(R.id.txtListItemName);
		txtListItemName.setBackgroundColor(this.backgroundColor);
		txtListItemName.setText(cursor.getString(cursor.getColumnIndex(MasterListItemsTable.COL_ITEM_NAME)));
		int isStruckOut = cursor.getInt(cursor.getColumnIndex(ListsTable.COL_STRUCK_OUT));
		if (isStruckOut > 0) {
			// item has been struck out
			txtListItemName.setTypeface(null, Typeface.ITALIC);
			txtListItemName.setTextColor(this.strikeoutTextColor);
			txtListItemName.setPaintFlags(txtListItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			// item is NOT struck out
			txtListItemName.setTypeface(null, Typeface.NORMAL);
			txtListItemName.setTextColor(this.normalTextColor);
		}

		if (this.showCategories) {
			TextView txtListItemCategory = (TextView) view.findViewById(R.id.txtListItemCategory);
			txtListItemCategory.setBackgroundColor(this.backgroundColor);
			String categoryText =
					"(" + cursor.getString(cursor.getColumnIndex(CategoriesTable.COL_CATEGORY_NAME)) + ")";
			txtListItemCategory.setText(categoryText);
			if (isStruckOut > 0) {
				// item has been struck out
				txtListItemCategory.setTypeface(null, Typeface.ITALIC);
				txtListItemCategory.setTextColor(this.strikeoutTextColor);
				//txtListItemCategory.setPaintFlags(txtListItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				// item is NOT struck out
				txtListItemCategory.setTypeface(null, Typeface.NORMAL);
				txtListItemCategory.setTextColor(this.normalTextColor);
			}
		}
	}

	@Override
	public View newView(Context c, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View v = inflater.inflate(R.layout.list_items_row, parent, false);
		bindView(v, c, cursor);
		return v;
	}

	public void setColors(int backgroundColor, int normalTextColor, int strikeoutTextColor) {
		this.backgroundColor = backgroundColor;
		this.normalTextColor = normalTextColor;
		this.strikeoutTextColor = strikeoutTextColor;
		this.colorsSet = true;
	}

	public void setShowCategories(boolean showCategoriesValue) {
		this.showCategories = showCategoriesValue;
	}

}
