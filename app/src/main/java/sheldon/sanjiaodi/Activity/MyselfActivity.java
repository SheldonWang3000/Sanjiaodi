package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import sheldon.sanjiaodi.R;

public class MyselfActivity extends Activity implements View.OnClickListener{

    private TextView userText;
    private TextView idText;
    private TextView phoneText;
    private TextView mailText;
    private TextView collegeText;
    private TextView subjectText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);

        ((TextView) findViewById(R.id.header_text)).setText("个人信息");
        findViewById(R.id.btn_myself_phone).setOnClickListener(this);
        findViewById(R.id.btn_myself_mail).setOnClickListener(this);
        findViewById(R.id.btn_myself_college).setOnClickListener(this);
        findViewById(R.id.btn_myself_subject).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);

        userText = (TextView) findViewById(R.id.myself_name_text);
        idText = (TextView) findViewById(R.id.myself_id_text);
        phoneText = (TextView) findViewById(R.id.myself_phone_text);
        mailText = (TextView) findViewById(R.id.myself_mail_text);
        collegeText = (TextView) findViewById(R.id.myself_college_text);
        subjectText = (TextView) findViewById(R.id.myself_subject_text);

        initData();
    }

    private void initData() {
        SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
        userText.setText(s.getString("realname", "三角地"));
        idText.setText("No_" + s.getString("username", "sjd"));
        phoneText.setText("Tel_" + s.getString("mobile", ""));
        mailText.setText(s.getString("email", ""));
        collegeText.setText(s.getString("department", ""));
        subjectText.setText(s.getString("major", ""));
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.btn_myself_phone:
                i.setClass(this, PhoneActivity.class);
                startActivity(i);
                break;
            case R.id.btn_myself_mail:
                i.putExtra("title", "邮箱");
                i.putExtra("content", mailText.getText().toString());
                i.setClass(this, MyselfAlertActivity.class);
                startActivity(i);
                break;
            case R.id.btn_myself_college:
                i.putExtra("title", "学院");
                i.putExtra("content", collegeText.getText().toString());
                i.setClass(this, MyselfAlertActivity.class);
                startActivity(i);
                break;
            case R.id.btn_myself_subject:
                i.putExtra("title", "专业");
                i.putExtra("content", subjectText.getText().toString());
                i.setClass(this, MyselfAlertActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
