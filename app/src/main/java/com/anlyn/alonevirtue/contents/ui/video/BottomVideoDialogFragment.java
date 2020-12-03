package com.anlyn.alonevirtue.contents.ui.video;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.BottomDialogVideoFrameBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomVideoDialogFragment extends BottomSheetDialogFragment {
    private String url;
    private Context context;
    private String basePath;
    private MutableLiveData<List<VideoContentsInfo>> list;
    private BottomItemListener listener;
    private View imageView;
    private int position;

    public BottomVideoDialogFragment(String url, Context context, String basePath, MutableLiveData<List<VideoContentsInfo>> list,int position,View imageView) {
        this.url = url;
        this.context = context;
        this.basePath = basePath;
        this.list = list;
        this.imageView=imageView;
        this.position = position;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomItemListener) context;
        }catch (ClassCastException e){
        throw new ClassCastException(context.toString());
    }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomDialogVideoFrameBinding binding = BottomDialogVideoFrameBinding.inflate(inflater,container,false);
        TextView delTextView = binding.tvBtnRemoveVideo;
        TextView upTextView = binding.tvBtnPositionUp;

        delTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteListener(list,context,url,basePath);
                dismiss();
            }
        });

        upTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changePosToTop(list,context,position,basePath);
                dismiss();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        imageView.setAlpha(1);
    }

    public interface BottomItemListener{
        void deleteListener(MutableLiveData<List<VideoContentsInfo>> list,Context context,String pickUrl,String baseUrl);
        void changePosToTop(MutableLiveData<List<VideoContentsInfo>> list,Context context,int position,String basePath);
    }

}
