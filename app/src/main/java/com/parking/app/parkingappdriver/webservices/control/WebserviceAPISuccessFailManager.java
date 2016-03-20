package com.parking.app.parkingappdriver.webservices.control;//package app.parking.com.parkingapp.webservices.control;
//
//import com.app.bike.control.APIResponseControl;
//import com.app.bike.iclasses.GlobalKeys;
//import com.app.bike.utils.VVDNAndroidAppUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Webservice API Response is Success OR Fail
// *
// *
// */
//public class WebserviceAPISuccessFailManager {
//	/**
//	 * Instance of This class
//	 */
//	public static WebserviceAPISuccessFailManager mApiSuccessFailManager;
//	/**
//	 * Debugging TAG
//	 */
//	@SuppressWarnings("unused")
//	private String TAG = WebserviceAPISuccessFailManager.class.getSimpleName();
//
//	private WebserviceAPISuccessFailManager() {
//	}
//
//	/**
//	 * Get Instance of this class
//	 *
//	 * @return
//	 */
//	public static WebserviceAPISuccessFailManager getInstance() {
//		if (mApiSuccessFailManager == null)
//			mApiSuccessFailManager = new WebserviceAPISuccessFailManager();
//		return mApiSuccessFailManager;
//
//	}
//
//	/**
//	 * Get Api Response Status
//	 *
//	 * @param response
//	 * @return
//	 */
//	public boolean getReponseStatus(String response) {
//		if (response != null && !response.isEmpty()) {
//			JSONObject jsonObject;
//			try {
//				jsonObject = new JSONObject(response);
//				if (jsonObject.getBoolean(GlobalKeys.RESPONSE_STATUS)) {
//					return true;
//				} else {
//					return false;
//				}
//			} catch (JSONException e) {
//				// e.printStackTrace();
//				return false;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * Get Api Response Message code
//	 *
//	 * @param response
//	 * @return
//	 */
//	public int getReponseMessageCode(String response) {
//		if (response != null && !response.isEmpty()) {
//			JSONObject jsonObject;
//			try {
//				jsonObject = new JSONObject(response);
//				int msg = jsonObject.getInt(GlobalKeys.RESPONSE_MESSAGE);
//				return msg;
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return 0;
//			}
//		}
//		return 0;
//	}
//	/**
//	 * Get Api Respose message
//	 */
//	public String getReponseMessage(String response) {
//		if (response != null && !response.isEmpty()) {
//			JSONObject jsonObject;
//			try {
//				jsonObject = new JSONObject(response);
//				int msg = jsonObject.getInt(GlobalKeys.RESPONSE_MESSAGE);
//				String msgText = APIResponseControl.RESPONSE_CODE.get(msg);
//				VVDNAndroidAppUtils.showLog(TAG, "" + msg+" "+msgText);
//				return msgText;
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//		return null;
//	}
//	/**
//	 * Get Api Response Data
//	 *
//	 * @param response
//	 * @return
//	 */
//	public JSONArray getReponseData(String response) {
//		if (response != null && !response.isEmpty()) {
//			JSONObject jsonObject;
//			try {
//				jsonObject = new JSONObject(response);
//				JSONArray data = jsonObject
//						.getJSONArray(GlobalKeys.RESPONSE_DATA);
//				return data;
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//		return null;
//	}
//}
