package com.parking.app.parkingappdriver.model;

import java.io.Serializable;


public class ClearDentDTO implements Serializable {

    private String partName;
    private boolean clearDent;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public boolean isClearDent() {
        return clearDent;
    }

    public void setClearDent(boolean clearDent) {
        this.clearDent = clearDent;
    }
}
