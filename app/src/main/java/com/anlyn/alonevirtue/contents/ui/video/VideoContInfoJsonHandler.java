package com.anlyn.alonevirtue.contents.ui.video;

import com.anlyn.alonevirtue.json_mangement.JsonTransHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoContInfoJsonHandler extends JsonTransHandler {
    private JSONObject jsonObject;
    private String name = "videoInfo";
    private List<VideoContentsInfo> list;
    @Override
    public Object encode(int type) {
        if(type == OBJECT_TO_JSON){
            return listToJson() ;
        }
        return null;
    }

    @Override
    public Object decode(int type) {
            if(type==JSON_TO_LIST) {
              return jsonToList();
            }


        return null;
    }

    JSONObject listToJson(){
        JSONArray jsonArray =new JSONArray();

        for(VideoContentsInfo obj : list) {
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
            result.put(name, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    List jsonToList(){
        List<VideoContentsInfo> list = new ArrayList<>();
        JSONArray array = null;
        try {
            array = jsonObject.getJSONArray(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < array.length(); i++) {
                String infoStr = "";
                infoStr=array.get(i).toString();
                Gson gson = new Gson();
                VideoContentsInfo info_object =gson.fromJson(infoStr,VideoContentsInfo.class);
                list.add(info_object);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return list;
    }


    public void setList(List<VideoContentsInfo> list) {
        this.list = list;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

}
