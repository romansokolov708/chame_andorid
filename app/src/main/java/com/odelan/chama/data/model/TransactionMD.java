package com.odelan.chama.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 7/12/2017.
 */

@JsonObject
public class TransactionMD extends BaseModel {

    public static String TYPE_CONTRIBUTE = "contribute";
    public static String TYPE_BACK = "back";
    public static String TYPE_BORROW = "borrow";
    public static String TYPE_WITHDRAW = "withdraw";
    public static String TYPE_MERRY = "merry";
	public static String TYPE_LOANS = "loan";

    @JsonField(name = "_id")
    public Integer _id;

    @JsonField(name = "type")
    public String type; // i.e : TYPE_CONTRIBUTE

    @JsonField(name = "username") // back to whom
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
