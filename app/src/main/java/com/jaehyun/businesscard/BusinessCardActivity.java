package com.jaehyun.businesscard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaehyun.businesscard.database.entity.BusinessCardEntity;
import com.jaehyun.businesscard.viewmodel.BusinessCardViewModel;

public class BusinessCardActivity extends AppCompatActivity {
    BusinessCardViewModel businessCardViewModel;
    LiveData<BusinessCardEntity> businessCardEntity;
    TextView name = null;
    TextView email = null;
    TextView tel = null;
    TextView mobile = null;
    TextView team = null;
    TextView position = null;
    TextView address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);
        LinearLayout view = findViewById(R.id.businessCard);
        name = view.findViewById(R.id.textViewName);
        email = view.findViewById(R.id.textViewEmail);
        tel = view.findViewById(R.id.textViewTel);
        mobile = view.findViewById(R.id.textViewMobile);
        team = view.findViewById(R.id.textViewTeamName);
        position = view.findViewById(R.id.textViewPosition);
        address = view.findViewById(R.id.textViewCompanyAddress);

//        BusinessCardRepository repository = new BusinessCardRepository(this);
//
//
        setBusinessCardData(new BusinessCardEntity());


        BusinessCardApplication.getDatabase().businessCardDao().getBusinessCard(1).observe(this, businessCardEntity -> {
            setBusinessCardData(businessCardEntity);
            Log.d("test","옵저버로 정보들어옴??");
        });


        businessCardViewModel = new BusinessCardViewModel(getApplication());

        businessCardEntity = businessCardViewModel.getEntity();
        }

public void setBusinessCardData(BusinessCardEntity e){
        name.setText(e.getName());
        email.setText(e.getEmail());
        tel.setText(e.getTel());
        mobile.setText(e.getMobile());
        team.setText(e.getTeam());
        position.setText(e.getPosition());
        address.setText(e.getAddress());
    }
}
