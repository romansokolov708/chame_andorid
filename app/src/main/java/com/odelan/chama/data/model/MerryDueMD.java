package com.odelan.chama.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 7/12/2017.
 */

@JsonObject
public class MerryDueMD extends BaseModel {

    @JsonField(name = "username")
    public String username;

    @JsonField(name = "amount")
    public String amount;

    @JsonField(name = "currency")
    public String currency;

    @JsonField(name = "duedate")
    public String duedate;

    public String getAmount() {
        return amount + " " + currency;
    }
}
