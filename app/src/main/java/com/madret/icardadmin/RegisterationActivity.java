package com.madret.icardadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.madret.icardadmin.Retrofit.ApiServiceRegisteration;
import com.madret.icardadmin.Retrofit.ResgisterationPojo;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterationActivity extends AppCompatActivity {


    @BindView(R.id.AppCompatEditText_Name)AppCompatEditText EditTextName;
    @BindView(R.id.AppCompatEditText_phone)AppCompatEditText EditTextPhone;
    @BindView(R.id.AppCompatEditText_password)AppCompatEditText EditTextPassword;
    @BindView(R.id.AppCompatEditText_companyname)AppCompatEditText EditTextCompany;
    @BindView(R.id.AppCompatButton_register)AppCompatButton register;
    @BindView(R.id.constraint)ConstraintLayout constraintLayout;
    @BindView(R.id.progressBar)ProgressBar progressbar;
    @BindView(R.id.ccp)CountryCodePicker ccp;
    String PhoneNumber,Name,Password,Company;
    static final String BASE_URL = "http://madret.com/";
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        ButterKnife.bind(this);
        constraintLayout.setOnClickListener(null);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Validation();
            }
        });
    }


    public void Validation()
    {
        ccp.registerCarrierNumberEditText(EditTextPhone);
        PhoneNumber = ccp.getFullNumberWithPlus();
        Password    = EditTextPassword.getText().toString().trim();
        Company     = EditTextCompany.getText().toString().trim();;
        Name        = EditTextName.getText().toString().trim();;

     if(Name.isEmpty() || PhoneNumber.isEmpty() || Password.isEmpty() || Company.isEmpty())
     {
         Snackbar snackbar = Snackbar.make(constraintLayout, "Please Fill Empty Field", Snackbar.LENGTH_LONG);
         View sbView = snackbar.getView();
         TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
         textView.setTextColor(Color.parseColor("#f20707"));
         textView.setTextSize(18);
         snackbar.show();
         return;
     }
     if(EditTextPhone.getText().toString().length() < 9)
     {
         EditTextPhone.setError("Phone Number Less than 9 number");
         return;
     }
     else
     {
         RegisterationAdmin();
     }
    }

    public void RegisterationAdmin()
    {
        progressbar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceRegisteration apiServices = retrofit.create(ApiServiceRegisteration.class);
        Call<ResgisterationPojo> call = apiServices.getResponse(Name,PhoneNumber,Password,Company);
        call.enqueue(new Callback<ResgisterationPojo>()
        {
            @Override
            public void onResponse(Call<ResgisterationPojo> call, retrofit2.Response<ResgisterationPojo> response)
            {
                Log.d("tag", "onResponse: "+response.code());

                progressbar.setVisibility(View.GONE);

                  if(!response.body().isError())
                  {
                     Intent intent = new Intent(RegisterationActivity.this,LoginActivity.class);
                      startActivity(intent);
                      finish();
                  }
                  else
                  {
                      AlertMsg(response.body().getErrorMsg());
                  }

            }

            @Override
            public void onFailure(Call<ResgisterationPojo> call, Throwable t)
            {
                progressbar.setVisibility(View.GONE);
                Log.d("tag", "onFailure: "+t.toString());
                AlertMsg("Check Your Internet Connection");
            }
        });
    }
    public void AlertMsg(String msg)
    {
        builder = new AlertDialog.Builder(RegisterationActivity.this);
        builder.setMessage(msg);
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(RegisterationActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.bottomtop, R.anim.none);
    }

}
