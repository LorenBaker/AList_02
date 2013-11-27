package com.lbconsulting.alist_02;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListPreviewArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	private int backgroundColor;
	private int normalTextColor;
	private int strikeoutTextColor;

	public ListPreviewArrayAdapter(Context context, String[] values) {
		super(context, R.layout.master_list_item_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.master_list_item_row, parent, false);
		LinearLayout masterListItemLinearLayout = (LinearLayout) rowView.findViewById(R.id.masterListItemLinearLayout);
		masterListItemLinearLayout.setBackgroundColor(this.backgroundColor);
		TextView itemName = (TextView) rowView.findViewById(R.id.txtMasterListItem);
		itemName.setText(values[position]);

		switch (position) {
		case 1:
			// List Item Strikeout Text
			itemName.setTypeface(null, Typeface.ITALIC);
			itemName.setTextColor(this.strikeoutTextColor);
			/*int hotpink = res.getColor(color.hotpink);
			itemName.setTextColor(hotpink);*/
			itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			break;

		default:
			//List Item Normal Text
			itemName.setTypeface(null, Typeface.NORMAL);
			itemName.setTextColor(this.normalTextColor);
			/*itemName.setTextColor(res.getColor(color.red));*/
			break;
		}

		return rowView;
	}

	public void setColors(int backgroundColor, int normalTextColor, int strikeoutTextColor) {
		this.backgroundColor = backgroundColor;
		this.normalTextColor = normalTextColor;
		this.strikeoutTextColor = strikeoutTextColor;
	}
}