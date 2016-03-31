package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class PhoneActivity extends Activity implements View.OnClickListener{


    private EditText phoneEdit;
    private EditText verifyCodeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        ((TextView) findViewById(R.id.header_text)).setText("手机身份验证");
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.btn_code).setOnClickListener(this);
        findViewById(R.id.btn_verify).setOnClickListener(this);

        phoneEdit = (EditText) findViewById(R.id.alert_phone_edit);
        verifyCodeEdit = (EditText) findViewById(R.id.alert_phone_code);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.btn_code:
                String phone = phoneEdit.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "手机号码为空", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PhoneActivity.this, "已请求验证码", Toast.LENGTH_SHORT).show();
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
                    MyVolley.getVerifyCode(this, uid, phone,
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    SJDLog.i("getVerifyCodeResponse", response);
                                    int status = Integer.valueOf(response.toString());
                                    String message = "";
                                    switch (status) {
                                        case 1:
                                            message = "请输入正确的手机号";
                                            break;
                                        case 2:
                                            message = "您的手机号已经验证过了";
                                            break;
                                        case 3:
                                            message = "验证码发送失败";
                                            break;
                                        case 4:
                                            message = "验证码发送成功，请耐心等待";
                                            break;
                                        default:
                                            break;
                                    }
                                    if (!TextUtils.isEmpty(message)) {
                                        Toast.makeText(PhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    SJDLog.i("getVerifyCodeError", error);
                                }
                            });
                }
                break;
            case R.id.btn_verify:
                break;
            default:
                break;
        }
    }
}
