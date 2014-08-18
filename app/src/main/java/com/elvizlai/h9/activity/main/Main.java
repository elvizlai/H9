package com.elvizlai.h9.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.elvizlai.h9.R;
import com.elvizlai.h9.util.ToastUtil;

/**
 * Created by Elvizlai on 14-8-18.
 */
public class Main extends Activity{
    //用于记录上一次按返回按钮的时间，用于连按两次返回退出
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
    }



    /**
     * 屏蔽返回按钮，按一次返回按钮提示“再按一次返回桌面”
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showMsg("再按一次返回桌面");
                mExitTime = System.currentTimeMillis();

            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
