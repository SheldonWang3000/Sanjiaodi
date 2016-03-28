package sheldon.sanjiaodi.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import sheldon.sanjiaodi.GlideImageLoader;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class UploadActivity extends ActionBarActivity {

    private Bitmap bitmap;
    String photoPath;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


//        imageView.setImageBitmap(bitmap);

        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(ContextCompat.getColor(this, R.color.pku_red))
                .setIconCamera(R.mipmap.camera)
                .build();
        //配置功能
        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnableCamera(true)
                .setEnablePreview(true).build();

//        final FunctionConfig functionConfig = new FunctionConfig.Builder()
//                .setEnableCamera(true).build();

        //配置imageloader
        ImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);

        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,
                        functionConfig, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                SJDLog.i("success_size", resultList.size());
                                SJDLog.i("success_0_path", resultList.get(0).getPhotoPath());
                                photoPath = resultList.get(0).getPhotoPath();
                                MyVolley.getInstance(UploadActivity.this).uploadImage(
                                        "http://192.168.1.100/upload.php",
                                        photoPath,
                                        new Response.Listener() {
                                            @Override
                                            public void onResponse(Object response) {

                                                Toast.makeText(UploadActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Toast.makeText(UploadActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            @Override
                            public void onHanlderFailure(int requestCode, String errorMsg) {

                            }
                        });
            }
        });

    }
}
