package com.parking.app.parkingappdriver.model;

import java.io.Serializable;
import java.util.List;


public class VehicleInspectDTO implements Serializable {

    private String comments;
    private String customersignoff;
    private List<VehicleImagesDTO> imagesDTO;
    private List<ClearDentDTO> partsCondition;

    public List<ClearDentDTO> getPartsCondition() {
        return partsCondition;
    }

    public void setPartsCondition(List<ClearDentDTO> partsCondition) {
        this.partsCondition = partsCondition;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomersignoff() {
        return customersignoff;
    }

    public void setCustomersignoff(String customersignoff) {
        this.customersignoff = customersignoff;
    }

    public List<VehicleImagesDTO> getImagesDTO() {
        return imagesDTO;
    }

    public void setImagesDTO(List<VehicleImagesDTO> imagesDTO) {
        this.imagesDTO = imagesDTO;
    }
}
