package com.anlyn.alonevirtue.contents.ui.video;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

class OGTag {

    public VideoContentsInfo getOGTag(String url, final OGTag ret) {
        // 입력받은 url에 해당하는 html을 요청하고 OGTag들을 가져온다.
        VideoContentsInfo info = new VideoContentsInfo();
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            Elements ogTags = doc.select("meta[property^=og:]");
            if (ogTags.size() == 0) {
                return null;
            }

            // 필요한 OGTag를 추려낸다
            for (int i = 0; i < ogTags.size(); i++) {
                Element tag = ogTags.get(i);

                String text = tag.attr("property");
                if ("og:image".equals(text)) {
                    info.setUrl(tag.attr("content"));

//                } else if ("og:image".equals(text)) {
//                    ret.setOgImageUrl(tag.attr("content"));
//                } else if ("og:description".equals(text)) {
//                    ret.setOgDescription(tag.attr("content"));
                } else if ("og:title".equals(text)) {
                    info.setTitle(tag.attr("content"));
                }
            }
        } catch (IOException e) {
            return null;
        }
        return info;
    }
}

