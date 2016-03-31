package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sheldon.sanjiaodi.R;

public class InfoSampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int sample = getIntent().getIntExtra("sample", -1);
        if (sample != -1) {
            setContentView(sample);
            String title = getIntent().getStringExtra("title");
            ((TextView)findViewById(R.id.header_text)).setText(title);
            final int imageId = getIntent().getIntExtra("resource", -1);
            if (imageId != -1) {
                ImageView imageView = (ImageView) findViewById(R.id.sample_image);
                ((TextView) findViewById(R.id.info_image_title)).setText(getIntent().getStringExtra("subtitle"));
                imageView.setImageResource(imageId);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.putExtra("resource", imageId);
                        i.setClass(InfoSampleActivity.this, ImageActivity.class);
                        startActivity(i);
                    }
                });
            }
            findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

}
