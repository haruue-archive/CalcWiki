package org.calcwiki.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;
import com.jude.utils.JUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.calcwiki.BuildConfig;
import org.calcwiki.R;
import org.calcwiki.data.network.api.MediaWikiInterceptor;
import org.calcwiki.data.network.helper.RegisterApiHelper;
import org.calcwiki.data.storage.CurrentRegister;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.ui.dialog.CaptchaDialog;

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
        registerButton.setOnClickListener(new Listener());
    }

    class Listener implements View.OnClickListener, RegisterApiHelper.RegisterApiHelperListener, CaptchaDialog.CaptchaDialogListener {

        ProgressDialog progressDialog;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_register:
                    refreshCurrentRegister();
                    progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setMessage(getResources().getString(R.string.registering));
                    progressDialog.show();
                    RegisterApiHelper.register(this);
                    break;
            }
        }

        @Override
        public void onRegisterSuccess() {
            JUtils.Toast(getResources().getString(R.string.create_account_success));
            CurrentRegister.clear();
            MediaWikiInterceptor.getInstance().clearAll();
            CurrentUser.getInstance().refreshCurrentUser();
            finish();
        }

        @Override
        public void onRegisterFailure(int reason) {
            MediaWikiInterceptor.getInstance().clearAll();
            progressDialog.dismiss();
            switch (reason) {
                 case RegisterApiHelper.RegisterFailureReason.EMPTY_USERNAME:
                     usernameEditText.setError(getResources().getString(R.string.username_can_not_be_empty));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.EMPTY_PASSWORD:
                     passwordEditText.setError(getResources().getString(R.string.password_can_not_be_empty));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.USERNAME_EXIST:
                     usernameEditText.setError(getResources().getString(R.string.username_exist));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.CAPTCHA_ERROR:
                     JUtils.Toast(getResources().getString(R.string.captcha_error));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.INVALID_USERNAME:
                     usernameEditText.setError(getResources().getString(R.string.username_invalid));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.NETWORK_ERROR:
                     JUtils.Toast(getResources().getString(R.string.no_network));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.SERVER_ERROR:
                     JUtils.Toast(getResources().getString(R.string.server_exception_and_try_again));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.TOKEN_WRONG:
                     JUtils.Toast(getResources().getString(R.string.server_exception_and_try_again));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.PASSWORD_NAME_MATCH:
                     passwordEditText.setError(getResources().getString(R.string.password_can_not_same_as_username));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.INVALID_EMAIL_ADDRESS_FORMAT:
                     emailEditText.setError(getResources().getString(R.string.invalid_email_address_format));
                     break;
                 case RegisterApiHelper.RegisterFailureReason.PASSWORD_TOO_SHORT:
                     passwordEditText.setError(getResources().getString(R.string.password_too_short));
                     break;

            }

        }

        @Override
        public void onRegisterCaptcha(String captchaId, String question) {
            progressDialog.dismiss();
            new CaptchaDialog().initialize(RegisterActivity.this, captchaId, question, this).show(getFragmentManager(), captchaId);
        }

        @Override
        public void onCaptchaDialogResult(String captchaId, String answerToCaptcha) {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.registering));
            progressDialog.show();
            RegisterApiHelper.register(this, captchaId, answerToCaptcha);
        }
    }

    public void refreshCurrentRegister() {
        CurrentRegister.getInstance().name = usernameEditText.getText().toString();
        CurrentRegister.getInstance().password = passwordEditText.getText().toString();
        CurrentRegister.getInstance().email = emailEditText.getText().toString();
        CurrentRegister.getInstance().realname = realNameEditText.getText().toString();
    }


    public static void startAction(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
