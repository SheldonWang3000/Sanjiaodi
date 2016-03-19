package sheldon.sanjiaodi.InfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import sheldon.sanjiaodi.ImageActivity;
import sheldon.sanjiaodi.R;

public class InfoSampleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pku_red));
        }

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
