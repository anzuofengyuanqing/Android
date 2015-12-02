package cn.edu.nuc.seeworld.fg;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.edu.nuc.seeworld.R;
import cn.edu.nuc.seeworld.YOUJIActivity;
import cn.edu.nuc.seeworld.findActivity;

/**
 * Created by lenovo on 2015/9/9.
 */
public class fg_Main extends Fragment {
    ImageView button;
    ImageView btn_find;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.main,container,false);
        button= (ImageView) view.findViewById(R.id.btn_youji);
        btn_find= (ImageView) view.findViewById(R.id.btn_find);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toYOUJI();
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFind();
            }
        });
        return view;
    }

    private void toYOUJI(){
        Intent intent=new Intent(this.getActivity(), YOUJIActivity.class);
        startActivity(intent);
    }
    private void toFind(){
        Intent intent=new Intent(this.getActivity(), findActivity.class);
        startActivity(intent);
    }

}
