package com.madret.icardadmin;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.madret.icardadmin.Retrofit.ApiRequestCards;
import com.madret.icardadmin.Retrofit.Card;
import com.madret.icardadmin.Retrofit.CardItems;
import com.madret.icardadmin.Utilites.Cardadapter;
import com.madret.icardadmin.Retrofit.Cardsresponse;
import com.madret.icardadmin.Utilites.PreferenceManger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.madret.icardadmin.Retrofit.Client.BASE_URL;

public class ShowAllCardActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)ProgressBar progressBar;
    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.toolbar)Toolbar toolbar;

    CardItems cardItems;
    String name,companyname,PersonalImage,id,profession,age,address,link,issuedate,expiredate;
    List<CardItems> cardlist;
    private Cardadapter cardadapter;
    PreferenceManger preferenceManger;
    List<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_card);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        preferenceManger = new PreferenceManger(this);

        cardlist = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



            RequestedCard(preferenceManger.GetPhone());








    }


    public void RequestedCard(String phone)
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiRequestCards apiRequestCards = retrofit.create(ApiRequestCards.class);
        Call<Cardsresponse> call = apiRequestCards.getRequestedCards(phone);
        call.enqueue(new Callback<Cardsresponse>()
        {
            @Override
            public void onResponse(Call<Cardsresponse>call, Response <Cardsresponse> response)
            {
                progressBar.setVisibility(View.GONE);
                Log.d("tag", "onResponse: "+response.code());
                cards = response.body().getCards();

                if(response.body().getCards() != null)
                {
                    for (int i = 0; i < cards.size() ; i++)
                    {
                        cardItems = new CardItems();
                        name = cards.get(i).getFullName();
                        companyname = cards.get(i).getCompanyName();
                        PersonalImage  = cards.get(i).getPersonImage();
                        id  = cards.get(i).getIdentificationNumber();
                        profession  = cards.get(i).getProfession();
                        age  = cards.get(i).getAge();
                        address  = cards.get(i).getAddress();
                        link  = cards.get(i).getCompanylink();
                        issuedate  = cards.get(i).getIssueDate();
                        expiredate  = cards.get(i).getExpireDate();
                        cardItems.setName(name);
                        cardItems.setCompany(companyname);
                        cardItems.setPersonalimage(PersonalImage);
                        cardItems.setId(id);
                        cardItems.setProfession(profession);
                        cardItems.setAge(age);
                        cardItems.setAddress(address);
                        cardItems.setLink(link);
                        cardItems.setIssuedate(issuedate);
                        cardItems.setExpiredate(expiredate);
                        cardlist.add(cardItems);
                    }
                }
                else
                {
                    Toast.makeText(ShowAllCardActivity.this, "No Cards Found", Toast.LENGTH_SHORT).show();
                }
                cardadapter = new Cardadapter(cardlist,getApplicationContext());
                 RecyclerView.LayoutManager recyce = new LinearLayoutManager(ShowAllCardActivity.this);
                 recyclerView.setAdapter(cardadapter);

            }

            @Override
            public void onFailure(Call<Cardsresponse> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                Log.d("tag", "onFailure: "+t.toString());
                Toast.makeText(ShowAllCardActivity.this, "Poor Internet Connections", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchview, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

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

    private void search(SearchView searchView)
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                cardadapter.getFilter().filter(newText);
                if (cardadapter != null) cardadapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(ShowAllCardActivity.this,DashBoardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.leftrightin,R.anim.leftrightout);
    }

    protected void onResume()
    {
            super.onResume();
            if(cardlist.size() > 0)
            {   cardlist.clear();
                RequestedCard(preferenceManger.GetPhone());
            }
    }

    private void logoutUser()
    {
        preferenceManger.Clear();
        preferenceManger.setLogin(false);
        Intent intent = new Intent(ShowAllCardActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}