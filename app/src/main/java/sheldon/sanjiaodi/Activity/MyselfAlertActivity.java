package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class MyselfAlertActivity extends Activity {

    private final static int ALERT_MAIL = 2;
    private final int ALERT_COLLEGE = 3;
    private final int ALERT_SUBJECT = 4;

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
                View view = MyselfAlertActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                alertContent();
            }
        });
    }

    private void alertContent() {
        String uid = null;
        try {
            uid = Info.getUid(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uid == null) {
            //TODO
            return;
        }
        MyVolley.changeInfo(this, type, uid, contentEdit.getText().toString(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONObject tmpObject = (JSONObject) response;
                        SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = s.edit();
                        switch (type) {
                            case ALERT_MAIL:
                                try {
                                    editor.putString("email", tmpObject.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case ALERT_COLLEGE:
                                try {
                                    editor.putString("department", tmpObject.getString("department"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case ALERT_SUBJECT:
                                try {
                                    editor.putString("major", tmpObject.getString("major"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                        editor.commit();
                        Toast.makeText(MyselfAlertActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SJDLog.w("changeInfoError", error);
                    }
                });
    }
}
