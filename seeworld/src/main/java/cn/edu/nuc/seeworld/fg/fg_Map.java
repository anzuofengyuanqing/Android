package cn.edu.nuc.seeworld.fg;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nuc.seeworld.Config;
import cn.edu.nuc.seeworld.R;

/**
 * Created by lenovo on 2015/9/9.
 */
public class fg_Map extends Fragment {
    public MapView mapview=null;
    public TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.map,container,false);
        mapview= (MapView) view.findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState);
        textView= (TextView) view.findViewById(R.id.tv_local);
        final List<MarkerOptions> list = new ArrayList<MarkerOptions>();
        final TencentMap tenmap=mapview.getMap();
        tenmap.setCenter(Config.clickLatlng);
        tenmap.clearAllOverlays();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Config.mylatlng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        tenmap.addMarker(markerOptions);
        tenmap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                tenmap.clearAllOverlays();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
                tenmap.addMarker(markerOptions);
                Config.clickLatlng = latLng;
                Config.isclickmap = true;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenmap.setCenter(Config.mylatlng);
                tenmap.clearAllOverlays();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(Config.mylatlng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
                tenmap.addMarker(markerOptions);
                Config.clickLatlng=Config.mylatlng;
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        mapview.onPause();
        super.onPause();
    }
    @Override
    public void onResume() {
        mapview.onResume();
        super.onResume();
    }
    @Override
    public void onDestroyView() {
        mapview.onDestroy();
        super.onDestroyView();

    }
    @Override
    public void onStop() {
        mapview.onStop();
        super.onStop();
    }
}
