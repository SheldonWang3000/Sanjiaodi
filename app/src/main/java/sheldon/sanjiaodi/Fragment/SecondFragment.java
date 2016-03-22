package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.ImageActivity;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class SecondFragment extends BaseFragment {


    private ImageView imageView;
    private ImageView imageView2;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_second, null);
        imageView = ((ImageView) view.findViewById(R.id.image_view));
        imageView2 = ((ImageView) view.findViewById(R.id.image_view2));
        return view;
    }

    @Override
    protected String getTitle() {
        return "Second";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        final String url = "http://ww2.sinaimg.cn/bmiddle/9c079b04gw1f1qjtatuwxj20m80gojsn.jpg";
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("url", "http://sabrinadepestre.com/wp-content/uploads/2016/02/space-wallpaper-29.jpg");
//                i.putExtra("resource", R.mipmap.flower);
                i.setClass(getContext(), ImageActivity.class);
                startActivity(i);
            }
        });

        MyVolley.getInstance(getContext()).getImage(imageView, url, null);
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

}
