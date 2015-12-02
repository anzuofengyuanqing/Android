package cn.edu.nuc.seeworld;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.LatLng;

import cn.edu.nuc.seeworld.fg.fg_Main;
import cn.edu.nuc.seeworld.fg.fg_Map;
import cn.edu.nuc.seeworld.fg.fg_Menu;
import cn.edu.nuc.seeworld.fg.fg_Setting;
import cn.edu.nuc.seeworld.fg.fg_Stree;
import cn.edu.nuc.seeworld.view.SlidingMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TencentLocationListener{
    private SlidingMenu mSlidingMenu;
    private fg_Menu fg_menu;
    private RelativeLayout mMain;
    private RelativeLayout mSetting;
    private RelativeLayout mMap;
    private RelativeLayout mStree;
    private RelativeLayout mAR;
    private fg_Main fgmain;
    private fg_Map fgmap;
    private fg_Setting fgsetting;
    private fg_Stree fgstree;
    private Fragment curfg;
    private Toolbar toolbar;
    private TencentLocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();
        setListener();
        settoolbar();
        TencentLocationRequest request = TencentLocationRequest.create();
        locationManager= TencentLocationManager.getInstance(this);
        int error = locationManager.requestLocationUpdates(request, this);
    }

    private void settoolbar() {
        toolbar= (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("See world");
        toolbar.setNavigationContentDescription(R.id.id_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_course);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingMenu.toggle();
            }
        });


    }

    private void setListener() {
        mMain.setOnClickListener(this);
        mMap.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mStree.setOnClickListener(this);
        mAR.setOnClickListener(this);

    }
    private void findID() {
        mSlidingMenu= (SlidingMenu) findViewById(R.id.id_menu);
        mMain= (RelativeLayout) findViewById(R.id.item_main);
        mMap= (RelativeLayout) findViewById(R.id.item_map);
        mSetting= (RelativeLayout) findViewById(R.id.item_setting);
        mStree= (RelativeLayout) findViewById(R.id.item_stree);
        mAR= (RelativeLayout) findViewById(R.id.item_ar);
    }
//    public void toggleMenu(View view){
//       mSlidingMenu.toggle();
//   }

    @Override
    protected void onStart() {
        super.onStart();
        defulContent();
        Config.isStreeFg=false;
    }

    public  void defulContent(){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fgmain==null){
            fgmain=new fg_Main();
        }
        ft.replace(R.id.id_content,fgmain);
        ft.commit();
        curfg=fgmain;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_main:
//                Toast.makeText(this,"MAIN",Toast.LENGTH_LONG).show();
                if(curfg!=fgmain){
                    fgmain=new fg_Main();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction fragmentTransaction=fm.beginTransaction();
                    fragmentTransaction.replace(R.id.id_content,fgmain);
                    fragmentTransaction.commit();
                    curfg=fgmain;
                    Config.isStreeFg=false;
                    mSlidingMenu.toggle();
                }
                break;
            case R.id.item_map:
//                Toast.makeText(this,"MAP",Toast.LENGTH_LONG).show();
                if(curfg!=fgmap){
                    fgmap=new fg_Map();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction fragmentTransaction=fm.beginTransaction();
                    fragmentTransaction.replace(R.id.id_content,fgmap);
                    fragmentTransaction.commit();
                    curfg=fgmap;
                    Config.isStreeFg=false;
                    mSlidingMenu.toggle();
                }
                break;
            case R.id.item_setting:
//                Toast.makeText(this,"SETTING",Toast.LENGTH_LONG).show();
                if(curfg!=fgsetting){
                    fgsetting=new fg_Setting();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction fragmentTransaction=fm.beginTransaction();
                    fragmentTransaction.replace(R.id.id_content,fgsetting);
                    fragmentTransaction.commit();
                    curfg=fgsetting;
                    Config.isStreeFg=false;
                    mSlidingMenu.toggle();
                }
                break;
            case R.id.item_stree:
//                Toast.makeText(this,"SETTING",Toast.LENGTH_LONG).show();
                if(curfg!=fgstree){
                    fgstree=new fg_Stree();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction fragmentTransaction=fm.beginTransaction();
                    fragmentTransaction.replace(R.id.id_content,fgstree);
                    fragmentTransaction.commit();
                    curfg=fgstree;
                    Config.isStreeFg=true;
                    mSlidingMenu.toggle();
                }
                break;
            case R.id.item_ar:
                Intent i=new Intent(this,cn.edu.nuc.seeworld.ar.SampleApplication.ImageTargets.ImageTargets.class);
                startActivity(i);
                mSlidingMenu.toggle();
        }
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (TencentLocation.ERROR_OK == i&&Config.isclickmap==false) {
//            Toast.makeText(this,"定位成功",Toast.LENGTH_SHORT).show();
            Config.clickLatlng=new LatLng(tencentLocation.getLatitude(),tencentLocation.getLongitude());
            Config.mylatlng=new LatLng(tencentLocation.getLatitude(),tencentLocation.getLongitude());
        } else {
            System.out.println("失败");
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }
}
