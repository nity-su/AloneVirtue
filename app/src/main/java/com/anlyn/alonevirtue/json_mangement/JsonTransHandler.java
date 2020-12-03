package com.anlyn.alonevirtue.json_mangement;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class JsonTransHandler {

    @IntDef({JSON_TO_LIST,OBJECT_TO_JSON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface encodeType {}

    public static final int JSON_TO_LIST=0;
    public static final int OBJECT_TO_JSON=1;
    @encodeType
    private int type;
    public abstract Object encode(@encodeType int type);
    public abstract Object decode(@encodeType int type);
}
