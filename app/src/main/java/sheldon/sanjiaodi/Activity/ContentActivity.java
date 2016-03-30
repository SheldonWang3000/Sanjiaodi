package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Date;

import sheldon.sanjiaodi.ListItem.ItemData;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;

public class ContentActivity extends Activity {

    private String url;
    private ImageView contentImage;
    private TextView contentText;
    private TextView timeText;
    private TextView deadlineText;
    private RelativeLayout signButton;
    private RelativeLayout process;
    private ItemData item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        item = (ItemData) getIntent().getExtras().getSerializable("item");
        ((TextView) findViewById(R.id.header_text)).setText("活动详情");
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
        deadlineText = (TextView) findViewById(R.id.deadline_time);
        signButton.setVisibility(View.GONE);
        deadlineText.setVisibility(View.GONE);
        process.setVisibility(View.VISIBLE);

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
        url = item.bigUrl;
        if (item.sign.equals("1")) {
            signButton.setVisibility(View.VISIBLE);
            deadlineText.setVisibility(View.VISIBLE);
        }else {
            deadlineText.setText("报名截止时间：" + getDate(item.deadline));
        }
        timeText.setText("活动时间：" + getDate(item.startTime) + "至" + getDate(item.endTime));
        contentText.setText(Html.fromHtml(item.explain));
        MyVolley.getImage(this, url, new SimpleTarget() {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                contentImage.setImageBitmap((Bitmap) resource);
                process.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                contentImage.setImageDrawable(errorDrawable);
                process.setVisibility(View.GONE);
            }

        });
    }

    private String getDate(String deadline) {
        Date date = new Date(Long.valueOf(deadline) * 1000);
        return DateFormat.format("yyyy-MM-dd", date).toString();
    }
}
