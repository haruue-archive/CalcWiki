package org.calcwiki.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

public class NetworkConnectivityReceiver extends BroadcastReceiver {

    static ArrayList<OnNetworkStateChangeListener> listenerArray = new ArrayList<OnNetworkStateChangeListener>(0);

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();

        if (!listenerArray.isEmpty()) {
            boolean hasNetwork;
            hasNetwork = activeInfo != null;
            for (OnNetworkStateChangeListener i: listenerArray) {
                i.onNetworkStateChange(hasNetwork);
            }
        }
    }

    public interface OnNetworkStateChangeListener {
        void onNetworkStateChange(boolean hasNetwork);
    }

    public static void addNetworkStateChangeListener(OnNetworkStateChangeListener listener) {
        listenerArray.add(listener);
    }

    public static void removeNetworkStateChangeListener(OnNetworkStateChangeListener listener) {
        listenerArray.remove(listener);
    }
}
