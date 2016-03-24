package sheldon.sanjiaodi.ListItem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import sheldon.sanjiaodi.Activity.ImageActivity;
import sheldon.sanjiaodi.R;

//import com.daimajia.swipe.adapters.BaseSwipeAdapter;

/**
 * Created by Sheldon on 2016/3/5.
 */
public class ItemAdapter extends BaseSwipeAdapter {

    List<String> list;
    Context context;

    public ItemAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_item;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_item, null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView textView = (TextView) convertView.findViewById(R.id.time_text);
        convertView.findViewById(R.id.list_small_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("url", "http://ww4.sinaimg.cn/mw690/cd5de796gw1f282geyquij203i034jr8.jpg");
                i.setClass(context, ImageActivity.class);
                context.startActivity(i);
            }
        });
        convertView.findViewById(R.id.swipe_menu_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view.findViewById(R.id.swipe_menu_0);
                String str = textView.getText().toString();
                switch (str) {
                    case "收藏":
                        //TODO 收藏
                        textView.setText("已收藏");
                        break;
                    case "已收藏":
                        //TODO 取消收藏
                        textView.setText("收藏");
                        break;
                    default:
                        break;
                }
            }
        });
        textView.setText(list.get(position));
    }

}
