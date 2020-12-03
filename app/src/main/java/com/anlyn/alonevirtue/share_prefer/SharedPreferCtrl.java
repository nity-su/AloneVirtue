package com.anlyn.alonevirtue.share_prefer;

import android.content.Context;
import android.content.SharedPreferences;

import com.anlyn.alonevirtue.R;
import static android.content.Context.MODE_PRIVATE;

public class SharedPreferCtrl {
    private Context context;
    private SharedPreferences preferences;
    public  SharedPreferCtrl(Context context,String name){
       this.context=context;
        preferences =  context.getSharedPreferences(name,MODE_PRIVATE);
    }
    //not recreate
    public void applyTheme(String name){
        boolean theme = preferences.getBoolean(name,false);
        if(theme) {
            context.setTheme(R.style.darkMode);
        }else {
            context.setTheme(R.style.lightMode);
        }
    }

    public boolean isOpenAppFirst(){
        return preferences.getBoolean("firstOpen",true);
    }

    public void appWasOpened(boolean choice){
        preferences.edit().putBoolean("firstOpen",choice).apply();
    }

    public void editApply(String name,boolean choice){
        preferences.edit().putBoolean(name,choice).apply();
    }
}
