package com.anlyn.alonevirtue.contents.ui.image;

import com.anlyn.alonevirtue.json_mangement.JsonTransHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImageInfoJsonHandler extends JsonTransHandler {
    private List<ImageInfo> list;
    private String title="ImageUrls";
    private JSONObject jsonObject;
    @Override
    public Object encode(int type) {
        if(OBJECT_TO_JSON == type){
            return listToJson();
        }

        return null;
    }

    @Override
    public Object decode(int type) {

        if(JSON_TO_LIST == type)
            return jsonToList();

        return null;
    }

    List jsonToList(){
        List<ImageInfo> list = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(title);
            Gson gson =new Gson();
            for(int i=0;i<jsonArray.length();i++){
                String strJson = jsonArray.get(i).toString();
                ImageInfo info= gson.fromJson(strJson,ImageInfo.class);
                list.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    JSONObject listToJson(){

        JSONObject result=new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(ImageInfo info:list){
            Gson gson =new Gson();
            try {
                JSONObject jsonObj= new JSONObject(gson.toJson(info));
                jsonArray.put(jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            result.put(title,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }


    public void setList(List<ImageInfo> list) {
        this.list = list;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
