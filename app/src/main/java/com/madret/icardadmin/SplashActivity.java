package com.madret.icardadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.madret.icardadmin.Utilites.PreferenceManger;

public class SplashActivity extends AppCompatActivity {
    PreferenceManger preferenceManger;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        preferenceManger = new PreferenceManger(this);
        if(preferenceManger.isLoggedIn())
        {
            Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
