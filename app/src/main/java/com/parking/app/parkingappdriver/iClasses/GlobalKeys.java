package com.parking.app.parkingappdriver.iClasses;

/**
 * Created by Harish on 2/21/2016.
 */
public interface GlobalKeys {
    String PASSWORD = "password";
    String EMAIL = "email";
    String LOGIN_API = "users/login";
    String LOGOUT_API = "users/logout";
    String SIGNUP_API = "users/signup";

    String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    String HEADER_VALUE_CONTENT_TYPE = "application/json";
    String PILLION_LOGIN_REQUEST_KEY = "login_api";
    String PILLION_LOGOUT_REQUEST_KEY = "logout_api";
    String MESSAGE = "message";
    String AUTHTOKEN = "authtoken";

    String FIRSTNAME = "firstname";
    String LASTNAME = "lastname";

    String LOAD_JOBS_API = "job/loadjobs";
    String PILLION_LOAD_JOB_REQUEST_KEY = "load_jobs";

    String CONFIRM_JOB_API = "job/confirmjob";
    String JOB_ID = "jobId";
    String VALLET_ID = "valletId";
    String CONFIRM_JOB_REQUEST_KEY = "confirm_job";

    String CURRENT_JOB_API = "job/currentjobs";
    String CURRENT_JOB_REQUEST_KEY = "current_job";

    String START_JOB_API = "job/startjob";
    String START_JOB_REQUEST_KEY = "start_job";
}
