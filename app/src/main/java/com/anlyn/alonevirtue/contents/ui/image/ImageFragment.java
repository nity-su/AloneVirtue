package com.anlyn.alonevirtue.contents.ui.image;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.json_mangement.JsonDataSave;
import com.anlyn.alonevirtue.databinding.FragmentImageBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
//https://codingwithmitch.com/blog/android-staggered-recyclerview/
public class ImageFragment extends Fragment {
    private final int REQUEST_CODE=1001;
    private ImageViewModel imageViewModel;
    private FragmentImageBinding binding;
    private String fileName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ImageFragmentArgs args = ImageFragmentArgs.fromBundle(getArguments());
        fileName=getContext().getFilesDir()+ File.separator+args.getTitle()+File.separator+"ImageUrls";

        binding=FragmentImageBinding.inflate(getLayoutInflater(),container,false);
        imageViewModel =
                new ViewModelProvider(this,new ImageViewModelFactory(this,fileName)).get(ImageViewModel.class);
        View root = binding.getRoot();

        Button addImageBtn =binding.AddFavorImageBtn;
        final RecyclerView recyclerView = binding.imageArrangeRv;
//        GridLayoutManager manager =new GridLayoutManager(getContext(),3);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(2));


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                ((StaggeredGridLayoutManager)recyclerView.getLayoutManager()).invalidateSpanAssignments();
//            }
//        });

//        smoothScroller.setTargetPosition(imageViewModel.getImageInfolist().size()-1);
//        manager.startSmoothScroll(smoothScroller);

        //empty none

        final ImageArrangeAdapter adapter =new ImageArrangeAdapter(ImageFragment.this,fileName,imageViewModel.getListMutableLiveData());
        recyclerView.setAdapter(adapter);
        imageViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<ImageInfo>>() {
            @Override
            public void onChanged(List<ImageInfo> imageInfos) {
                adapter.setList(imageInfos);
                recyclerView.scrollToPosition(imageInfos.size()-1);

            }
        });


        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewModel.getBuilder().observe(getViewLifecycleOwner(), new Observer<AlertDialog.Builder>() {
                    @Override
                    public void onChanged(AlertDialog.Builder builder) {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });

        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode == 1010){
            String url =data.getStringExtra("url");

            List<ImageInfo> list = imageViewModel.getImageInfolist();
            list.add(new ImageInfo("",url));
            //list update
            imageViewModel.setListMutableLiveData(list);
            //save
            save(list);
        }
    }

    void save(List<ImageInfo> list){
        ImageInfoJsonHandler handler =new ImageInfoJsonHandler();
        handler.setList(list);
        JSONObject jsonObject=(JSONObject) handler.encode(ImageInfoJsonHandler.OBJECT_TO_JSON);
        JsonDataSave save =new JsonDataSave();
        save.create(getContext(),fileName,jsonObject.toString());
    }

    @BindingAdapter(value = { "loadImageInImageFragment","fragment"},requireAll = false)
    public static void loadUrl(final ImageView view, final String url,Fragment fragment) {
        Log.d("url",url);

        RequestOptions options = new RequestOptions();
//        options.placeholder(R.drawable.ic_baseline_broken_image_24);
        options.error(R.drawable.ic_baseline_broken_image_24);
        options.fallback(R.drawable.ic_baseline_broken_image_24);
        Glide.with(fragment)
                .setDefaultRequestOptions(options)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }
    @BindingAdapter({"touchListener"})
    public static void setListener(ImageView view, ImageTouchControlListener listener){
        view.setOnClickListener(listener);
        view.setOnLongClickListener(listener);
    }

}