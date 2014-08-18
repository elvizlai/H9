package com.elvizlai.h9.activity.login;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.elvizlai.h9.R;
import com.elvizlai.h9.activity.loading.Loading;
import com.elvizlai.h9.config.Config;
import com.elvizlai.h9.util.CryptUtil;
import com.elvizlai.h9.util.LogUtil;
import com.elvizlai.h9.util.ToastUtil;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends Activity {
    //用于记录上一次按返回按钮的时间，用于连按两次返回退出
    private long mExitTime;


    private EditText user_ed, psw_ed;
    private CheckBox isRemPsw_cb, isAutoLogin_cb;
    private Button login_btn;
    private ImageButton ipSetting_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();
        restoreData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1989 && resultCode == 0x1129) {
            ToastUtil.showMsg("用户名或密码错误！");
        }else if (requestCode == 0x1989 && resultCode == 0x0605){
            ToastUtil.showMsg("无法连接到服务器！");
        }
    }

    /**
     * 初始化视图
     */
    private void init() {
        user_ed = (EditText) findViewById(R.id.user_ed);
        psw_ed = (EditText) findViewById(R.id.psw_ed);

        isRemPsw_cb = (CheckBox) findViewById(R.id.isRemPsw_cb);
        isAutoLogin_cb = (CheckBox) findViewById(R.id.isAutoLogin_cb);

        login_btn = (Button) findViewById(R.id.login_btn);

        ipSetting_btn = (ImageButton) findViewById(R.id.ipSetting_btn);


        View.OnClickListener btnClickedHandler = new btnClickedHandler();

        login_btn.setOnClickListener(btnClickedHandler);
        ipSetting_btn.setOnClickListener(btnClickedHandler);
    }


    /**
     * 配置文件恢复
     */
    private void restoreData() {
        user_ed.setText(Config.getInstance().getAccount());

        boolean isRemPsw = Config.getInstance().getIsRemPsw();

        if (isRemPsw) {
            isRemPsw_cb.setChecked(isRemPsw);
            psw_ed.setText(CryptUtil.decryptPsw(Config.getInstance().getPassword()));
        }

        isAutoLogin_cb.setChecked(Config.getInstance().getIsAutoLogin());
    }

    private void storeData() {
        String user = user_ed.getText().toString();
        String psw = psw_ed.getText().toString();

        if (user.equals("") || psw.equals("")) {
            Toast.makeText(getBaseContext(), "输入不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtil.d(user + " " + psw);
        Config.getInstance().setAccount(user);
        Config.getInstance().setPassword(CryptUtil.encryptPsw(psw));
        Config.getInstance().setIsRemPsw(isRemPsw_cb.isChecked());
        Config.getInstance().setIsAutoLogin(isAutoLogin_cb.isChecked());

    }

    private void showNetSettingDialog() {
        final LayoutInflater factory = LayoutInflater.from(Login.this);
        final View view = factory.inflate(R.layout.url_setting, null);
        final EditText ed_url = (EditText) view.findViewById(R.id.url);//获得输入框对象
        ed_url.setText(Config.getInstance().getServiceUrl().replaceAll("/JHSoft.WCF/POSTServiceForAndroid.svc/", ""));
        new AlertDialog.Builder(this)
                .setTitle("配置服务器地址")//提示框标题
                .setCancelable(false)//不允许通过后退关闭对话框
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            private boolean isUrlInvalid(String url) {
                                Pattern pattern = Pattern.compile("^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$", Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(url);
                                if (!matcher.matches()) {
                                    Toast.makeText(Login.this, R.string.url_error_toast, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                return true;
                            }

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = ed_url.getText().toString();
                                try {
                                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                    field.setAccessible(true);
                                    field.set(dialog, false);//为false不能关闭对话框。为true则可关闭
                                    if (isUrlInvalid(url)) {
                                        Config.getInstance().setServiceUrl(url + "/JHSoft.WCF/POSTServiceForAndroid.svc/");
                                        field.set(dialog, true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                )
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create().show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_netsetting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.network_settings) {
            showNetSettingDialog();
            return true;
        }

        if (id == R.id.about_me) {
            ToastUtil.showMsg("ElvizLai");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 屏蔽返回按钮，按一次返回按钮提示“再按一次退出程序”
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showMsg(getString(R.string.exit_hint));
                mExitTime = System.currentTimeMillis();

            } else {
                //该方法对2.2版本后有效
                finish();
                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                manager.killBackgroundProcesses(getPackageName());
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击登录、menu响应
     */
    private class btnClickedHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_btn:
                    storeData();
                    Intent intent = new Intent(Login.this, Loading.class);
                    startActivityForResult(intent, 0x1989);
                    break;
                case R.id.ipSetting_btn:
                    showNetSettingDialog();
                    break;
                default:
                    break;
            }

        }
    }
}
