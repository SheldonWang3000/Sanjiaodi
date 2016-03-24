package sheldon.sanjiaodi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sheldon.sanjiaodi.R;

public class MyselfAlertActivity extends ActionBarActivity {

    private final static int ALERT_MAIL = 0;
    private final int ALERT_COLLEGE = 1;
    private final int ALERT_SUBJECT = 2;

    private int type;

    private EditText contentEdit;
    private Button btnAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_alert);
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        ((TextView) findViewById(R.id.header_text)).setText(title);

        switch (title) {
            case "邮箱":
                type = ALERT_MAIL;
                break;
            case "学院":
                type = ALERT_COLLEGE;
                break;
            case "专业":
                type = ALERT_SUBJECT;
                break;
            default:
                break;
        }

        contentEdit = (EditText) findViewById(R.id.alert_myself_info);
        String content = i.getStringExtra("content");
        contentEdit.setText(content);
        contentEdit.setSelection(content.length());

        (findViewById(R.id.back_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAlert = (Button) findViewById(R.id.btn_alert);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertContent();
            }
        });
    }

    private void alertContent() {
        Toast toast = Toast.makeText(MyselfAlertActivity.this, "alert", Toast.LENGTH_SHORT);
        toast.show();
        switch (type) {
            case ALERT_MAIL:
                break;
            case ALERT_COLLEGE:
                break;
            case ALERT_SUBJECT:
                break;
            default:
                break;
        }
    }
}
