package sheldon.sanjiaodi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
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


    public static void getImage(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.index)
                .error(R.mipmap.image_failed)
                .into(imageView);
    }
    public static void getImage(Context context, ImageView imageView, String url,
                                   ImageLoader.ImageListener listener) {
        if (listener == null) {
            listener = ImageLoader.getImageListener(imageView, R.mipmap.index, R.mipmap.image_failed);
        }
        MyVolley.getInstance(context).mImageLoader.get(baseUrl + url, listener);
    }

    public static void getImage(Context context, String url,
                                SimpleTarget target) {
        Glide.with(context.getApplicationContext())
                .load(url)
                .asBitmap()
                .error(R.mipmap.image_failed)
                .placeholder(R.mipmap.index)
                .into(target);
    }

    public void getJson(String url, Response.Listener<JSONObject> callback, Response.ErrorListener errorListener) {
        addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        baseUrl + url,
                        null,
                        callback,
                        errorListener));
    }

    public void getString(String url, Response.Listener<String> callback, Response.ErrorListener errorListener) {
        addToRequestQueue(
                new StringRequest(
                        Request.Method.GET,
                        baseUrl + url,
                        callback,
                        errorListener));
    }

    public void getArray(String url, Response.Listener<JSONArray> callback, Response.ErrorListener errorListener) {
        addToRequestQueue(
                new JsonArrayRequest(
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
        MyVolley.getInstance(context).getJson(url, callback, errorListener);

    }

    public static void getContent(Context context, String uid, Response.Listener callback,
                                  Response.ErrorListener errorListener) {
        String url = "http://sanjiaodi.cn/sjd_phone/index.php?s=/event/index/index_api_new/uid/"
                + uid + ".html";
        SJDLog.i("MyVolley", url);
        MyVolley.getInstance(context).getArray(url, callback, errorListener);
    }

    public static void getCollection(Context context, String uid, Response.Listener callback,
                                  Response.ErrorListener errorListener) {
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/event/index/myevent_api/lora/collect/uid/"
                + uid + ".html";
        SJDLog.i("MyVolley", url);
        MyVolley.getInstance(context).getArray(url, callback, errorListener);
    }

    public static void getAttend(Context context, String uid, Response.Listener callback,
                                     Response.ErrorListener errorListener) {
        //TODO 应该正常，待测试
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/event/index/myevent_api/lora/attend/uid/"
                + uid + ".html";
        SJDLog.i("MyVolley", url);
        MyVolley.getInstance(context).getArray(url, callback, errorListener);
    }

    public static void collect(Context context, String uid, String id,
                               Response.Listener callback,
                               Response.ErrorListener errorListener) {
        String url = "http://sanjiaodi.cn/sjd_phone/index.php?s=/event/index/doCollect_api/event_id/"
               + id + "/uid/" + uid + ".html";
        SJDLog.i("Volley_collect", url);
        MyVolley.getInstance(context).getString(url, callback, errorListener);
    }

    public static void unCollect(Context context, String uid, String id,
                               Response.Listener callback,
                               Response.ErrorListener errorListener) {
        String url = "http://sanjiaodi.cn/sjd_phone/index.php?s=/event/index/unCollect_api/event_id/"
                + id + "/uid/" + uid + ".html";
        SJDLog.i("Volley_collect", url);
        MyVolley.getInstance(context).getString(url, callback, errorListener);
    }
    public static void getTag(Context context, Response.Listener callback,
                              Response.ErrorListener errorListener) {
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/event/index/hottag/num/8.html";
        SJDLog.i("getTagVolley", url);
        MyVolley.getInstance(context).getArray(url, callback, errorListener);
    }

    public static void getHotActivity(Context context, String uid, Response.Listener callback,
                                      Response.ErrorListener errorListener) {
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/event/index/gethotactivity/uid/"
                + uid + ".html";
        SJDLog.i("getHotActivityVolley", url);
        MyVolley.getInstance(context).getArray(url, callback, errorListener);
    }

    public static void changePassword(Context context, String uid, String oldPassword, String newPassword,
                                      Response.Listener callback, Response.ErrorListener errorListener) {
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/ucenter/verify/doChangePassword_api/uid/"
                + uid + "/old_password/" + oldPassword + "/new_password/" + newPassword + ".html";
        SJDLog.i("changePasswordVolley", url);
        MyVolley.getInstance(context).getString(url, callback, errorListener);
    }

    public static void getActivityByTag(Context context, String id, String uid,
                                        Response.Listener callback,
                                        Response.ErrorListener errorListener) {
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/event/index/getByTag/tag_id/"
                + id + "/uid/" + uid + ".html";
        SJDLog.i("getActivityByTag", url);
        MyVolley.getInstance(context).getArray(url, callback, errorListener);
    }

    public static void getVerifyCode(Context context, String uid, String phone,
                                     Response.Listener callback,
                                     Response.ErrorListener errorListener) {
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/ucenter/verify/sendVerify_api/config/config/account/"
               + phone + "/uid/" + uid + ".html";
        SJDLog.i("getVerifyCodeVolley", url);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                baseUrl + url,
                callback,
                errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyVolley.getInstance(context).addToRequestQueue(request);
    }

    public static void getConfirm(Context context, String uid, String phone, String code,
                                  Response.Listener callback, Response.ErrorListener errorListener) {
        String url = "http://www.sanjiaodi.cn/sjd_phone/index.php?s=/ucenter/verify/checkVerify_api/uid/"
                + uid + "/mobile/" + phone + "/verify_num/" + code + ".html";
        SJDLog.i("getComfirmVolley", url);
        MyVolley.getInstance(context).getString(url, callback, errorListener);
    }

    public static void changeInfo(Context context, int id, String uid, String content,
                                  Response.Listener callback, Response.ErrorListener errorListener) {
        //id 2邮箱，3学院，4专业
        String url = "http://sanjiaodi.cn/sjd_phone/index.php?s=/ucenter/member/saveinfo_api/id/"
                + id + "/uid/" + uid + "/content/" + content + ".html";
        SJDLog.i("changeInfoVolley", url);
        MyVolley.getInstance(context).getJson(url, callback, errorListener);
    }

    public void uploadImage(String url, String location,
                            Response.Listener callback,
                            Response.ErrorListener errorListener) {
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(location, options);
        Image image = new Image(bitmap, location);
        Request request = new UploadRequest(url, image, callback, errorListener) ;
        addToRequestQueue(request) ;
    }


}
