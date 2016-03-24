package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import sheldon.sanjiaodi.R;

public class PhoneActivity extends Activity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        ((TextView) findViewById(R.id.header_text)).setText("手机身份验证");
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.btn_code).setOnClickListener(this);
        findViewById(R.id.btn_verify).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.btn_code:
                break;
            case R.id.btn_verify:
                break;
            default:
                break;
        }
    }
}
