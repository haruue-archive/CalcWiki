package org.calcwiki.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;
import com.jude.utils.JUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.calcwiki.BuildConfig;
import org.calcwiki.R;
import org.calcwiki.data.network.helper.RegisterApiHelper;

import retrofit2.http.POST;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText usernameEditText;
    MaterialEditText passwordEditText;
    MaterialEditText confirmPasswordEditText;
    MaterialEditText emailEditText;
    MaterialEditText realNameEditText;
    ButtonRectangle registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Utils
        JUtils.setDebug(BuildConfig.DEBUG, this.getClass().getName());
        // Initialize UI
        initializeUI();
    }

    private void initializeUI() {
        usernameEditText = (MaterialEditText) findViewById(R.id.edittext_username);
        passwordEditText = (MaterialEditText) findViewById(R.id.edittext_password);
        confirmPasswordEditText = (MaterialEditText) findViewById(R.id.edittext_password_again);
        emailEditText = (MaterialEditText) findViewById(R.id.edittext_email);
        realNameEditText = (MaterialEditText) findViewById(R.id.edittext_realname);
        registerButton = (ButtonRectangle) findViewById(R.id.button_register);
    }

    class Listener implements View.OnClickListener, RegisterApiHelper.RegisterApiHelperListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_register:
                    RegisterApiHelper.register(this);
                    break;

            }
        }

        @Override
        public void onRegisterSuccess() {

        }

        @Override
        public void onRegisterFailure(int reason) {

        }

        @Override
        public void onRegisterCaptcha(String captchaId, String question) {

        }
    }


    public static void startAction(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
