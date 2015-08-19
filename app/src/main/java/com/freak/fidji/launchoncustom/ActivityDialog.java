package com.freak.fidji.launchoncustom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ActivityDialog extends DialogFragment {

    private int buttonNumber = -1;
    private List<ResolveInfo> resInfos;
    private ActivityDialogListener listener;

    public void setButton(int button) {
        this.buttonNumber = button;
    }

    public class ActivityAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final List<ResolveInfo> activities;
        private final PackageManager pm = getActivity().getPackageManager();

        public ActivityAdapter(Context context, List<ResolveInfo> resInfos) {
            super(context, android.R.layout.select_dialog_item);
            this.context = context;
            this.activities = resInfos;

            for (int i = 0 ; i < activities.size() ; i++) {
                try {
                    this.add(activities.get(i).activityInfo.nonLocalizedLabel.toString());
                } catch (NullPointerException e) {
                    this.add(pm.getApplicationLabel(activities.get(i).activityInfo.applicationInfo).toString());
                }
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Drawable icon = activities.get(position).activityInfo.loadIcon(pm);
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

            Bitmap b = Bitmap.createScaledBitmap(bmp, 48, 48, true);
            Drawable d = new BitmapDrawable(context.getResources(), b);

            View view = super.getView(position, convertView, parent);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
            textView.setCompoundDrawablePadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
            return view;
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d("ActivityDialog", "Retrieve application list with other method");
        final PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        resInfos = packageManager.queryIntentActivities(intent, 0);
        Log.d("ActivityDialog", resInfos.size() + " applications found");
        ActivityAdapter adapter = new ActivityAdapter(getActivity(), resInfos);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.pick_app);
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.PREFERENCE_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("package" + buttonNumber, resInfos.get(item).activityInfo.packageName);
                editor.putString("class" + buttonNumber, resInfos.get(item).activityInfo.name);
                editor.apply();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }

        );

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (listener != null) {
            listener.onDismissActivityDialog(buttonNumber);
        }
        super.onDismiss(dialog);
    }

    public void addActivityDialogListener(ActivityDialogListener listener) {
        this.listener = listener;
    }
}
