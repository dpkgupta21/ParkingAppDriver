package com.parking.app.parkingappdriver.webservices.ihelper;

/**
 * Web API Response Helper
 *
 * @author Harish
 */
public interface WebAPIResponseListener {
    /**
     * On Success of API Call
     *
     * @param arguments
     */
    void onSuccessOfResponse(Object... arguments);

    /**
     * on Fail of API Call
     *
     * @param arguments
     */
    void onFailOfResponse(Object... arguments);

}
