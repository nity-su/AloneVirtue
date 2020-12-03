package com.anlyn.alonevirtue.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;

import androidx.appcompat.app.AppCompatActivity;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.contents.ui.video.AddVideoInfoActivity;
import com.anlyn.alonevirtue.share_prefer.SharedPreferCtrl;

class ThemeSetDialog {
    private String result[]= new String[1];
    private AlertDialog.Builder builder;
    ThemeSetDialog(final Context context){
    final String items[] = context.getResources().getStringArray(R.array.theme_setting);

        builder=new AlertDialog.Builder(context).setTitle("불러오기").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result[0] =items[which];

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                SharedPreferences preferences = ((AppCompatActivity)context).getPreferences(Context.MODE_PRIVATE);
                if (result[0].equals(items[0])){
                    SharedPreferCtrl ctrl = new SharedPreferCtrl(context,"theme");
                    ctrl.editApply("theme",true);
                    ((AppCompatActivity) context).recreate();
                }
                if(result[0].equals(items[1])){
                    SharedPreferCtrl ctrl = new SharedPreferCtrl(context,"theme");
                    ctrl.editApply("theme",false);
                    ((AppCompatActivity) context).recreate();
                }


            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }
}
