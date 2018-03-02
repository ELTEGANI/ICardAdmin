package com.madret.icardadmin.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tigani on 11/5/2017.
 */

public class loginpojo
{

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("total_card")
    @Expose
    private String totalCard;
    @SerializedName("used_card")
    @Expose
    private String usedCard;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotalCard() {
        return totalCard;
    }

    public void setTotalCard(String totalCard) {
        this.totalCard = totalCard;
    }

    public String getUsedCard() {
        return usedCard;
    }

    public void setUsedCard(String usedCard) {
        this.usedCard = usedCard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}

