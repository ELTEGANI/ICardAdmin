package com.madret.icardadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.madret.icardadmin.Utilites.PreferenceManger;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.imageview_newCard)ImageView imageViewNewCard;
    @BindView(R.id.imageview_showallcard)ImageView imageViewShowCard;
    @BindView(R.id.imageview_barcode)ImageView imageViewbarcode;


    PreferenceManger preferenceManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        preferenceManger = new PreferenceManger(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        imageViewNewCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DashBoardActivity.this,CreateNewCardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.bottomtop, R.anim.none);
            }
        });

        imageViewShowCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DashBoardActivity.this,ShowAllCardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.bottomtop,R.anim.none);
            }
        });

        imageViewbarcode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DashBoardActivity.this,QrCodeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.bottomtop,R.anim.none);
            }
        });
    }


//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//        timer = new Timer();
//        Log.i("Main", "Invoking logout timer");
//        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
//        timer.schedule(logoutTimeTask,300000); //auto logout in 5 minutes
//    }
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        if (timer != null)
//        {
//            timer.cancel();
//            Log.i("Main", "cancel timer");
//            timer = null;
//        }
//    }
//
//    private class LogOutTimerTask extends TimerTask {
//
//        @Override
//        public void run()
//        {
//            //redirect user to login screen
//            Intent i = new Intent(DashBoardActivity.this, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
//            finish();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.menu_signout:
                logoutUser();
                break;

            default:
                return false;


        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser()
    {
        preferenceManger.Clear();
        preferenceManger.setLogin(false);
        Intent intent = new Intent(DashBoardActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }



}
