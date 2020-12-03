package com.anlyn.alonevirtue.contents.ui.video;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebView;

import com.anlyn.alonevirtue.R;

class LoadVideoFromUrl {
    private WebView wv;
    private String url;
    public LoadVideoFromUrl(WebView wv,String url,Context context) {
        init(wv,url,context);
    }

    void init(WebView wv,String url, Context context){
        //this.wv=wv;
        Log.d("width",""+wv.getWidth());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        wv.setWebViewClient(new WebViewClient() {
//            public void onPageFinished(WebView view, String url) {wv.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()"); }
//        });
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = getHTML(url,context);
        wv.setWebChromeClient( new FullscreenableChromeClient(((Activity)context)));
        //wv.loadUrl(html);
        wv.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }

    String getHTML(String url,Context context){
        DisplayMetrics display=context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        url="\""+url+"\"";
        //String url= "\"https://www.youtube.com/embed/aXAzTUbUB6Y\"";
        Object[] objects=new Object[1];
        objects[0]=url;
        String htmlFormat = context.getString(R.string.video_htmp_format);
        String html=String.format(htmlFormat,objects);
        // html = String.format(html,url);
        //String html = "<iframe width=\"100%\" height=\"100%\"  src=\"https://www.youtube.com/embed/aXAzTUbUB6Y\"  encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
        //String html = "<object data=\"https://www.youtube.com/embed/aXAzTUbUB6Y\" width=\"560\" height=\"315\" encrypted-media;\" allowfullscreen></object>";
        return html;
    }
}
