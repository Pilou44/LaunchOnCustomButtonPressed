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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG)
            Log.i(TAG, "Broadcast received");
        SharedPreferences preferences = context.getSharedPreferences(MainActivity.PREFERENCE_NAME, 0);
        ComponentName component = null;
        String action = "";
        if (intent.getAction().equals("com.parrot.action.CUSTOM_BUTTON_1")) {
            if (DEBUG)
                Log.i(TAG, "Button 1");
            component = new ComponentName(preferences.getString("package1", "none"), preferences.getString("class1", "none"));
            action = preferences.getString("package1", "none");
        }
        else if (intent.getAction().equals("com.parrot.action.CUSTOM_BUTTON_2")) {
            if (DEBUG)
                Log.i(TAG, "Button 2");
            component = new ComponentName(preferences.getString("package2", "none"), preferences.getString("class2", "none"));
            action = preferences.getString("package2", "none");
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
