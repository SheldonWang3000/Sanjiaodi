package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    private String phoneNumber;
    private RelativeLayout process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        ((TextView) findViewById(R.id.header_text)).setText("手机身份验证");
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.btn_code).setOnClickListener(this);
        findViewById(R.id.btn_verify).setOnClickListener(this);

        process = (RelativeLayout) findViewById(R.id.loading);
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
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                String phone = phoneEdit.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "手机号码为空", Toast.LENGTH_SHORT).show();
                } else {
//                    process.setV
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
                    process.setVisibility(View.VISIBLE);
                    MyVolley.getVerifyCode(this, uid, phone,
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    process.setVisibility(View.GONE);
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
                                            phoneNumber = phoneEdit.getText().toString();
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
                if (phoneEdit.getText().toString().equals(phoneNumber)) {
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
                    process.setVisibility(View.VISIBLE);
                    MyVolley.getConfirm(this, uid, phoneEdit.getText().toString(),
                            verifyCodeEdit.getText().toString(),
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    process.setVisibility(View.GONE);
                                    SJDLog.i("getConfirmResponse", response);
                                    int status = Integer.valueOf(response.toString());
                                    String message = "";
                                    if (status == 0) {
                                        message = "手机验证失败";
                                    }
                                    if (status == 1) {
                                        message = "手机验证成功";
                                        SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                                        s.edit().putString("mobile", phoneNumber).commit();
                                    }
                                    if (!TextUtils.isEmpty(message)) {
                                        Toast.makeText(PhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    SJDLog.w("getConfirmError", error);
                                }
                            });
                } else {
                    Toast.makeText(PhoneActivity.this, "更改手机号码后请重新请求验证码", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
