package cn.edu.nuc.seeworld.fg;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nuc.seeworld.R;

/**
 * Created by lenovo on 2015/9/9.
 */
public class fg_Menu extends Fragment implements View.OnClickListener{
    private MenuOnclickListener menuOnclickListener;
    public  interface MenuOnclickListener{
        void MenuOnclick(View view);
    }
    public  void setMenuOnclickListener(MenuOnclickListener menuOnclickListener){
        this.menuOnclickListener=menuOnclickListener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.print("进入菜单fragment");
        View view =inflater.inflate(R.layout.left_menu,container,false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
            if(menuOnclickListener!=null){
                menuOnclickListener.MenuOnclick(v);
            }
    }
}
