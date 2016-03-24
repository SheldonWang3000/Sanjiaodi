package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;

public class ContentActivity extends Activity {

    private String url;
    private ImageView contentImage;
    private TextView contentText;
    private TextView timeText;
    private RelativeLayout signButton;
    private RelativeLayout process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ((TextView) findViewById(R.id.header_text)).setText("活动详情");
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        process = (RelativeLayout) findViewById(R.id.loading);
        contentImage = (ImageView) findViewById(R.id.content_image);
        timeText = (TextView) findViewById(R.id.content_time);
        contentText = (TextView) findViewById(R.id.content_html);
        signButton = (RelativeLayout) findViewById(R.id.btn_sign);

        contentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("url", url);
                i.setClass(ContentActivity.this, ImageActivity.class);
                startActivity(i);
            }
        });

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sign();
            }
        });

        initData();
    }

    private void sign() {

    }

    private void initData() {
        url = "http://i6.download.fd.pchome.net/g1/M00/12/1A/ooYBAFaxY9iIbIy-AAJvMjrySaMAAC3dwAfRpkAAm9K331.jpg";
        process.setVisibility(View.GONE);
//        signButton.setVisibility(View.GONE);
        contentText.setText(Html.fromHtml("abc<br>efg"));
        MyVolley.getInstance(this).getImage(contentImage,
                url, null);
    }
}
