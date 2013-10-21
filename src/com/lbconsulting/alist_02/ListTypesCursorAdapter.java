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

import com.lbconsulting.alist_02.database.ListTypesTable;

public class ListTypesCursorAdapter extends CursorAdapter {
	public Context context;
	public LayoutInflater inflater;

	public ListTypesCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context c, Cursor cursor) {
		Resources res = c.getResources();
		TextView listTypeTextView = null;

		switch (view.getId()) {
		case R.id.listTypesLinearLayout:
			LinearLayout v = (LinearLayout) view;

			listTypeTextView = (TextView) view.findViewById(R.id.txtListType);
			listTypeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListTypesTable.COL_LIST_TYPE)));
			listTypeTextView.setTypeface(null, Typeface.BOLD_ITALIC);
			listTypeTextView.setTextColor(res.getColor(R.color.black));

			/*			ColorDrawable cd = (ColorDrawable) listTypeTextView.getBackground();
						int colorCode = cd.getColor();
						v.setBackgroundColor(colorCode);*/

			break;

		case R.id.listTypesDropdownLinearLayout:
			listTypeTextView = (TextView) view.findViewById(R.id.txtListTypeDropdown);
			listTypeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListTypesTable.COL_LIST_TYPE)));
			listTypeTextView.setTypeface(null, Typeface.ITALIC);
			listTypeTextView.setTextColor(res.getColor(R.color.black));
			break;
		}
	}

	@Override
	public View newView(Context c, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View v = inflater.inflate(R.layout.list_types_row, parent, false);
		return v;
	}

	@Override
	public View newDropDownView(Context c, Cursor cursor, ViewGroup parent) {
		super.newDropDownView(c, cursor, parent);
		Resources res = c.getResources();
		View v = View.inflate(c, R.layout.list_types_dropdown_row, null);
		return v;
	}
}
