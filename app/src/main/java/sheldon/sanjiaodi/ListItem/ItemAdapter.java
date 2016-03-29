package sheldon.sanjiaodi.ListItem;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.Date;
import java.util.List;

import sheldon.sanjiaodi.Activity.ImageActivity;
import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.RefreshInterface;
import sheldon.sanjiaodi.SJDLog;

/**
 * Created by Sheldon on 2016/3/5.
 */
public class ItemAdapter extends BaseSwipeAdapter {

    private List<ItemData> list;
    private Context context;
    private RefreshInterface listener;

    public ItemAdapter(List<ItemData> list, Context context) {
        this.list = list;
        this.context = context;
        this.listener = null;
    }
    public ItemAdapter(List<ItemData> list, Context context, RefreshInterface listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
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

    private String getDateFromTime(int position) {
        Long time = Long.valueOf(list.get(position).publishTime);
        Date date = new Date(time * 1000);
        return "开始时间：" + DateFormat.format("yyyy-MM-dd(EEEE)", date).toString();
    }

    private String getDateDiff(int position) {
        int minute = 60;
        int hour = minute * 60;
        int day = hour * 24;
        int week = day * 7;
        int month = day * 30;
        long now = new Date().getTime() / 1000;
        long diff = now - Long.valueOf(list.get(position).publishTime);
        int diffMonth = (int) (diff / month);
        int diffWeek = (int) (diff / week);
        int diffDay = (int) (diff / day);
        int diffHour = (int) (diff / hour);
        int diffMinute = (int) (diff / minute);
        if (diffMonth >= 1) {
            return diffMonth + "月前";
        }
        if (diffWeek >= 1) {
            return diffWeek + "周前";
        }
        if (diffDay >= 1) {
            return diffDay + "天前";
        }
        if (diffHour >= 1) {
            return diffHour + "小时前";
        }
        if (diffMinute >= 1) {
            return diffMinute + "分钟前";
        }
        return "刚刚";
    }

    @Override
    public void fillValues(final int position, View convertView) {
        ((TextView) convertView.findViewById(R.id.time_text))
                .setText(getDateDiff(position));
        ((TextView) convertView.findViewById(R.id.list_content_title))
                .setText(list.get(position).title);
        ((TextView) convertView.findViewById(R.id.start_time_text))
                .setText(getDateFromTime(position));
        ((TextView) convertView.findViewById(R.id.swipe_menu_0))
                .setText(list.get(position).collect ? "已收藏" : "收藏");

        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_small_image);
        final String smallUrl = list.get(position).smallUrl;

        MyVolley.getImage(context, imageView, smallUrl);
        convertView.findViewById(R.id.list_small_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("url", smallUrl);
                i.setClass(context, ImageActivity.class);
                context.startActivity(i);
            }
        });
        convertView.findViewById(R.id.swipe_menu_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView textView = (TextView) view.findViewById(R.id.swipe_menu_0);
                String str = textView.getText().toString();
                switch (str) {
                    case "收藏":
                        try {
                            MyVolley.collect(context, Info.getUid(context), list.get(position).id,
                                    new Response.Listener() {
                                        @Override
                                        public void onResponse(Object response) {
                                            SJDLog.i("collectSuccess", response);
                                            if (response.equals("1")) {
                                                textView.setText("已收藏");
                                                Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                                                if (listener != null) {
                                                    listener.refresh();
                                                }
                                            }else {
                                                Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            SJDLog.w("collectError", error);
                                            Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    case "已收藏":
                        try {
                            MyVolley.unCollect(context, Info.getUid(context), list.get(position).id,
                                    new Response.Listener() {
                                        @Override
                                        public void onResponse(Object response) {
                                            SJDLog.i("unCollectSuccess", response);
                                            if (response.equals("1")) {
                                                textView.setText("收藏");
                                                Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                                                if (listener != null) {
                                                    listener.refresh();
                                                }
                                            }else {
                                                Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            SJDLog.w("unCollectError", error);
                                            Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
