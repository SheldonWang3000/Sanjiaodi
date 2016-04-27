package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Date;

import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.ListItem.ItemData;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

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
        SJDLog.i("ContentActivity", "onClick");
        LayoutInflater inflater = ContentActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.sign_dialog, null);
        ((TextView)view.findViewById(R.id.dialog_title)).setText("活动名称：" + item.title);
        ((TextView)view.findViewById(R.id.dialog_time)).setText("活动时间：" +
                getDate(item.startTime) + "至" + getDate(item.endTime));
        final EditText phoneEdit = (EditText) view.findViewById(R.id.sign_phone);
        SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
        String phone = s.getString("mobile", null);
        phoneEdit.setText(phone);
        phoneEdit.setSelection(phone.length());
        new AlertDialog.Builder(ContentActivity.this)
                .setView(view)
                .setPositiveButton("报名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uid = null;
                        try {
                            uid = Info.getUid(ContentActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (TextUtils.isEmpty(uid)) {
                            //TODO 登录异常
                        }
                        MyVolley.sign(ContentActivity.this, uid, item.id, phoneEdit.getText().toString(),
                                new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        int result = Integer.valueOf((String)response);
                                        String str;
                                        switch (result) {
                                            case 0:
                                                str = "报名已截止";
                                                break;
                                            case 1:
                                                str = "报名失败";
                                                break;
                                            case 2:
                                                str = "报名成功";
                                                break;
                                            case 3:
                                                str = "您已报名";
                                                break;
                                            default:
                                                str = "错误";
                                                break;
                                        }
                                        Toast.makeText(ContentActivity.this, str, Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        SJDLog.d("sign_error", error);
                                    }
                                });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
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
