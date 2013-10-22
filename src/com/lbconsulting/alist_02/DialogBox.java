package com.lbconsulting.alist_02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogBox {

	public static final int OK_ONLY = 1;
	public static final int OK_CANCEL = 2;

	public static final int PRESSED_OK = -1;
	public static final int PRESSED_CANCEL = -2;

	private Context context = null;
	private String title = "";
	private String message = "";
	private int style = 0;
	private int result = -1;

	public DialogBox(Context context, String title, String message, int style) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.style = style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getResult() {
		return result;
	}

	private void setResult(int result) {
		this.result = result;
	}

	public void Show() {
		if (this.context == null) {
			return;
		}
		if (this.title == "") {
			return;
		}
		if (this.message == "") {
			return;
		}
		if (this.style < 1) {
			return;
		}

		// confirm user wants to delete the current active list.
		AlertDialog.Builder alert = new AlertDialog.Builder(this.context);
		alert.setTitle(this.title);
		alert.setMessage(this.message);

		switch (this.style) {
		case OK_ONLY:
			alert.setPositiveButton(this.context.getString(R.string.OK),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							DialogBox.this.setResult(whichButton);
						}
					});
			break;

		case OK_CANCEL:

			alert.setPositiveButton(this.context.getString(R.string.OK),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							DialogBox.this.setResult(whichButton);
						}
					});

			alert.setNegativeButton(this.context.getString(R.string.Cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							DialogBox.this.setResult(whichButton);
						}
					});
			break;

		default:
			break;
		}

		alert.show();

	}

}
