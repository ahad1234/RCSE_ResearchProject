package my.project.proveofconcept;

import android.os.Build;

public class GetDeviceInfo {

    private String apiSdk;
    private String userDevice;
    private String deviceModel;
    private String userProduct;


    public String getApiSdk(){
        apiSdk = "" + Build.VERSION.SDK_INT;
        return apiSdk;
    }

    public String getUserDevice(){
        userDevice = Build.DEVICE;
        return userDevice;
    }

    public String getDeviceModel(){
        deviceModel = Build.MODEL;
        return deviceModel;
    }

    public String getUserProduct(){
        userProduct = Build.PRODUCT;
        return userProduct;
    }
}
