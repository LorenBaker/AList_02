package com.lbconsulting.alist_02;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListPreviewArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public ListPreviewArrayAdapter(Context context, String[] values) {
		super(context, R.layout.master_list_item_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.master_list_item_row, parent, false);
		TextView itemName = (TextView) rowView.findViewById(R.id.txtMasterListItem);
		itemName.setText(values[position]);

		Resources res = context.getResources();

		switch (position) {
		case 1:
			// List Item Strikeout Text
			itemName.setTypeface(null, Typeface.ITALIC);
			itemName.setTextColor(res.getColor(R.color.darkgray));
			itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			break;

		default:
			//List Item Normal Text
			itemName.setTypeface(null, Typeface.NORMAL);
			itemName.setTextColor(res.getColor(R.color.black));
			break;
		}

		return rowView;
	}
}