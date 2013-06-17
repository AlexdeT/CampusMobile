package fr.alexdet.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


/**
 * From http://stackoverflow.com/questions/4175398/clear-edittext-on-click
 * Superclass of EditText Display a small cross at the right of the EditText
 * Allow to clear it in one click
 */

public class MyClearableEditText extends EditText {

	public String defaultValue = "";
	final Drawable imgX = getResources().getDrawable(
			android.R.drawable.ic_menu_close_clear_cancel);

	public MyClearableEditText(Context context) {
		super(context);

		init();
	}

	public MyClearableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public MyClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	void init() {

		this.setPadding(10, 0, 5, 0);

		imgX.setBounds(0, 0, imgX.getIntrinsicWidth() - 5,
				imgX.getIntrinsicHeight() - 5);

		manageClearButton();

		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				MyClearableEditText et = MyClearableEditText.this;

				if (et.getCompoundDrawables()[2] == null)
					return false;
				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;
				if (event.getX() > et.getWidth() - et.getPaddingRight()
						- imgX.getIntrinsicWidth()) {
					et.setText("");
					MyClearableEditText.this.removeClearButton();
				}
				return false;
			}
		});

		this.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				MyClearableEditText.this.manageClearButton();
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
	}

	void manageClearButton() {
		if (this.getText().toString().equals(""))
			removeClearButton();
		else
			addClearButton();
	}

	void addClearButton() {
		this.setCompoundDrawables(this.getCompoundDrawables()[0],
				this.getCompoundDrawables()[1], imgX,
				this.getCompoundDrawables()[3]);
	}

	void removeClearButton() {
		this.setCompoundDrawables(this.getCompoundDrawables()[0],
				this.getCompoundDrawables()[1], null,
				this.getCompoundDrawables()[3]);
	}

}
