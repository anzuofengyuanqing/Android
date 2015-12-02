package cn.edu.nuc.seeworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.hanvon.HWCloudManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.nuc.seeworld.adapter.FriendsAdapter;
import cn.edu.nuc.seeworld.entity.Myfriends;
import cn.edu.nuc.seeworld.utils.BitmapUtils;
import cn.edu.nuc.seeworld.utils.StringUtils;

/**
 * Created by lenovo on 2015/10/11.
 */
public class friendsActivity extends AppCompatActivity {
    private static final String TAG = "cn.edu.nuc.seeworld.friendsActivity";
    private static final int CAMERA = 1;
    ListView listView;
    List<Myfriends> myfriendses;
    SearchView searchView;
    Toolbar toolbar;
    private HWCloudManager hwCloudManager;
    private Bitmap disposedbitmap;
    AsyncTask<Void, Void, String> cameratask;
    BaseAdapter adapter;
    public Handler mHandler;
    BaseAdapter adapterple;
    File tempFile;
    List<Myfriends> rplemyfriendses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_friends);
        hwCloudManager = new HWCloudManager(this, "a9e178bf-aa0d-486d-a305-e7e23755ab54");
        initData();
        initView();
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0x124){
                    Log.d(TAG, "更新ListView");
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void initData() {
        myfriendses = new ArrayList<>();
        rplemyfriendses=   new ArrayList<>();
        BmobQuery<Myfriends> query = new BmobQuery<Myfriends>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(this));
        query.setLimit(50);
        query.findObjects(this, new FindListener<Myfriends>() {
            @Override
            public void onSuccess(List<Myfriends> list) {

                for (Myfriends myfriend : list) {
                    myfriendses.add(myfriend);
                }
                mHandler.sendEmptyMessage(0x124);
                Toast.makeText(friendsActivity.this, "获取列表成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(friendsActivity.this, "获取列表失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, i + s);
            }
        });

    }

    private void initView() {
        adapter = new FriendsAdapter(myfriendses, this);
        listView = (ListView) findViewById(R.id.lv_friends);
        listView.setAdapter(adapter);
        searchView = (SearchView) findViewById(R.id.sv_site);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("搜索");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG,"开始调用回调");
                if(TextUtils.isEmpty(newText)){
                    listView.setAdapter(adapter);
                }else {
                    Log.d(TAG,"开始调用");
                    rplemyfriendses.clear();
                    for(int i=0;i<myfriendses.size();i++){
                        if(myfriendses.get(i).getProvince()!=null&&myfriendses.get(i).getProvince().indexOf(newText)!=-1){
                            rplemyfriendses.add(myfriendses.get(i));
                        }
                    }
                    adapterple=new FriendsAdapter(rplemyfriendses,friendsActivity.this);
                    listView.setAdapter(adapterple);
                }
                return true;
            }
        });
        settoolbar();
    }

    private void settoolbar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.id_friendsbar);
        toolbar.setTitle("我的朋友们");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_friend) {
                    String SDState = Environment.getExternalStorageState();
                    if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                        tempFile = new File("/sdcard/pintu/" + name);
                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(camera, CAMERA);

                    }
                }
                return true;
            }
        });
    }
    private void myActivityResult(){
        Log.d(TAG,"开始上传");
        disposedbitmap=BitmapFactory.decodeFile(tempFile.getAbsolutePath());
        disposedbitmap = BitmapUtils.resizePhoto(disposedbitmap);
        cameratask = new cameraTask();
        cameratask.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA){
            myActivityResult();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addfriends, menu);
        return true;
    }

    private class cameraTask extends AsyncTask<Void, Void, String> {
        private String doSomeNeeded(Bitmap bitmap) {
            String result = "1";
            Bitmap bitmaptest = BitmapFactory.decodeResource(getResources(), R.drawable.test04);
            Log.d(TAG, "开始提交");
            result = hwCloudManager.cardLanguage4Https("chns", bitmap);
            Log.d(TAG, result);
            return result;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = doSomeNeeded(disposedbitmap);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            doresolutionJson(s);
        }
    }

    public void doresolutionJson(String s) {

            final Myfriends myfriends = new Myfriends();
            try {
                JSONObject friends = new JSONObject(s);
                myfriends.setName((String) friends.getJSONArray("name").get(0));
                if (myfriends.getName() == "") {
                    return;
                }
                String s1="";
                for (int i=0;i<friends.getJSONArray("mobile").length();i++){
                    if(friends.getJSONArray("mobile").get(i).toString().length()>=11){
                        s1=friends.getJSONArray("mobile").get(i).toString();
                        break;
                    }
                }
                myfriends.setMobile(s1);
                String s2="";
                String s3="";
                s2= (String) friends.getJSONArray("addr").get(0);
                List<String> list=StringUtils.allprovince();
                for(int i=0;i< list.size();i++){
                    if(s2.indexOf(list.get(i))!=-1){
                        s3=list.get(i);
                    }
                }
                myfriends.setProvince(s3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myfriends.setUser(BmobUser.getCurrentUser(this));
        myfriends.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                myfriendses.add(myfriends);
                for(int i=0;i<myfriendses.size();i++){
                    Log.d(TAG,myfriendses.get(i).getName()+i);
                }
                mHandler.sendEmptyMessage(0x124);
                Toast.makeText(friendsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(friendsActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
