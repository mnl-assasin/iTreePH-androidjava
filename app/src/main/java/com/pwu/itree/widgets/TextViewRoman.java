package com.pwu.itree.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pwu.itree.utils.FontStyle;


@SuppressLint("AppCompatCustomView")
public class TextViewRoman extends TextView {

    public TextViewRoman(Context context) {
        super(context);
        setTypeface(FontStyle.helveticaRoman(context));
    }

    public TextViewRoman(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(FontStyle.helveticaRoman(context));
    }

    public TextViewRoman(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(FontStyle.helveticaRoman(context));
    }

}