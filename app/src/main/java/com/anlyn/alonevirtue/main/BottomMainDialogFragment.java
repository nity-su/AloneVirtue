package com.anlyn.alonevirtue.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.databinding.BottomDialogMainFrameBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomMainDialogFragment extends BottomSheetDialogFragment {
    private MutableLiveData<List<FavoriteObjectItem>> liveData;
    private int position;
    private String folder;
    private BottomMainListener listener;
    private String imagePath;
    private String basePath;
    public BottomMainDialogFragment(MutableLiveData<List<FavoriteObjectItem>> liveData, int position, String folder,String imagePath,String basePath) {
        this.liveData = liveData;
        this.position = position;
        this.folder = folder;
        this.imagePath = imagePath;
        this.basePath = basePath;
    }

    @Override
     public void onAttach(@NonNull Context context) {
         super.onAttach(context);
         listener = (BottomMainListener) context;
     }

     @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomDialogMainFrameBinding binding = BottomDialogMainFrameBinding.inflate(getLayoutInflater());
         binding.tvMbtnRemoveObject.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Context context = v.getContext();
                 listener.deleteListener(context,folder,imagePath,position,liveData,basePath);
                dismiss();
             }
         });
        return binding.getRoot();
    }

    public interface BottomMainListener{
         void deleteListener(Context context,String folderPath,String imagePath, int position, MutableLiveData<List<FavoriteObjectItem>> liveData,String basePath);
    }
}
