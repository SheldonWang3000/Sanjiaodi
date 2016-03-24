package sheldon.sanjiaodi.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import sheldon.sanjiaodi.R;

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
    }

    private void alertPassword() {

    }
}
