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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private EditText userEditText;
    private EditText passwordEditText;
    private Button btn_login;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LoginActivity.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                attemptLogin();
            }
        });

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
                        mProgressView.setVisibility(View.GONE);
                        SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = s.edit();
                        editor.putString("token", "Y");
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

}

