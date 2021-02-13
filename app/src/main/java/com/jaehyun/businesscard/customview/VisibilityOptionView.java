package com.jaehyun.businesscard.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.data.local.BusinessCardEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class VisibilityOptionView extends LinearLayout {
    LinearLayout visibilityView = null;
    private BusinessCardView businessCardView = null;

    List<CheckBox> checkBoxes = new ArrayList<>();


    Context context;
    CheckBox name = null;
    CheckBox teamName = null;
    CheckBox position = null;
    CheckBox address = null;
    CheckBox companyName = null;
    CheckBox companyAddress = null;
    CheckBox tel = null;
    CheckBox mobile = null;

    public VisibilityOptionView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public VisibilityOptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public VisibilityOptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        visibilityView = (LinearLayout) li.inflate(R.layout.view_visibility_option, this, false);



//        name = card.findViewById(R.id.textViewName);
//        email = card.findViewById(R.id.textViewEmail);
//        tel = card.findViewById(R.id.textViewTel);
//        mobile = card.findViewById(R.id.textViewMobile);
//        team = card.findViewById(R.id.textViewTeamName);
//        position = card.findViewById(R.id.textViewPosition);
//        address = card.findViewById(R.id.textViewCompanyAddress);
//        fax = card.findViewById(R.id.textViewFax);
        addView(visibilityView);
    }

    public void setBusinessCardView(BusinessCardView businessCardView) {
        this.businessCardView = businessCardView;
        for(View v : businessCardView.getViewList()){
            CheckBox c = new CheckBox(context);
            if(v.getTag() != null) {
                c.setText(v.getTag().toString());
                Log.d("test",v.getTag().toString());
                c.setChecked(true);
                c.setOnCheckedChangeListener((compoundButton, b) -> {
                    if(b){
                        v.setVisibility(VISIBLE);
                    }else {
                        v.setVisibility(INVISIBLE);
                    }
                });
            }
            checkBoxes.add(c);
            addView(c);
        }
    }

    public void drawCheck(){

    }
}
