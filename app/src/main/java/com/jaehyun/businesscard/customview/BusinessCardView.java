package com.jaehyun.businesscard.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.data.local.BusinessCardEntity;

import java.util.ArrayList;
import java.util.List;

public class BusinessCardView extends LinearLayout {
    LinearLayout card = null;
    Context context;
    TextView name = null;
    TextView email = null;
    TextView tel = null;
    TextView mobile = null;
    TextView team = null;
    TextView position = null;
    TextView address = null;
    TextView fax = null;

    List<View> viewList = new ArrayList<>();

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
        card = (LinearLayout) li.inflate(R.layout.view_business_card, this, false);


        name = card.findViewById(R.id.textViewName);
        viewList.add(name);

        email = card.findViewById(R.id.textViewEmail);
        viewList.add(email);

        tel = card.findViewById(R.id.textViewTel);
        viewList.add(tel);

        mobile = card.findViewById(R.id.textViewMobile);
        viewList.add(mobile);

        team = card.findViewById(R.id.textViewTeamName);
        viewList.add(team);

        position = card.findViewById(R.id.textViewPosition);
        viewList.add(position);

        address = card.findViewById(R.id.textViewCompanyAddress);
        viewList.add(address);

        fax = card.findViewById(R.id.textViewFax);
        viewList.add(fax);

        addView(card);
    }

    public View setBusinessCardData(BusinessCardEntity e){
        name.setText(e.getName());
        email.setText(e.getEmail());
        tel.setText(e.getTel());
        mobile.setText(e.getMobile());
        team.setText(e.getTeam());

        position.setText(e.getPosition());
        address.setText(e.getAddress());
        fax.setText(e.getFax());
//        removeView(card);
//        addView(card);
        return card;
    }

    public List<View> getViewList() {
        return viewList;
    }
}
