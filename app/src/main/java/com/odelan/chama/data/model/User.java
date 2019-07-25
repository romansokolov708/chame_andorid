package com.odelan.chama.data.model;
import android.text.format.DateFormat;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.Date;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class User extends BaseModel {

    public static final String TYPE_CHAIRPERSON = "Chairperson";
    public static final String TYPE_TREASURE = "Treasure";
    public static final String TYPE_SECRETARY = "Secretary";
    public static final String TYPE_MEMBER = "Member";

    public static final String STATUS_ACTIVE = "Active"; // unsuspended
    public static final String STATUS_SUSPEND = "Suspend";
    public static final String STATUS_REMOVE = "Remove";

    @JsonField(name = "user_name")
    public String user_name;

    @JsonField(name = "phone")
    public String phone;

    @JsonField(name = "avatar")
    public String avatar;

    @JsonField(name = "chama_code")
    public String chama_code;

    @JsonField(name = "chama_name")
    public String chama_name;

    @JsonField(name = "account_status")
    public String account_status; // Active, Suspend

    @JsonField(name = "role")
    public String role; // TYPE_CHAIRPERSON, ...

    @JsonField(name = "max_members")
    public Integer max_members;

    @JsonField(name = "contribution_status")
    public Boolean contribution_status;

    @JsonField(name = "contribution")
    public Integer contribution;

    @JsonField(name = "contribution_cycle")
    public String contribution_cycle;

    @JsonField(name = "emergency_fund")
    public Integer emergency_fund;

    @JsonField(name = "my_saving")
    public Integer my_saving;

    @JsonField(name = "merry_go_round")
    public Integer merry_go_round;

    @JsonField(name = "created_at")
    public String created_at;

    public String getDate() {
        return created_at;
    }
}
