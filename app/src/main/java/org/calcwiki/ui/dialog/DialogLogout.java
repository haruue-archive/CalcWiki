package org.calcwiki.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.network.helper.LogoutApiHelper;
import org.calcwiki.data.storage.CurrentUser;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 登出 Dialog
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class DialogLogout {

    MaterialDialog dialog;
    Context context;
    ProgressDialog progressDialog;

    public DialogLogout(Context context) {
        this.context = context;
        dialog = new MaterialDialog(context)
                .setTitle(R.string.are_you_sure_logout)
                .setMessage(R.string.no_person_data_will_be_persist_on_this_device)
                .setPositiveButton(R.string.yes, new Listener())
                .setNegativeButton(R.string.cancel, new Listener());
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    class Listener implements View.OnClickListener, LogoutApiHelper.OnLogoutListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_p:
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage(context.getResources().getString(R.string.logouting));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    LogoutApiHelper.logout(this);
                    break;
                case R.id.btn_n:
                    dialog.dismiss();
                    break;
            }

        }

        @Override
        public void onLogoutSuccess() {
            CurrentUser.getInstance().onLogout();
            dialog.dismiss();
            progressDialog.dismiss();
        }

        @Override
        public void onLogoutFailure() {
            dialog.dismiss();
            JUtils.Toast(context.getResources().getString(R.string.no_network));
        }
    }


}
