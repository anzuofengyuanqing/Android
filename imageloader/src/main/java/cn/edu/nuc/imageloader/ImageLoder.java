package cn.edu.nuc.imageloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.edu.nuc.imageloader.disklrucache.DiskLruCache;

/**
 * Created by lenovo on 2016/1/5.
 */
public class ImageLoder {
    private static final String TAG = "ImageLoder";

    public static final int MESSAGE_POST_RESULT = 1;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;

    private static final int TAG_KEY_URL = 0x1234;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final int DISK_CACHE_INDEX = 0;
    private boolean mIsDiskLruCacheCreated = false;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoder#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor
            (CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<Runnable>(), sThreadFactory);

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            String uri = (String) imageView.getTag(TAG_KEY_URL);
            if (uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.w(TAG, "set image bitmap,but url has changed,ignored!");
            }
        }
    };

    private Context mContext;
    private ImageResizer mImageResizer = new ImageResizer();
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCaChe;

    private ImageLoder(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpae(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCaChe = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ImageLoder build(Context context) {
        return new ImageLoder(context);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    private void bindBitmap(final String uri, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URL, uri);
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = new Runnable() {

            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                if(bitmap!=null){
                    LoaderResult result=new LoaderResult(bitmap,imageView,uri);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result);
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromMemCache,url:" + uri);
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDisk,url:" + uri);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
            Log.d(TAG, "loadBitmapFromHttp,url:" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "encounter error,DiskLruCache is not created.");
            bitmap = downLoadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    private Bitmap downLoadBitmapFromUrl(String uri) {
        Bitmap bitmap=null;
        HttpURLConnection urlConnection=null;
        BufferedInputStream in=null;
        try {
            final  URL url=new URL(uri);
            urlConnection= (HttpURLConnection) url.openConnection();
            in=new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap= BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            Log.e(TAG,"Error in downloadBitmap: "+e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String uri, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI thread.");
        }
        if (mDiskLruCaChe == null) return null;
        String key = hashKeyFormUrl(uri);
        DiskLruCache.Editor editor = mDiskLruCaChe.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downLoadUrlToStream(uri, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCaChe.flush();
        }
        return loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
    }

    private boolean downLoadUrlToStream(String uri, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "downloadBitmap failed." + e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private Bitmap loadBitmapFromDiskCache(String uri, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d(TAG, "load bitmap from UI Thread.it's not recommended");
        }
        if (mDiskLruCaChe == null) return null;
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(uri);
        DiskLruCache.Snapshot snapshot = mDiskLruCaChe.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromMemCache(String uri) {
        final String key = hashKeyFormUrl(uri);
        Bitmap bitmap = getBitmapFromMemCache(key);
        return bitmap;
    }

    private String hashKeyFormUrl(String uri) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(uri.getBytes());
            cacheKey = byteToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(uri.hashCode());
        }
        return cacheKey;
    }

    private String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUsableSpae(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    private File getDiskCacheDir(Context context, String bitmap) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + bitmap);
    }

    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(Bitmap bitmap, ImageView imageView, String uri) {
            this.bitmap = bitmap;
            this.imageView = imageView;
            this.uri = uri;
        }
    }

}
