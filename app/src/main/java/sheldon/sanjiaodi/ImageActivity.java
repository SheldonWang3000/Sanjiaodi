package sheldon.sanjiaodi;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Sheldon on 2016/3/6.
 */
public class ImageActivity extends Activity{
    ImageView mImageView;
    PhotoViewAttacher mAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Any implementation of ImageView can be used!
        mImageView = (ImageView) findViewById(R.id.activity_photo);

        // Set the Drawable displayed
        Drawable bitmap = getResources().getDrawable(R.mipmap.flower);
        mImageView.setImageDrawable(bitmap);

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(mImageView);
//        mAttacher.update();
    }


}
