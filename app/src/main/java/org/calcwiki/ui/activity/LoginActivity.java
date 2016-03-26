package org.calcwiki.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.jude.utils.JUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.calcwiki.BuildConfig;
import org.calcwiki.R;
import org.calcwiki.data.network.api.ApiService;
import org.calcwiki.data.network.helper.LoginApiHelper;
import org.calcwiki.data.network.helper.QueryApiHelper;
import org.calcwiki.data.storage.CurrentLogin;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.ui.drawer.MainDrawer;
import org.calcwiki.ui.util.CurrentStateStorager;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialEditText usernameEditText;
    MaterialEditText passwordEditText;
    ButtonRectangle loginButton;
    TextView forgetPasswordButton;
    ButtonRectangle registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Init Utils
        JUtils.setDebug(BuildConfig.DEBUG, this.getClass().getName());
        initializeUI(savedInstanceState);
    }

    void initializeUI(Bundle savedInstanceState) {
        // Initialize Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.login);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Initialize Widgets
        usernameEditText = (MaterialEditText) findViewById(R.id.edittext_username);
        passwordEditText = (MaterialEditText) findViewById(R.id.edittext_password);
        loginButton = (ButtonRectangle) findViewById(R.id.button_login);
        forgetPasswordButton = (TextView) findViewById(R.id.button_forget_password);
        registerButton = (ButtonRectangle) findViewById(R.id.button_register);
        // Initialize Buttons Listener
        loginButton.setOnClickListener(new Listener());
        registerButton.setOnClickListener(new Listener());
        forgetPasswordButton.setOnClickListener(new Listener());
    }

    private class Listener implements View.OnClickListener, LoginApiHelper.LoginApiHelperListener, QueryApiHelper.OnGetBaseUserInfoListener {

        ProgressDialog progressDialog;

        @Override
        public void onLoginSuccess() {
            QueryApiHelper.getBaseUserInfo(this);
        }

        @Override
        public void onLoginFailure(int reason) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            switch (reason) {
                case LoginApiHelper.LoginFailureReason.EMPTY_USERNAME:
                    usernameEditText.setError(getResources().getString(R.string.username_can_not_be_empty));
                    break;
                case LoginApiHelper.LoginFailureReason.EMPTY_PASSWORD:
                    passwordEditText.setError(getResources().getString(R.string.password_can_not_be_empty));
                    break;
                case LoginApiHelper.LoginFailureReason.USERNAME_NOT_EXIST:
                    usernameEditText.setError(getResources().getString(R.string.username_not_exist));
                    break;
                case LoginApiHelper.LoginFailureReason.PASSWORD_ERROR:
                    passwordEditText.setError(getResources().getString(R.string.password_error));
                    break;
                case LoginApiHelper.LoginFailureReason.NETWORK_ERROR:
                    JUtils.Toast(getResources().getString(R.string.no_network));
                    break;
                case LoginApiHelper.LoginFailureReason.SERVER_ERROR:
                case LoginApiHelper.LoginFailureReason.TOKEN_WRONG:
                    break;
                default:
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_login:
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage(getResources().getString(R.string.logining));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    refreshCurrentLogin();
                    LoginApiHelper.login(this);
                    break;
                case R.id.button_register:
                    break;
                case R.id.button_forget_password:
                    break;
            }

        }

        @Override
        public void onGetBaseUserInfo() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            finish();
            JUtils.Toast(getResources().getString(R.string.login_success));
        }
    }

    /**
     * 从这个 Activity 中获取数据并放入 .data.storage.CurrentLogin 单例对象中存储
     */
    public void refreshCurrentLogin() {
        CurrentLogin.getInstance().username = usernameEditText.getText().toString();
        CurrentLogin.getInstance().password = passwordEditText.getText().toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CurrentStateStorager.save(outState);
        outState.putSerializable(CurrentLogin.class.getName(), CurrentLogin.getInstance());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CurrentStateStorager.restore(savedInstanceState);
        CurrentLogin.restoreInstance(savedInstanceState.getSerializable(CurrentLogin.class.getName()));
        if (CurrentLogin.getInstance().username != null) {
            usernameEditText.setText(CurrentLogin.getInstance().username);
        }
        if (CurrentLogin.getInstance().password != null) {
            passwordEditText.setText(CurrentLogin.getInstance().password);
        }
    }

    public static void startAction(Context context, String defaultUsername) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("defaultUsername", defaultUsername);
        context.startActivity(intent);
    }
}
