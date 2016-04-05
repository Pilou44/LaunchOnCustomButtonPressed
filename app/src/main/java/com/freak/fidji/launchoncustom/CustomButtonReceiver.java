package com.freak.fidji.launchoncustom;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class CustomButtonReceiver extends BroadcastReceiver {
    private static final String TAG = CustomButtonReceiver.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final String ACTION_BUTTON_1 = "com.parrot.action.CUSTOM_BUTTON_1";
    private static final String ACTION_BUTTON_2 = "com.parrot.action.CUSTOM_BUTTON_2";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG)
            Log.i(TAG, "Broadcast received");
        SharedPreferences preferences = context.getSharedPreferences(MainActivity.PREFERENCE_NAME, 0);
        ComponentName component = null;
        String action = "";
        if (intent.getAction().equals(ACTION_BUTTON_1)) {
            if (DEBUG)
                Log.i(TAG, "Button 1");
            action = preferences.getString(context.getString(R.string.key_package_1), context.getString(R.string.key_none));
            component = new ComponentName(action, preferences.getString(context.getString(R.string.key_class_1), context.getString(R.string.key_none)));
        }
        else if (intent.getAction().equals(ACTION_BUTTON_2)) {
            if (DEBUG)
                Log.i(TAG, "Button 2");
            action = preferences.getString(context.getString(R.string.key_package_2), context.getString(R.string.key_none));
            component = new ComponentName(action, preferences.getString(context.getString(R.string.key_class_2), context.getString(R.string.key_none)));
        }
        if (component != null) {
            Intent newIntent = new Intent();
            newIntent.setComponent(component);
            if (!action.equals("none")) {
                newIntent.setAction(action);
            }
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            try {
                context.startActivity(newIntent);
            }
            catch (Exception e) {
                Log.w(TAG, "No activity to launch");
                e.printStackTrace();
            }
        }
    }
}
