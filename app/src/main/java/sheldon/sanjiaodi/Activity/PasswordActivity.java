package sheldon.sanjiaodi.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class PasswordActivity extends ActionBarActivity {

    private EditText oldEditText;
    private EditText newEditText;
    private EditText newAgainEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        ((TextView) findViewById(R.id.header_text)).setText("修改密码");

        (findViewById(R.id.btn_password_alert)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPassword();
            }
        });

        oldEditText = (EditText) findViewById(R.id.old_password);
        newEditText = (EditText) findViewById(R.id.new_password);
        newAgainEditText = (EditText) findViewById(R.id.new_password_again);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_password_alert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPassword();
            }
        });
    }

    private void alertPassword() {
        if (newEditText.getText().toString().equals(newAgainEditText.getText().toString())) {
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
            MyVolley.changePassword(this, uid, oldEditText.getText().toString(),
                    newEditText.getText().toString(),
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            if (response.toString().equals("0")) {
                                Toast.makeText(PasswordActivity.this,
                                        "密码修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(PasswordActivity.this,
                                        "密码修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            SJDLog.w("changePasswordError", error);
                        }
                    });
        } else {
            Toast.makeText(this, "新密码和确认密码不一致", Toast.LENGTH_SHORT).show();
        }
    }
}
