/**
 * Copyright 2015 Bartosz Lipinski
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.edu.nuc.seeworld.fg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.nuc.seeworld.Config;
import cn.edu.nuc.seeworld.R;
import cn.edu.nuc.seeworld.entity.MyVideoandImage;
import cn.edu.nuc.seeworld.ui.record.MediaRecorderActivity;


/**
 * Created by Bartosz Lipinski
 * 28.01.15
 */
public class ColorFragment extends Fragment {
    private static final String TAG = "cn.edu.nuc.seeworld.fg.ColorFragment";
    private static final String EXTRA_COLOR = "com.bartoszlipinski.flippablestackview.fragment.ColorFragment.EXTRA_COLOR";
    private static final int CAMERA = 1;
    private static final int PHOTO = 2;
    FrameLayout mMainLayout;
    ImageView imageView;
    ImageView pic_imageView;
    Button[] camera_log_buttons = new Button[3];
    AlertDialog dialog;
    private Button btn_publish;
    private EditText et_site;
    private EditText et_text;
    private String pic_path;
    private String video_path;
    private Context mContext;

    public static ColorFragment newInstance(int backgroundColor) {
        ColorFragment fragment = new ColorFragment();
        Bundle bdl = new Bundle();
        bdl.putInt(EXTRA_COLOR, backgroundColor);
        fragment.setArguments(bdl);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dummy, container, false);
        Bundle bdl = getArguments();
        mContext = this.getActivity();
        mMainLayout = (FrameLayout) v.findViewById(R.id.main_layout);

        LayerDrawable bgDrawable = (LayerDrawable) mMainLayout.getBackground();
        GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.background_shape);
        shape.setColor(bdl.getInt(EXTRA_COLOR));
        imageView = (ImageView) v.findViewById(R.id.iv_camera);
        pic_imageView = (ImageView) v.findViewById(R.id.iv_pic);
        et_site = (EditText) v.findViewById(R.id.et_site);
        et_text = (EditText) v.findViewById(R.id.et_writeword);
        btn_publish = (Button) v.findViewById(R.id.btn_publish);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View v1 = layoutInflater.inflate(R.layout.camera_dialog, null);
                builder.setView(v1);
                camera_log_buttons[0] = (Button) v1.findViewById(R.id.bt_camera_dialog_1);
                camera_log_buttons[1] = (Button) v1.findViewById(R.id.bt_camera_dialog_2);
                camera_log_buttons[2] = (Button) v1.findViewById(R.id.bt_camera_dialog_3);
                for (int i = 0; i < 3; i++) {
                    camera_log_buttons[i].setOnClickListener(new cameraonclicklistener());

                }
                dialog = builder.create();
                dialog.show();
            }
        });
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
        return v;
    }

    private void publish() {
        String site = et_site.getText().toString();
        video_path= Config.video_path;
        if (null == site||"".equals(site)) {
            Toast.makeText(mContext, "地点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final MyVideoandImage myVideoandImage = new MyVideoandImage();
        String text = et_text.getText().toString();
        if (text != null) {
            myVideoandImage.setText(text);
        }
        myVideoandImage.setProvince(site);
        ArrayList<String> files = new ArrayList<String>();
        final int flag[] = new int[]{0, 0};
        if (pic_path != null) {

//            File image_file=new File(pic_path);
//            BmobFile image_bomb_file=new BmobFile(image_file);
//            myVideoandImage.setImage(image_bomb_file);
            files.add(0,pic_path);
            flag[0] = 1;
        }
        if (video_path != null) {
//            File video_file=new File(video_path);
//            myVideoandImage.setVideo(new BmobFile(video_file));
            files.add(video_path);
            flag[1] = 1;
        }
        BmobProFile.getInstance(mContext).uploadBatch((String[]) files.toArray(new String[files.size()]), new UploadBatchListener() {
            @Override
            public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {
                if (flag[0] == 1) myVideoandImage.setImage_url(strings[0]);
                if (flag[1] == 1&&flag[0] == 1) myVideoandImage.setVideo_url(strings[1]);
                if(flag[0]==0&&flag[1]==1)myVideoandImage.setVideo_url(strings[0]);
                myVideoandImage.save(mContext, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                        clearcons();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, i + s);
                    }
                });
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearcons() {
        pic_imageView.setImageBitmap(null);
        imageView.setImageBitmap(null);
        et_site.setText("");
        et_text.setText("");
        pic_path = null;
        video_path = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK && null != data) {
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            File file = new File("/sdcard/pintu/");
            file.mkdirs();
            String filename = file.getPath() + name;
            try {
                fout = new FileOutputStream(filename);
                pic_path = filename;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pic_imageView.setImageBitmap(bitmap);
            pic_imageView.setVisibility(View.VISIBLE);
        }
        if (requestCode == PHOTO && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            //获取图片并显示
            Log.d("SEEWORLDTAG", picturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            pic_path = picturePath;
            pic_imageView.setImageBitmap(bitmap);
            pic_imageView.setVisibility(View.VISIBLE);
        }
    }

    class cameraonclicklistener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_camera_dialog_1:
                    getpicfromalbum();
                    break;
                case R.id.bt_camera_dialog_2:
                    getpicfromcamera();
                    break;
                case R.id.bt_camera_dialog_3:
                    getvideo();
                    break;
            }
            dialog.cancel();
        }
    }

    private void getpicfromalbum() {
        Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, PHOTO);
    }

    private void getpicfromcamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA);
    }

    private void getvideo() {
        Intent intent = new Intent(this.getActivity(), MediaRecorderActivity.class);
        startActivity(intent);
    }
}
