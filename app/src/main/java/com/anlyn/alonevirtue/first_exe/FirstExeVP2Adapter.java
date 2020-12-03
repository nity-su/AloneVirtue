package com.anlyn.alonevirtue.first_exe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.anlyn.alonevirtue.BR;
import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.contents.ui.video.VideoTouchControlListener;

import java.util.List;

public class FirstExeVP2Adapter extends RecyclerView.Adapter<FirstExeVP2Adapter.Holder> {
    private List<FirstExeCellUnit> list;

    public FirstExeVP2Adapter(List<FirstExeCellUnit> pictureList) {
        this.list = pictureList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.first_exe_cell,parent,false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ViewDataBinding binding;
        public Holder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(Object object){
            binding.setVariable(BR.firstExeUnit,object);
            binding.executePendingBindings();
        }
    }
}
