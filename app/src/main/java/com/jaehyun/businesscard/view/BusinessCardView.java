package com.jaehyun.businesscard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.database.entity.BusinessCardEntity;

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
        email = card.findViewById(R.id.textViewEmail);
        tel = card.findViewById(R.id.textViewTel);
        mobile = card.findViewById(R.id.textViewMobile);
        team = card.findViewById(R.id.textViewTeamName);
        position = card.findViewById(R.id.textViewPosition);
        address = card.findViewById(R.id.textViewCompanyAddress);
        fax = card.findViewById(R.id.textViewFax);

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
        return card;
    }


}
