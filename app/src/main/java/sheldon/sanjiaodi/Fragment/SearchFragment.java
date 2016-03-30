package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.swipe.util.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sheldon.sanjiaodi.Activity.ContentActivity;
import sheldon.sanjiaodi.Activity.MainActivity;
import sheldon.sanjiaodi.Activity.TagActivity;
import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.ListItem.ItemData;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener{

    private ListView listView;
    private ItemAdapter itemAdapter;
    private List<ItemData> itemList;
    private Map<Integer, TagData> tagDataMap;
    private TagData[] datas;
    private RelativeLayout process;


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_search, null);

        ((TextView) view.findViewById(R.id.header_text)).setText("活动标签");

        listView = (ListView) view.findViewById(R.id.hot_list_view);
        process = (RelativeLayout) view.findViewById(R.id.loading);
        process.setVisibility(View.VISIBLE);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList, getContext());
        listView.setAdapter(itemAdapter);
        itemAdapter.setMode(Attributes.Mode.Single);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int openPosition = itemAdapter.getOpenItems().get(0);
                if (openPosition != -1) {
                    itemAdapter.closeItem(openPosition);
                } else {
                    SJDLog.i("onListClick", position);
                    Intent i = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", itemList.get(position));
                    i.putExtras(bundle);
                    i.setClass(getContext(), ContentActivity.class);
                    startActivity(i);
                }

            }
        });

        datas = new TagData[8];
        tagDataMap = new HashMap<>();
        for (int i = 1; i <= 8; ++i) {
            int resource = getResources().getIdentifier("hot_tag_" + i,
                    "id", getContext().getPackageName());
            TextView tmpTextView = (TextView) view.findViewById(resource);
            tmpTextView.setOnClickListener(this);
            TagData tmpData = new TagData();
            tmpData.tagView = tmpTextView;
            datas[i - 1] = tmpData;
            tagDataMap.put(resource, tmpData);
        }
        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity) context);
        return view;
    }

    @Override
    public String getTitle() {
        return "Search";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        MyVolley.getTag(context,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONArray array = (JSONArray) response;
                        try {
                            for (int i = 0; i < array.length(); ++i) {
                                datas[i].title = ((JSONObject) array.get(i)).getString("tag");
                                datas[i].id = ((JSONObject) array.get(i)).getString("id");
                                datas[i].tagView.setText(datas[i].title);
                            }
                        } catch (JSONException ignored) {
                            SJDLog.w("tagResponse", ignored.toString());
                            ignored.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SJDLog.w("getTagError", error);
                    }
                });
        String uid = null;
        try {
            uid = Info.getUid(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(uid)) {
            //TODO 登录异常
        }
        MyVolley.getHotActivity(context, uid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONArray array = (JSONArray) response;
                        try {
                            for (int i = 0; i < array.length(); ++i) {
                                JSONObject tmp = (JSONObject) array.get(i);
                                ItemData tmpItem = new ItemData(tmp.getString("id"), tmp.getString("image_big"),
                                        tmp.getString("image_big"), tmp.getString("title"), tmp.getString("sTime"),
                                        tmp.getString("eTime"), tmp.getString("update_time"),
                                        tmp.getString("deadline"), tmp.getString("is_sign"), tmp.getString("explain"),
                                        tmp.getBoolean("collect")
                                );
                                itemList.add(tmpItem);
                            }
                        } catch (JSONException jsonError) {
                            SJDLog.w("hotActivityResponseError", jsonError);
                            jsonError.printStackTrace();
                        }
                        process.setVisibility(View.GONE);
                        itemAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SJDLog.w("getHotError", error);
                    }
                });


    }

    @Override
    public void onClick(View v) {
        SJDLog.i("click", v.getId() + "");
        Intent i = new Intent();
        TagData tmp = tagDataMap.get(v.getId());
        i.putExtra("title", tmp.title);
        i.putExtra("id", tmp.id);
        i.setClass(getContext(), TagActivity.class);
        startActivity(i);
    }

    private class TagData {
        public String id;
        public String title;
        public TextView tagView;
    }
}
