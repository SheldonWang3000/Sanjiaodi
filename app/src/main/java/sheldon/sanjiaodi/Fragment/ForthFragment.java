package sheldon.sanjiaodi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sheldon.sanjiaodi.Activity.MyselfActivity;
import sheldon.sanjiaodi.Activity.ParticipateActivity;
import sheldon.sanjiaodi.Activity.PasswordActivity;
import sheldon.sanjiaodi.Activity.PhoneActivity;
import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.Activity.LoginActivity;
import sheldon.sanjiaodi.Activity.MainActivity;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class ForthFragment extends BaseFragment implements View.OnClickListener{

    private TextView userName;
    private RelativeLayout process;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_forth, null);
        ((TextView) view.findViewById(R.id.header_text)).setText("我的活动");
        process = (RelativeLayout) view.findViewById(R.id.loading);
        userName = (TextView) view.findViewById(R.id.username_text);
        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity) getActivity());
        view.findViewById(R.id.btn_password).setOnClickListener(this);
        view.findViewById(R.id.btn_part).setOnClickListener(this);
        view.findViewById(R.id.btn_phone).setOnClickListener(this);
        view.findViewById(R.id.btn_myself).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        return view;
    }

    @Override
    public String getTitle() {
        return "Forth";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userName.setText("Teer");
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.btn_part:
                i.setClass(getActivity(), ParticipateActivity.class);
                startActivity(i);
                break;
            case R.id.btn_myself:
                i.setClass(getActivity(), MyselfActivity.class);
                startActivity(i);
                break;
            case R.id.btn_password:
                i.setClass(getActivity(), PasswordActivity.class);
                startActivity(i);
                break;
            case R.id.btn_phone:
                i.setClass(getActivity(), PhoneActivity.class);
                startActivity(i);
                break;
            case R.id.btn_logout:
                SharedPreferences s = getActivity().getSharedPreferences("sjd", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = s.edit();
                e.putString("token", "");
                e.commit();
                i.setClass(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
