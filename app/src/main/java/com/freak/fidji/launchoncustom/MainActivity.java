package com.freak.fidji.launchoncustom;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends FragmentActivity implements ActivityDialogListener{

    public static final String PREFERENCE_NAME = "LaunchOnCustomPressed";
    private ImageButton app1Icone, app2Icone;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);

        app1Icone = (ImageButton) findViewById(R.id.app1_icone);
        app1Icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDialog dialog = new ActivityDialog();
                dialog.setButton(1);
                dialog.addActivityDialogListener(MainActivity.this);
                dialog.show(getSupportFragmentManager(), "activity1");
            }
        });
        app2Icone = (ImageButton) findViewById(R.id.app2_icone);
        app2Icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDialog dialog = new ActivityDialog();
                dialog.setButton(2);
                dialog.addActivityDialogListener(MainActivity.this);
                dialog.show(getSupportFragmentManager(), "activity2");
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
            ComponentName component1 = new ComponentName(preferences.getString("package1", "none"), preferences.getString("class1", "none"));

            if (component1.getPackageName().equals("none")){
                app1Icone.setImageResource(R.drawable.none);
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

                    app1Icone.setImageDrawable(d);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    app1Icone.setImageResource(R.drawable.none);
                }
            }
        }
        else if (buttonNumber == 2) {
            ComponentName component2 = new ComponentName(preferences.getString("package2", "none"), preferences.getString("class2", "none"));

            if (component2.getPackageName().equals("none")){
                app2Icone.setImageResource(R.drawable.none);
            }
            else {
                try {
                    app2Icone.setImageDrawable(getPackageManager().getActivityIcon(component2));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    app2Icone.setImageResource(R.drawable.none);
                }
            }
        }
    }
}
