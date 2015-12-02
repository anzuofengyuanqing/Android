package cn.edu.nuc.seeworld.fg;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.tencent.tencentmap.streetviewsdk.Marker;
import com.tencent.tencentmap.streetviewsdk.StreetViewPanorama;
import com.tencent.tencentmap.streetviewsdk.StreetViewPanoramaView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.nuc.seeworld.Config;
import cn.edu.nuc.seeworld.R;
import cn.edu.nuc.seeworld.entity.MyMessage;
import cn.edu.nuc.seeworld.view.MyStree;

/**
 * Created by lenovo on 2015/9/10.
 */
public class fg_Stree extends Fragment {
    private MyStree mPanoramaView;
    StreetViewPanorama mPanorama;
    ImageView imageButton;
    Context context;
    List<MyMessage> Contentlist;
    String newID=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.stree,container,false);
        mPanoramaView = (MyStree) view.findViewById(R.id.panorama_view);
        mPanorama=mPanoramaView.getStreetViewPanorama();
        mPanorama.setZoomGesturesEnabled(false);
        context=this.getActivity();
        Contentlist=new ArrayList<>();
        final Marker marker=new Marker(38.015891560126306,112.45087473660825, BitmapFactory.decodeResource(getResources(),R.drawable.giftbox)){
            @Override
            public void onClick(float v, float v1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.get_image, null);
                builder.setView(view);
                Button button= (Button) view.findViewById(R.id.btn_queren);
                final AlertDialog dialog = builder.create();
                dialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                super.onClick(v, v1);
            }
        };

        mPanorama.addMarker(marker);
        imageButton= (ImageView) view.findViewById(R.id.iv_pen);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.additem, null);
                ImageButton ib = (ImageButton) view.findViewById(R.id.tv_ok);
                final EditText et = (EditText) view.findViewById(R.id.et_content);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et.getText().toString() != null && et.getText().toString() != "") {
                            mPanoramaView.publishnow(et.getText().toString());
                            saveMessage(et.getText().toString());
                        }
                        dialog.cancel();
                    }
                });
            }
        });
        mPanorama.setPosition(Config.clickLatlng.getLatitude(),Config.clickLatlng.getLongitude());
        mPanorama.setOnStreetViewPanoramaFinishListener(new StreetViewPanorama.OnStreetViewPanoramaFinishListner() {
            @Override
            public void OnStreetViewPanoramaFinish(boolean b) {
                BmobQuery<MyMessage> query = new BmobQuery<MyMessage>();
//                if(newID==null){
//                    newID=mPanorama.getCurrentStreetViewInfo().panoId;
//                }
                if (mPanorama.getCurrentStreetViewInfo() == null) {
//                    fg_error fe=new fg_error();
//                    FragmentManager fm=getFragmentManager();
//                    FragmentTransaction ft=fm.beginTransaction();
//                    ft.add(fe,"FG_ERROR");
//                    ft.commit();
                    Log.d("STREE","第一次为空");
                    return;
                }
                query.addWhereEqualTo("StreeID", mPanorama.getCurrentStreetViewInfo().panoId);
                Log.d("STREEFORME", mPanorama.getCurrentStreetViewInfo().latitude+""+"++++"+mPanorama.getCurrentStreetViewInfo().longitude);
                query.setLimit(50);
                query.findObjects(context, new FindListener<MyMessage>() {
                    @Override
                    public void onSuccess(List<MyMessage> list) {
                        Contentlist = list;
                        for (MyMessage myMessage : Contentlist) {
                            Log.d("STREEFORME", myMessage.getContenttext());
                        }
                        mPanoramaView.putcontent(Contentlist);

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
                imageButton.setVisibility(View.VISIBLE);

            }
        });
//        mPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
//            @Override
//            public void onStreetViewPanoramaChange(String s) {
//                if(mPanorama.getCurrentStreetViewInfo()!=null){
//                    newID=mPanorama.getCurrentStreetViewInfo().panoId;
//                }
//            }
//        });
        return view;
    }
    @Override
    public void onResume() {
        mPanoramaView.onResume();
        super.onResume();
        System.out.println("调用街景的onResume方法");
    }

    @Override
    public void onPause() {
        mPanoramaView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mPanoramaView.onDestory();
        super.onDestroyView();
    }

    public StreetViewPanoramaView getStreetView() {
        return mPanoramaView;
    }
    private void saveMessage(String content){
        MyMessage myMessage=new MyMessage();
        myMessage.setUser(Config.CurrUser);
        myMessage.setStreeID(mPanorama.getCurrentStreetViewInfo().panoId);
        myMessage.setContenttext(content);
        myMessage.save(this.getActivity(), new SaveListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String arg0) {

            }
        });
    }

}
