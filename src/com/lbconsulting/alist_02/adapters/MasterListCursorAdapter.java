package com.lbconsulting.alist_02.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.lbconsulting.alist_02.R;
import com.lbconsulting.alist_02.R.color;
import com.lbconsulting.alist_02.R.id;
import com.lbconsulting.alist_02.R.layout;
import com.lbconsulting.alist_02.database.MasterListItemsTable;

public class MasterListCursorAdapter extends CursorAdapter {

	/*public Context context;
	public LayoutInflater inflater;*/

	public MasterListCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		/*this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {
		Resources res = context.getResources();

		TextView itemName = (TextView) view.findViewById(R.id.txtMasterListItem);
		itemName.setText(cursor.getString(cursor.getColumnIndex(MasterListItemsTable.COL_ITEM_NAME)));
		int selectedColIndex = cursor.getColumnIndexOrThrow(MasterListItemsTable.COL_SELECTED);
		int isSelected = cursor.getInt(selectedColIndex);

		if (isSelected == 1) {
			itemName.setTypeface(null, Typeface.ITALIC);
			itemName.setTextColor(res.getColor(R.color.red));
		} else {
			itemName.setTypeface(null, Typeface.NORMAL);
			itemName.setTextColor(res.getColor(R.color.black));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.master_list_item_row, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}
