package com.lbconsulting.alist_02;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListsCursorAdapter extends CursorAdapter {
	/*public Context context;
	public LayoutInflater inflater;*/

	/*
	 * public ListTitlesCursorAdapter(Context context, int layout, Cursor c,
	 * String[] from, int[] to, int flags) { super(context, layout, c, from, to,
	 * flags);
	 */
	public ListsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		/*this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {
		/*Resources res = context.getResources();

		TextView itemName = (TextView) view.findViewById(R.id.txtLists);
		itemName.setText(cursor.getString(cursor.getColumnIndex(ListsTable.COL_MASTER_LIST_ITEM_ID)));
		int struckOutColumnIndex = cursor.getColumnIndexOrThrow(ListsTable.COL_STRUCK_OUT);
		int isStruckOut = cursor.getInt(struckOutColumnIndex);

		if (isStruckOut == 1) {
		    itemName.setTypeface(null, Typeface.ITALIC);
		    itemName.setTextColor(res.getColor(R.color.red));
		} else {
		    itemName.setTypeface(null, Typeface.NORMAL);
		    itemName.setTextColor(res.getColor(R.color.black));
		}*/
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.list_items_row, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}
