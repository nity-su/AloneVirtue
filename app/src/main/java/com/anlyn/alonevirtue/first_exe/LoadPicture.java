package com.anlyn.alonevirtue.first_exe;

import android.content.Context;
import android.content.res.TypedArray;

import com.anlyn.alonevirtue.R;

import java.util.ArrayList;
import java.util.List;

class LoadPicture {
    private List<FirstExeCellUnit> list;

    public LoadPicture(Context context) {
        list =new ArrayList<>();

        String[] strings = context.getResources().getStringArray(R.array.firstActivitytext);
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.drawable_array);

        for(int i=0;i<strings.length;i++){
            list.add(new FirstExeCellUnit(strings[i],typedArray.getResourceId(i,0)));
        }
    }

    public List<FirstExeCellUnit> getList() {
        return list;
    }
}
