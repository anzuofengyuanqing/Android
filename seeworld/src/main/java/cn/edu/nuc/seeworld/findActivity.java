package cn.edu.nuc.seeworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by lenovo on 2015/10/12.
 */
public class findActivity extends Activity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_find);
        initData();
        initView();
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.lv_finds);
    }

    private void initData() {

    }
}
