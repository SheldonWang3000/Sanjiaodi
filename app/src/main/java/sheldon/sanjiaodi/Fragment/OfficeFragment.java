package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.Activity.ImageActivity;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/18.
 */
public class OfficeFragment extends BaseFragment{
    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_office, null);
        view.findViewById(R.id.image_pku_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("resource", R.mipmap.pku_admin);
                i.setClass(context, ImageActivity.class);
                startActivity(i);
            }
        });
        view.findViewById(R.id.image_pku_department).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("resource", R.mipmap.pku_department);
                i.setClass(context, ImageActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

}
