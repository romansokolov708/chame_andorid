package com.odelan.chama.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 7/12/2017.
 */

@JsonObject
public class OutstandingMD extends BaseModel {

    @JsonField(name = "username")
    public String username;

    @JsonField(name = "phone")
    public String phone;

    @JsonField(name = "chama_code")
    public String chama_code;

    @JsonField(name = "borrowed_amount")
    public String borrowed_amount;

    @JsonField(name = "loan_amount")
    public String loan_amount; // loan_amount will be calculated by using interesting rate

    @JsonField(name = "currency")
    public String currency;

    @JsonField(name = "borrow_date")
    public String borrow_date;

    @JsonField(name = "schedule_loan_date")
    public String schedule_loan_date;

    @JsonField(name = "days")
    public Long days;

    @JsonField(name = "role")
    public String role;

    public String getBorrowedAmount() {
        return borrowed_amount + " " + currency;
    }

    public String getLoanedAmount() {
        return loan_amount + " " + currency;
    }

    public String getDuringDays() {
        return borrow_date + " - " + schedule_loan_date;
    }
}
