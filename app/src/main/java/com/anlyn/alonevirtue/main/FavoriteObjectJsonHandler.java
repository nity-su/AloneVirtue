package com.anlyn.alonevirtue.main;

import android.util.Log;

import com.anlyn.alonevirtue.json_mangement.JsonTransHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class FavoriteObjectJsonHandler extends JsonTransHandler {

    private JSONObject jsonObject;
    private List<FavoriteObjectItem> list;

    @Override
    public Object encode(@encodeType int type) {
        if(list==null)
            return null;

        if(type==OBJECT_TO_JSON)
            return listToJson(list);

        return null;
    }

    @Override
    public Object decode(@encodeType int type) {

        if(type==JSON_TO_LIST)
            return jsonToList();

        return null;
    }

    private List jsonToList(){
        List<FavoriteObjectItem> result = new ArrayList<>();
        JSONArray jsonArray=null;
        try {
            jsonArray =jsonObject.getJSONArray("FavoriteObject");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) jsonArray.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            FavoriteObjectItem parse = gson.fromJson(jsonObject.toString(), FavoriteObjectItem.class);
            result.add(parse);
            Log.d("size",result.size()+"");
        }
        return result;
    }

    private JSONObject listToJson(List<FavoriteObjectItem> list){
        JSONArray jsonArray =new JSONArray();

        for(FavoriteObjectItem obj : list) {
            Gson gson = new Gson();
            try {
                JSONObject jsonObj = new JSONObject(gson.toJson(obj));
                jsonArray.put(jsonObj);
            } catch (JSONException e) {
                return null;
            }
        }

        JSONObject result = null;
            try {
                result = new JSONObject();
                result.put("FavoriteObject", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return result;

    }
    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setList(List<FavoriteObjectItem> list) {
        this.list = list;
    }

}
