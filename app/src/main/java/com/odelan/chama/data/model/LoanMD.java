package com.odelan.chama.data.model;

import android.widget.ScrollView;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 7/12/2017.
 */

@JsonObject
public class LoanMD extends BaseModel {

    public static String TYPE_EMERGENCY_FUND = "emergency_fund";
    public static String TYPE_3X_MY_SAVING = "3x_my_saving";

    @JsonField(name = "id")
    public Integer id;

    @JsonField(name = "username")
    public String username;

    @JsonField(name = "amount")
    public String amount;

    @JsonField(name = "currency")
    public String currency;

    @JsonField(name = "duedate")
    public String duedate;

    @JsonField(name = "type")
    public String type; //TYPE_EMERGENCY_FUND or TYPE_3X_MY_SAVING

    public String getAmount() {
        return amount + " " + currency;
    }
}
