package com.anlyn.alonevirtue.contents.ui.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anlyn.alonevirtue.contents.ContentsActivity;
import com.anlyn.alonevirtue.json_mangement.JsonDataSave;
import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.FragmentVideoBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.greenfrvr.hashtagview.HashtagView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//https://stackoverflow.com/questions/51737667/since-the-android-getfragmentmanager-api-is-deprecated-is-there-any-alternati
//https://eso0609.tistory.com/72
public class VideoFragment extends Fragment implements ProgressTaskVideo.DataSave {

    private VideoViewModel viewModel;
    private final int REQUET_CODE = 1001;
    private String videoInfoFileName;
    private String fileBasePath;
    private ContentsAdapter adapter;
    private Fragment fragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        fragment=this;

        VideoFragmentArgs args = VideoFragmentArgs.fromBundle(getArguments());
        fileBasePath=getContext().getFilesDir()+ File.separator+args.getTitle();
        videoInfoFileName=fileBasePath+File.separator+"VideoInfo";
        final FragmentVideoBinding binding =FragmentVideoBinding.inflate(getLayoutInflater(),container,false);
        viewModel =
                new ViewModelProvider(this,new VideoViewModelFatory(VideoFragment.this,videoInfoFileName)).get(VideoViewModel.class);
        View root = binding.getRoot();
        //final TextView textView = binding.textHome;
        Button btn  = binding.addVideoContetnsBtn;
        final RecyclerView recyclerView =binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContentsAdapter(fileBasePath,getContext(),VideoFragment.this);
        adapter.setLiveData(viewModel.getMutableLiveDataInfoList());
        recyclerView.setAdapter(adapter);

//https://www.vogella.com/tutorials/AndroidIntent/article.html
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getDialog().observe(getViewLifecycleOwner(), new Observer<AlertDialog.Builder>() {
                    @Override
                    public void onChanged(AlertDialog.Builder builder) {
                        AlertDialog dialog=builder.create();
                        dialog.show();
                    }
                });

            }
        });

        viewModel.getMutableLiveDataInfoList().observe(getViewLifecycleOwner(), new Observer<List<VideoContentsInfo>>() {
            @Override
            public void onChanged(List<VideoContentsInfo> videoContentsInfos) {
                adapter.setList(videoContentsInfos);
                recyclerView.scrollToPosition(videoContentsInfos.size()-1);
            }
        });



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUET_CODE && resultCode == 1010) {

            ProgressTaskVideo.TitleAuto titleAuto = (ProgressTaskVideo.TitleAuto) getContext();
            final VideoContentsInfo info = new VideoContentsInfo();
            info.setTitle(data.getStringExtra("name"));
            info.setUrl(data.getStringExtra("url"));
            info.setUriType(data.getStringExtra("urlType"));
            info.setTag((List<String>) data.getSerializableExtra("tags"));
            titleAuto.setResult(data.getBooleanExtra("titleAuto",false));
            List<VideoContentsInfo> list=viewModel.getValue();
            list.add(info);

            MutableLiveData<List<VideoContentsInfo>> contents= viewModel.getMutableLiveDataInfoList();
            contents.setValue(list);
            //json file save
            if(info.getUriType().equals("GALLERY"))
            save(list);

        }
    }

    public void showDialog(final Context context, View view) {
        Dialog dialog = new Dialog(context);
        dialog.show();
    }

    void save(List<VideoContentsInfo> list){
        VideoContInfoJsonHandler handler = new VideoContInfoJsonHandler();
        handler.setList(list);
        JSONObject jsonObject = (JSONObject) handler.encode(VideoContInfoJsonHandler.OBJECT_TO_JSON);
        JsonDataSave save = new JsonDataSave();
        save.create(getContext(),videoInfoFileName,jsonObject.toString());
    }

    @Override
    public void save(String fileBasePath, List<VideoContentsInfo> list) {
        String savePath=fileBasePath+File.separator+"VideoInfo";
        VideoContInfoJsonHandler handler = new VideoContInfoJsonHandler();
        handler.setList(list);
        JSONObject jsonObject = (JSONObject) handler.encode(VideoContInfoJsonHandler.OBJECT_TO_JSON);
        JsonDataSave save = new JsonDataSave();
        save.create(getContext(),savePath,jsonObject.toString());
    }

    @BindingAdapter({ "setWebViewClient" })
    public static void setWebViewClient(WebView view, WebViewClient client) {
        view.setWebViewClient(client);
    }
