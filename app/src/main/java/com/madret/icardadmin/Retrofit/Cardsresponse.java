package com.madret.icardadmin.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tigani on 11/13/2017.
 */

public class Cardsresponse
{

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("cards")
    @Expose
    private List<Card> cards = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
