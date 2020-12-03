package com.anlyn.alonevirtue.contents.ui.notifications;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.FragmentNotificationsBinding;
//https://stackoverflow.com/questions/34706399/how-to-use-data-binding-with-fragment
//https://charko.tistory.com/13
public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        FragmentNotificationsBinding binding = FragmentNotificationsBinding.inflate(getLayoutInflater(),container,false);

        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = binding.getRoot();
        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}