//    String path= new OGTag().getOGTag("https://www.youtube.com/watch?v=cE4noLw95Yc",null);
//                    Glide.with(context).load(path).into(view);
    @BindingAdapter(value = "setListener")
    public static void setListener(ViewGroup viewGroup,VideoTouchControlListener listener){
//        ViewGroup viewGroup =(ViewGroup) imageView.getParent();
        viewGroup.setOnClickListener(listener);
        viewGroup.setOnLongClickListener(listener);
    }
//databinding gallery , network
    @BindingAdapter("android:text")
    public static void setText(TextView view,String title){
        view.setText(title);
    }

    @BindingAdapter(value ={ "videoContentsInfo" , "videoBasePath","videoList","fragment" } , requireAll = false)
    public static void loadUrl(final ImageView view,final VideoContentsInfo info,final String basePath,final List<VideoContentsInfo> list,final Fragment fragment) {
        Log.d("id",view.getId()+"");
        final String url =info.getUrl();
        final String videoType = info.getUriType();
        final Activity activity = ((Activity)fragment.getContext());
        final String urlPath = url.replace("/", "").replace("\\", "");
//        final String finalPath = basePath + File.separator + urlPath;

            //http://anitoy.pe.kr/preview-url-content/
            if (videoType.equals("GALLERY")) {
                new Thread() {
                    @Override
                    public void run() {
                        //중복
                        //이미지찾기
                        //final String uriToPath = convertMediaUriToPath(fragment.getContext(), uri);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(uriToPath, MediaStore.Images.Thumbnails.MINI_KIND);
                                RequestOptions options = new RequestOptions();
                                options.placeholder(R.drawable.ic_baseline_broken_image_24);
                                options.error(R.drawable.ic_baseline_broken_image_24);
                                options.fallback(R.drawable.ic_baseline_broken_image_24);

                                Glide.with(fragment)
                                        .setDefaultRequestOptions(options )
                                        .load(url)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .centerCrop()
                                        .transform(new CenterCrop(),new RoundedCorners(14))
                                        .into(view);

                            }
                        });
                    }

                }.start();


            }

        if(videoType.equals("NETWORK")) {
            ProgressTaskVideo taskVideo = new ProgressTaskVideo(view,info,basePath,fragment,list);
            taskVideo.execute();
        }

    }
    @BindingAdapter("tags")
    public static void setTags(HashtagView hashtagView, List<String> list){
        final HashtagView.DataTransform<String> HASH = new HashtagView.DataTransform<String>() {
            @Override
            public CharSequence prepare(String item) {
                SpannableString spannableString = new SpannableString("#" + item);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#483BFF")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //
                return spannableString;
            }
        };


        if(list==null) {
            list=new ArrayList<>();
            hashtagView.setData(list,HASH);
            return;
        }

        hashtagView.setData(list,HASH);
    }


    public static String convertMediaUriToPath(Context context,Uri uri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    public Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
//    @Override
//    public Boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getActivity().getMenuInflater();
//        menuInflater.inflate(R.menu.menu_search, menu);
//        Log.d("eee","eee");
//        MenuItem searchItem = menu.findItem(R.id.toolbar);
//
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        androidx.appcompat.widget.SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        }
//        super.onCreateOptionsMenu(menu,menuInflater);
//        return true;
//    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_serach);
        //https://stackoverflow.com/questions/48989235/menuitemcompat-setonactionexpandlistener-deprecated/48989340#48989340
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                adapter.cancelFilter();
                return true;
            }
        });

//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

            searchView = (SearchView) searchItem.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    query = query.replace(" ","");
                    if(query.equals(""))
                        return false;
                    adapter.filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        }
        super.onCreateOptionsMenu(menu,menuInflater);
    }
}
