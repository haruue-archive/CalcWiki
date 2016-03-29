package org.calcwiki.ui.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.calcwiki.R;

/**
 * 验证码的 Dialog
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CaptchaDialog extends DialogFragment {

    String question;
    String captchaId;
    Context context;
    CaptchaDialogListener listener;
    View view;
    MaterialEditText answerEditText;

    public interface CaptchaDialogListener {
        void onCaptchaDialogResult(String captchaId, String answerToCaptcha);
    }

    public CaptchaDialog initialize(Context context, String captchaId, String question, CaptchaDialogListener listener) {
        this.context = context;
        this.listener = listener;
        this.question = question;
        this.captchaId = captchaId;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_captcha, container);
        answerEditText = (MaterialEditText) view.findViewById(R.id.edittext_answer);
        // Set question
        ((TextView) view.findViewById(R.id.captcha_question)).setText(question);
        // Buttons
        view.findViewById(R.id.button_continue).setOnClickListener(new Listener());
        view.findViewById(R.id.button_cancel).setOnClickListener(new Listener());
        return view;
    }

    public class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_continue:
                    getDialog().dismiss();
                    listener.onCaptchaDialogResult(captchaId, answerEditText.getText().toString());
                    break;
                case R.id.button_cancel:
                    getDialog().cancel();
                    break;
            }

        }
    }


}
