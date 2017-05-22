package com.account.king.node;

import com.account.king.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * 所需要的location类
 * Created by king
 * on 2015/12/1.
 */
public class GeoNode implements Serializable {

    private String TAG = "GeoNode";
    private String province;
    private String city;
    private String region; // 县 区
    private String address;
    private String name;

    private String latitude;
    private String longitude;
    private String cityCode; // 城市加密码


    public GeoNode(JSONObject jsonObject) {
        if (null != jsonObject) {
            LogUtil.d(TAG, "GeoNode=" + jsonObject);
            this.province = jsonObject.optString("province");
            this.city = jsonObject.optString("city");
            this.region = jsonObject.optString("region");
            this.address = jsonObject.optString("address");
            this.name = jsonObject.optString("name");
            this.latitude = jsonObject.optString("latitude");
            this.longitude = jsonObject.optString("longitude");
            this.cityCode = jsonObject.optString("citycode");

        }
    }

    public GeoNode() {

    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * 对象转jsonObject
     *
     * @return
     * @throws JSONException
     */
    public JSONObject toJsonObject() throws JSONException {
        JSONObject mJsonObject = new JSONObject();
        if (this == null) {
            throw new NullPointerException("SnsLocationNode为空");
        }
        mJsonObject.put("province", this.province);
        mJsonObject.put("city", this.city);
        mJsonObject.put("region", this.region);
        mJsonObject.put("address", this.address);
        mJsonObject.put("latitude", this.latitude);
        mJsonObject.put("longitude", this.longitude);
        mJsonObject.put("citycode", this.cityCode
        );
        return mJsonObject;
    }

    @Override
    public String toString() {
        return "DiaryNode [TAG=" + TAG + ", province=" +
                province + ", city=" + city + ", region=" +
                region + ", address=" + address + ", latitude=" +
                latitude + ", longitude=" + longitude + ", citycode=" + cityCode + ", name=" + name + "]";
    }
}
