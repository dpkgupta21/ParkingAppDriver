package com.parking.app.parkingappdriver.model;

import java.io.Serializable;

/**
 * Created by Harish on 3/20/2016.
 */
public class MyJobsDTO implements Serializable {

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    String _id = "";


}
