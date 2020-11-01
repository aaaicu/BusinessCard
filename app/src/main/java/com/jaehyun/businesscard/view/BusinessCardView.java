package com.jaehyun.businesscard.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.jaehyun.businesscard.R;

public class BusinessCardView extends LinearLayout {
    LinearLayout card;
    Context context;

    public BusinessCardView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public BusinessCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public BusinessCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.view_business_card, this, false);
//        setScaleX(0.5f);
//        setScaleY(0.5f);
        addView(v);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left  = getLeft();
        int top   = getTop();
        int width = getWidth();
        int height= getHeight();
        int mwidth= getMeasuredWidth();
        int mheight=getMeasuredHeight();

        Log.v("CustomView-onDraw", "rect : (x, y, w, h, mw, mh) : " + left + " " + top + " " + width + " " + height + " " + mwidth + " " + mheight);

    }
}
