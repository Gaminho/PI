package com.gaminho.pi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaminho.pi.R;

public class TextWithLabel extends LinearLayout {

    private TextView mTVLabel, mTVValue;
    private String mLabel, mValue;

    public TextWithLabel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextWithLabel);
        mLabel = typedArray.getString(R.styleable.TextWithLabel_label);
        mValue = typedArray.getString(R.styleable.TextWithLabel_value);
        typedArray.recycle();

        init();
    }

    private void init(){
        inflate(getContext(), R.layout.label_value_field, this);
        mTVLabel = findViewById(R.id.tv_label);
        mTVValue = findViewById(R.id.tv_value);

        setLabel(mLabel != null ? mLabel : "Label");
        setValue(mValue != null ? mValue : "Value");
    }

    public void setLabel(String pLabel){
        mLabel = pLabel;
        mTVLabel.setText(mLabel);
    }

    public String getValue(){
        return mValue;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setValue(Object pValue){
        mValue = String.valueOf(pValue);
        mTVValue.setText(mValue);
    }
}
