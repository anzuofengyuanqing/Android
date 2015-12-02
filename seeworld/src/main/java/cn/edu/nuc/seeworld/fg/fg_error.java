package cn.edu.nuc.seeworld.fg;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nuc.seeworld.R;

/**
 * Created by lenovo on 2015/9/23.
 */
public class fg_error extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.error,container,false);
        return view;
    }
}
