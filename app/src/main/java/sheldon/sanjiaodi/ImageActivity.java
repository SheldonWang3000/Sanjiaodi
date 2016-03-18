package sheldon.sanjiaodi;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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
            Drawable bitmap = getResources().getDrawable(resource);
            imageView.setImageDrawable(bitmap);
            viewAttacher = new PhotoViewAttacher(imageView);
        }else {
            MyVolley.getInstance(this).getImage(imageView, url, null);
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
