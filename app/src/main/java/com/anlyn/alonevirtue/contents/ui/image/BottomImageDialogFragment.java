package com.anlyn.alonevirtue.contents.ui.image;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.databinding.BottomDialogImageFrameBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment;

import java.util.List;

public class BottomImageDialogFragment extends BottomSheetDialogFragment {
    private BottomItemListener mListener;
    private MutableLiveData<List<ImageInfo>> urlList;
    private String baseUrl;
    private String pickUrl;
    BottomImageDialogFragment(MutableLiveData<List<ImageInfo>> urlList, String pickUrl, String baseUrl){
        this.urlList = urlList;
        this.baseUrl = baseUrl;
        this.pickUrl = pickUrl;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomDialogImageFrameBinding binding = BottomDialogImageFrameBinding.inflate(inflater,container,false);
        TextView tv = binding.tvBtnRemovePhoto;
        TextView tvOpenImage = binding.tvBtnOpenOriginImage;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.imageOnclickListener(getUrlList(),pickUrl,getContext(),baseUrl);
                    }
                }).start();

                dismiss();
            }
        });

        tvOpenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.originalImageOpenListener(pickUrl,getContext());
            }
        });

        return binding.getRoot();
    }
    public interface BottomItemListener{
        void imageOnclickListener(MutableLiveData<List<ImageInfo>> urlList, String url, Context context, String baseUrl);
        void originalImageOpenListener(String url, Context context);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomItemListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }

    }

    public MutableLiveData<List<ImageInfo>> getUrlList() {
        return urlList;
    }

    //    public void setImageInfos(List<ImageInfo> imageInfos) {
//        this.imageInfos = imageInfos;
//    }
}
