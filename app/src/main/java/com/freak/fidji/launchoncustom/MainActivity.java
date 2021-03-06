package com.freak.fidji.launchoncustom;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends FragmentActivity implements ActivityDialogListener{

    public static final String PREFERENCE_NAME = "LaunchOnCustomPressed";
    private ImageButton app1Icon, app2Icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app1Icon = (ImageButton) findViewById(R.id.app1_icon);
        app1Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDialog dialog = new ActivityDialog();
                dialog.setButton(1);
                dialog.addActivityDialogListener(MainActivity.this);
                dialog.show(getSupportFragmentManager(), getString(R.string.key_activity_1));
            }
        });
        app2Icon = (ImageButton) findViewById(R.id.app2_icon);
        app2Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDialog dialog = new ActivityDialog();
                dialog.setButton(2);
                dialog.addActivityDialogListener(MainActivity.this);
                dialog.show(getSupportFragmentManager(), getString(R.string.key_activity_2));
            }
        });

        refreshButton(1);
        refreshButton(2);

    }

    @Override
    public void onDismissActivityDialog(int buttonNumber) {
        Log.i("MainActivity", "Dismiss dialog for button " + buttonNumber);
        refreshButton(buttonNumber);
    }

    private void refreshButton(int buttonNumber) {
        Log.i("MainActivity", "Refresh button " + buttonNumber);
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, 0);
        if (buttonNumber == 1) {
            ComponentName component1 = new ComponentName(preferences.getString(getString(R.string.key_package_1), getString(R.string.key_none)), preferences.getString(getString(R.string.key_class_1), getString(R.string.key_none)));

            if (component1.getPackageName().equals(getString(R.string.key_none))){
                app1Icon.setImageResource(R.drawable.none);
            }
            else {
                try {
                    Drawable icon = getPackageManager().getActivityIcon(component1);
                    Bitmap bmp;
                    if(icon instanceof BitmapDrawable) {
                        bmp  = ((BitmapDrawable)icon).getBitmap();
                    }
                    else{
                        Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(),icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        icon.draw(canvas);
                        bmp = bitmap;
                    }

                    Bitmap b = Bitmap.createScaledBitmap(bmp, 72, 72, true);
                    Drawable d = new BitmapDrawable(this.getResources(), b);

                    app1Icon.setImageDrawable(d);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    app1Icon.setImageResource(R.drawable.none);
                }
            }
        }
        else if (buttonNumber == 2) {
            ComponentName component2 = new ComponentName(preferences.getString(getString(R.string.key_package_2), getString(R.string.key_none)), preferences.getString(getString(R.string.key_class_2), getString(R.string.key_none)));

            if (component2.getPackageName().equals(getString(R.string.key_none))){
                app2Icon.setImageResource(R.drawable.none);
            }
            else {
                try {
                    app2Icon.setImageDrawable(getPackageManager().getActivityIcon(component2));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    app2Icon.setImageResource(R.drawable.none);
                }
            }
        }
    }
}
