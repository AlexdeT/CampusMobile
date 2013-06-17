package fr.alexdet.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import fr.alexdet.android.R;


/**
 * Class done in order to custom text font over the application
 * 
 * @author alexisdetalhouet
 * 
 */
public class CustomFontTextView extends TextView {

	/**
	 * Constructo
	 * 
	 * @param context
	 *            the application context
	 * @param attrs
	 *            contain the font style
	 * @param defStyle
	 *            style id
	 */
	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode()) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CustomFontTextView, defStyle, 0);

			int fontId = a.getInteger(R.styleable.CustomFontTextView_fontName,
					-1);
			if (fontId == -1) {
				throw new IllegalArgumentException(
						"The font_name attribute is required and must refer "
								+ "to a valid child.");
			}
			a.recycle();
			initialize(fontId);
		}

	}

	/**
	 * Default constructor
	 * 
	 * @param context
	 *            the application context
	 * @param attrs
	 *            conatin the font style
	 */
	public CustomFontTextView(Context context, AttributeSet attrs) {
		// call the constructor which has the complete definition
		this(context, attrs, 0);
	}

	/**
	 * Default Constructor
	 * 
	 * @param context
	 *            the application context
	 */
	public CustomFontTextView(Context context) {
		super(context);
		// This fallbacks to the default TextView without applying any custom
		// fonts
	}

	/**
	 * initialise the font of the textView
	 * 
	 * @param fontId
	 *            the font id requested
	 */
	public void initialize(int fontId) {

		Typeface tf = null;
		switch (fontId) {
		case 0:
			tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Champagne&Limousines.ttf");
			break;
		case 1:
			tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Champagne&LimousinesBoldItalic.ttf");
			break;
		case 2:
			tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Champagne&LimousinesBold.ttf");
			break;
		case 3:
			tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Champagne&LimousinesItalic.ttf");
			break;
		}

		setTypeface(tf);
	}
}
