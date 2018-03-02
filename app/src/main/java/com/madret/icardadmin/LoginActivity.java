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
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.madret.icardadmin.Retrofit.ApiServiceLogin;
import com.madret.icardadmin.Retrofit.loginpojo;
import com.madret.icardadmin.Utilites.PreferenceManger;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.madret.icardadmin.RegisterationActivity.BASE_URL;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.AppCompatEditText_phonenumber)AppCompatEditText EditTextPhoneNumber;
    @BindView(R.id.AppCompatEditText_password)AppCompatEditText EditTextPassword;
    @BindView(R.id.AppCompatTextView_createaccount) AppCompatTextView CreateAccount;
    @BindView(R.id.AppCompatButton_buttonlogin)AppCompatButton Login;
    String phone,password;
    @BindView(R.id.constraint)ConstraintLayout constraintLayout;
    @BindView(R.id.ccp)CountryCodePicker ccp;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    @BindView(R.id.progressBar)ProgressBar progressbar;
    PreferenceManger preferenceManger;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ccp.setCountryForNameCode("SD");
        constraintLayout.setOnClickListener(null);
        preferenceManger = new PreferenceManger(this);



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Validation();

            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LoginActivity.this,RegisterationActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.bottomtop, R.anim.none);
            }
        });
    }

    public void Validation() {
        phone    = EditTextPhoneNumber.getText().toString().trim();
        password = EditTextPassword.getText().toString().trim();

        ccp.registerCarrierNumberEditText(EditTextPhoneNumber);
        phone = ccp.getFullNumberWithPlus();
        if(phone.isEmpty() || password.isEmpty())
        {
            Snackbar snackbar = Snackbar.make(constraintLayout, "Please Fill Empty Field", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.parseColor("#f20707"));
            textView.setTextSize(18);
            snackbar.show();
            return;
        }
        if(phone.length() < 9)
        {
            EditTextPhoneNumber.setError("Phone Number Less than 9 number");
        }
        else
        {
            Login();
        }
    }

    public void Login()
    {
        progressbar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceLogin apiServicelogin = retrofit.create(ApiServiceLogin.class);
        Call<loginpojo> call = apiServicelogin.getlogin(phone,password);
        call.enqueue(new Callback<loginpojo>()
        {
            @Override
            public void onResponse(Call<loginpojo> call, retrofit2.Response<loginpojo> response)
            {
                progressbar.setVisibility(View.GONE);
                if(!response.body().isError())
                {
                    preferenceManger.Clear();
                    preferenceManger.setLogin(true);
                    preferenceManger.SaveCardUsage(response.body().getPhone(), Integer.parseInt(response.body().getTotalCard()), Integer.parseInt(response.body().getUsedCard()),response.body().getStatus());
                    Intent i = new Intent(LoginActivity.this,DashBoardActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.leftrightin,R.anim.leftrightout);
                }
                else
                {
                    AlertMsg(response.body().getErrorMsg());
                }

            }

            @Override
            public void onFailure(Call<loginpojo> call, Throwable t)
            {
                progressbar.setVisibility(View.GONE);
                Log.d("tag", "onFailure: "+t.toString());
                AlertMsg("Check Your Internet Connection");
            }
        });
    }
    public void AlertMsg(String msg)
    {
        builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(msg);
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }



}
