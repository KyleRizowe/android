package com.fuel4media.carrythistoo.model;

public class StateLaws {
    private String law_name;
    private Integer status;
    private Integer status1;
    private Integer status2;
    private String state1_abv;

    public String getState1_abv() {
        return state1_abv;
    }

    public String getState2_abv() {
        return state2_abv;
    }

    private String state2_abv;
    private Integer law_id;
    private String description;

    public String getDescription() {
        return description;
    }

    public String getLaw_name() {
        return law_name;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getStatus1() {
        return status1;
    }

    public Integer getStatus2() {
        return status2;
    }

    public Integer getLaw_id() {
        return law_id;
    }
}
