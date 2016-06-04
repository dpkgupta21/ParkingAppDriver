package com.parking.app.parkingappdriver.model;


import java.io.Serializable;

public class VehicleImagesDTO implements Serializable  {

    private String image_name;
    private String image_data;

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_data() {
        return image_data;
    }

    public void setImage_data(String image_data) {
        this.image_data = image_data;
    }
}
