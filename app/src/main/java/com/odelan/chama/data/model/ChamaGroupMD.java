package com.odelan.chama.data.model;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class ChamaGroupMD extends BaseModel {

    @JsonField(name = "id")
    public String c_id;

    @JsonField(name = "name")
    public String name;

    @JsonField(name = "code")
    public String code;

    @JsonField(name = "max_count")
    public String max_count;

    @JsonField(name = "contribution_amount")
    public String contribution_amount;

    @JsonField(name = "contribution_cycle")
    public String contribution_cycle;

    @JsonField(name = "percent_emergency")
    public String percent_emergency;

    @JsonField(name = "percent_save")
    public String percent_save;

    @JsonField(name = "percent_merry")
    public String percent_merry;

    @JsonField(name = "status")
    public String status; // Active, InActive

}
