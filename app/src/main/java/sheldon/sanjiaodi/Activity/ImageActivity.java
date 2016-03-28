package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Sheldon on 2016/3/6.
 */
public class ImageActivity extends Activity{
    ImageView imageView;
    PhotoViewAttacher viewAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView = (ImageView) findViewById(R.id.activity_photo);

        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            int resource = getIntent().getIntExtra("resource", R.mipmap.image_failed);
            Drawable bitmap;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                bitmap = getResources().getDrawable(resource, getTheme());
            }else {
                bitmap = ContextCompat.getDrawable(this, resource);
            }
            imageView.setImageDrawable(bitmap);
            viewAttacher = new PhotoViewAttacher(imageView);
        }else {
            MyVolley.getImage(this, imageView, url, null);
            viewAttacher = new PhotoViewAttacher(imageView);
            viewAttacher.update();
        }

        ImageView closeButton = (ImageView) findViewById(R.id.button_close_image);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
