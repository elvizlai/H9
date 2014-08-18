package com.elvizlai.h9.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.elvizlai.h9.util.ApplictionUtil;
import com.elvizlai.h9.util.CryptUtil;

/**
 * Created by elvizlai on 14-4-13.
 */
public class Config {

    private static Config config = new Config();
    private SharedPreferences usrPreference;
    private SharedPreferences sysPreference;

    private Config() {
        usrPreference = ApplictionUtil.getContext().getSharedPreferences("UserInfos", Context.MODE_PRIVATE);
        sysPreference = ApplictionUtil.getContext().getSharedPreferences("SystemInfos", Context.MODE_PRIVATE);
    }

    public static Config getInstance() {
        if (config == null)
            config = new Config();
        return config;
    }

    public String getServiceUrl() {
        String defaultServiceUrl = "192.168.1.94/c6/JHSoft.WCF/POSTServiceForAndroid.svc/";
        return sysPreference.getString("ServiceUrl", defaultServiceUrl);
    }

    public void setServiceUrl(String url) {
        sysPreference.edit().putString("ServiceUrl", url).apply();
    }

    public String getAccount() {
        return usrPreference.getString("Account", "");
    }

    public void setAccount(String account) {
        usrPreference.edit().putString("Account", account).apply();
    }

    public String getPassword() {
        return usrPreference.getString("Password", "");
    }

    public void setPassword(String psw) {
        usrPreference.edit().putString("Password", psw).apply();
    }

//    public void setSign() {
//        usrPreference.edit().putString("sign", account).apply();
//    }

    public String getSign() {
        return getAccount() + "$" + CryptUtil.encryptSHA(CryptUtil.decryptPsw(getPassword()));
    }


    public boolean getIsRemPsw() {
        return usrPreference.getBoolean("isRemPsw", false);
    }

    public void setIsRemPsw(boolean isRemPsw) {
        usrPreference.edit().putBoolean("isRemPsw", isRemPsw).apply();
    }

    public boolean getIsAutoLogin() {
        return usrPreference.getBoolean("isAutoLogin", false);
    }

    public void setIsAutoLogin(boolean isAutoLogin) {
        usrPreference.edit().putBoolean("isAutoLogin", isAutoLogin).apply();
    }

    public boolean getIsFirstInstall() {
        return usrPreference.getBoolean("isFirstInstall", true);
    }

    public void setIsFirstInstall(boolean isFirstInstall) {
        usrPreference.edit().putBoolean("isFirstInstall", isFirstInstall).apply();
    }
}
