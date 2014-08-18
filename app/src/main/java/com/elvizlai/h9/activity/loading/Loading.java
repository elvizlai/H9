package com.elvizlai.h9.activity.loading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.elvizlai.h9.R;
import com.elvizlai.h9.activity.loading.entity.TelPhoneInfo;
import com.elvizlai.h9.activity.main.Main;
import com.elvizlai.h9.config.Config;
import com.elvizlai.h9.util.AsyncHttpUtil;
import com.elvizlai.h9.util.JSONUtil;
import com.elvizlai.h9.util.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elvizlai on 14-8-14.
 */
public class Loading extends Activity {
    final String sign = Config.getInstance().getSign();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        LogUtil.d(sign);

        Intent intent = new Intent();
        //setResult(0x1129, intent);

        /**
         * 获取设备的信息，主要包括imei与MODEL
         */
        TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        TelPhoneInfo telPhoneInfo = new TelPhoneInfo();
        telPhoneInfo.setPhoneMacType(1);
        telPhoneInfo.setPhoneDeviceId(imei);
        telPhoneInfo.setPhoneModel(android.os.Build.MODEL);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sign", sign);
            jsonObject.put("telPhoneInfo", JSONUtil.format(telPhoneInfo));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        login(jsonObject);

    }


    private void login(JSONObject jsonObject) {
        AsyncHttpUtil.post("LoginNew", jsonObject, new AsyncHttpResponseHandler() {
            final int[] state = new int[1];

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                //说明帐号密码不正确
                if ((Integer) JSONUtil.JsonStr2Map(bytes).get("success") == 0) {
                    state[0] = -1;
                }
            }

            //网络异常
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                System.out.println("err:" + i);
                state[0] = -2;
            }

            @Override
            public void onFinish() {
                super.onFinish();

                if (state[0] == -1) {
                    setResult(0x1129);
                    finish();
                } else if (state[0] == -2) {
                    setResult(0x0605);
                    finish();
                } else {
                    Intent intent = new Intent(getBaseContext(), Main.class);
                    //设置标志，存在就拉倒前台，不存在就新建一个
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
