package com.madret.icardadmin.Utilites;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.madret.icardadmin.CardDetailesActivity;
import com.madret.icardadmin.R;
import com.madret.icardadmin.Retrofit.CardItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigani on 11/13/2017.
 */

public class Cardadapter extends RecyclerView.Adapter<Cardadapter.MyHolder> implements Filterable {


    private List<CardItems> cardlist;
    private Context context;
    private List<CardItems> mFilteredList;

    public Cardadapter(List<CardItems> cardlists, Context context)
    {
        this.cardlist = cardlists;
        this.context = context;
        this.mFilteredList = cardlist;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position)
    {
        holder.name.setText(mFilteredList.get(position).getName());
        holder.companyname.setText(mFilteredList.get(position).getCompany());
        holder.id.setText(mFilteredList.get(position).getId());
        holder.profession.setText(mFilteredList.get(position).getProfession());
        holder.address.setText(mFilteredList.get(position).getAddress());
        holder.link.setText(mFilteredList.get(position).getLink());
        holder.age.setText(mFilteredList.get(position).getAge());
        holder.expiredate.setText(mFilteredList.get(position).getExpiredate());
        holder.issuedate.setText(mFilteredList.get(position).getIssuedate());
        final String image = mFilteredList.get(position).getPersonalimage();
        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.personImage);
        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent  = new Intent(context,CardDetailesActivity.class);
                intent.putExtra("name",holder.name.getText().toString());
                intent.putExtra("companyname",holder.companyname.getText().toString());
                intent.putExtra("personimage",image);
                intent.putExtra("id",holder.id.getText().toString());
                intent.putExtra("profession",holder.profession.getText().toString());
                intent.putExtra("address",holder.address.getText().toString());
                intent.putExtra("link",holder.link.getText().toString());
                intent.putExtra("age",holder.age.getText().toString());
                intent.putExtra("issuedate",holder.issuedate.getText().toString());
                intent.putExtra("expiredate",holder.expiredate.getText().toString());
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                {
                    mFilteredList = cardlist;
                } else {
                    List<CardItems> filteredList = new ArrayList<>();
                    for (CardItems carditems : cardlist)
                    {
                        if (carditems.getName().toLowerCase().contains(charSequence))
                        {
                            filteredList.add(carditems);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                mFilteredList = (List<CardItems>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView name,companyname,id,age,profession,link,address,issuedate,expiredate;
        CardView cardView;
        ImageView personImage;

        public MyHolder(View itemView)
        {
            super(itemView);
            name        = (TextView) itemView.findViewById(R.id.textView_name);
            companyname = (TextView) itemView.findViewById(R.id.textView_companyname);
            personImage = (ImageView) itemView.findViewById(R.id.imageView_person);
            id = (TextView) itemView.findViewById(R.id.textView_id);
            age = (TextView) itemView.findViewById(R.id.textView_age);
            profession = (TextView) itemView.findViewById(R.id.textView_profession);
            link = (TextView) itemView.findViewById(R.id.textView_link);
            address = (TextView) itemView.findViewById(R.id.textView_address);
            issuedate = (TextView) itemView.findViewById(R.id.textView_issuedate);
            expiredate = (TextView) itemView.findViewById(R.id.textView_expiredate);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }


    }
}
