package com.elvizlai.h9.util;

import com.elvizlai.h9.config.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by huagai on 14-8-11.
 */
public class AsyncHttpUtil {

    private static AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();

    private AsyncHttpUtil() {
    }

    static {
        //最多重试3次,每次10秒
        mAsyncHttpClient.setMaxRetriesAndTimeout(3, 10000);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        String URL = "http://" + Config.getInstance().getServiceUrl() + relativeUrl;
        LogUtil.d(URL);
        return URL;
    }


    public static void post(String methodName, JSONObject jsonObject, AsyncHttpResponseHandler responseHandler) {

        try {
            StringEntity entity = new StringEntity(jsonObject.toString());
            post(methodName, entity, responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //url为方法名称
    public static void post(String methodName, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        if (mAsyncHttpClient == null)
            mAsyncHttpClient = new AsyncHttpClient();



        mAsyncHttpClient.post(ApplictionUtil.getContext(), getAbsoluteUrl(methodName), entity, "application/json", responseHandler);
    }


}
