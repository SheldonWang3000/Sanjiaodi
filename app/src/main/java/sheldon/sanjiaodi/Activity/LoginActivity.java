package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText userEditText;
    private EditText passwordEditText;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Info.removeUid();
        userEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_login_weixin).setOnClickListener(this);
        findViewById(R.id.btn_login_guest).setOnClickListener(this);

        mProgressView = findViewById(R.id.process);
    }

    private void attemptLogin() {

        userEditText.setError(null);
        passwordEditText.setError(null);

        String username = userEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(password))  {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            passwordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            userEditText.setError(getString(R.string.error_field_required));
            userEditText.requestFocus();
            return;
        }
        mProgressView.setVisibility(View.VISIBLE);
        MyVolley.login(this, username, password, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    SJDLog.i("login", response);
                    if (response.getInt("sign") == 1) {
                        JSONObject tmpObject = response.getJSONObject("data");
                        mProgressView.setVisibility(View.GONE);
                        SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = s.edit();
                        editor.putString("uid", tmpObject.getString("uid"));
                        editor.putString("realname", tmpObject.getString("realname"));
                        editor.putString("mobile", tmpObject.getString("mobile"));
                        editor.putString("username", tmpObject.getString("username"));
                        editor.putString("email", tmpObject.getString("email"));
                        editor.putString("department", tmpObject.getString("department"));
                        editor.putString("major", tmpObject.getString("major"));
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        mProgressView.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("登录错误")
                                .setMessage("登录失败")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                SJDLog.i("loginClick", "btn_login_clicked");
                View view = LoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                attemptLogin();
                break;
            case  R.id.btn_login_weixin:
                break;
            case R.id.btn_login_guest:
                SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                SJDLog.i("btn_login_guest", s.getString("uid", "-2"));
                SharedPreferences.Editor editor = s.edit();
                //-1代表游客登录
                editor.putString("uid", "-1");
                editor.commit();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}

