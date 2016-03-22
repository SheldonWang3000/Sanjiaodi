package sheldon.sanjiaodi;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sheldon on 2016/3/9.
 */
public class MyVolley {
    private static MyVolley mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private static String baseUrl = "";

    private MyVolley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    int maxSize = 10 * 1024 * 1024;
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(maxSize) {
                        @Override
                        protected int sizeOf(String key, Bitmap bitmap) {
                            return bitmap.getRowBytes() * bitmap.getHeight();
                        }
                    };

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized MyVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyVolley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    private ImageLoader getImageLoader() {
        return mImageLoader;
    }


    public void getImage(ImageView imageView, String url, ImageLoader.ImageListener listener) {
        if (listener == null) {
            listener = ImageLoader.getImageListener(imageView, R.mipmap.image_default, R.mipmap.image_failed);
        }
        mImageLoader.get(baseUrl + url, listener);
    }

    public void get(String url, Response.Listener<JSONObject> callback, Response.ErrorListener errorListener) {
        addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        baseUrl + url,
                        null,
                        callback,
                        errorListener));
    }

    public void post(String url, JSONObject paramsObject, Response.Listener<JSONObject> callback,
                     Response.ErrorListener errorListener) {
        addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        baseUrl + url,
                        paramsObject,
                        callback,
                        errorListener) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        return headers;
                    }
                });
    }

    public static void login(Context context, String username, String password,
                             Response.Listener<JSONObject> callback,
                             Response.ErrorListener errorListener) {
        String url = "http://sanjiaodi.cn/sjd_phone/index.php?s=/ucenter/member/login_api/name/"
                + username + "/password/" + password + ".html";
        SJDLog.i("login_url", url);
        MyVolley.getInstance(context).get(url, callback, errorListener);

    }
}
