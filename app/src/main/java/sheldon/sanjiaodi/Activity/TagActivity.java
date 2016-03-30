package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.swipe.util.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.ListItem.ItemData;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class TagActivity extends Activity {

    private ListView listView;
    private PtrClassicFrameLayout ptrFrameLayout;
    private ItemAdapter itemAdapter;
    private List<ItemData> itemList;
    private List<ItemData> moreItemList;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private RelativeLayout process;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        id = i.getStringExtra("id");

        ((TextView) findViewById(R.id.header_text)).setText(title);
        ((TextView) findViewById(R.id.tag_title)).setText(title);

        process = (RelativeLayout) findViewById(R.id.loading);
        process.setVisibility(View.VISIBLE);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.tag_ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(4.3f);
        listView = (ListView) findViewById(R.id.tag_list_view);

        loadMoreListViewContainer =
                (LoadMoreListViewContainer) findViewById(R.id.tag_load_more);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                closeAll();
                for (int i = 0; i < 3; ++i) {
                    if (moreItemList.isEmpty()) {
                        loadMoreContainer.loadMoreFinish(false, false);
                        break;
                    }
                    itemList.add(moreItemList.get(0));
                    moreItemList.remove(0);
                }
                SJDLog.i("size", itemList.size() + ":" + moreItemList.size());
                itemAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(!moreItemList.isEmpty(), !moreItemList.isEmpty());
            }
        });

        itemList = new ArrayList<>();
        moreItemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList, this);
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
                    i.setClass(TagActivity.this, ContentActivity.class);
                    startActivity(i);
                }

            }
        });
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshList(TagActivity.this.id);
            }
        });

        initData();

    }

    private void closeAll() {
        int openPosition = itemAdapter.getOpenItems().get(0);
        if (openPosition != -1) {
            itemAdapter.closeItem(openPosition);
        }
    }

    private void initData() {

        SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
        String oldTagContent = s.getString("tag" + id, "");
        SJDLog.i("initTagContent", oldTagContent);
        if (!TextUtils.isEmpty(oldTagContent)) {
            try {
                JSONArray array = new JSONArray(oldTagContent);
                getList(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        refreshList(id);
    }

    private void getList(JSONArray array) throws JSONException{
        itemList.clear();
        moreItemList.clear();
        List<ItemData> list = itemList;
        for (int i = 0; i < array.length(); ++i) {
            JSONObject tmp = (JSONObject) array.get(i);
            ItemData tmpItem = new ItemData(tmp.getString("id"), tmp.getString("image_big"),
                    tmp.getString("image_big"), tmp.getString("title"), tmp.getString("sTime"),
                    tmp.getString("eTime"), tmp.getString("update_time"),
                    tmp.getString("deadline"), tmp.getString("is_sign"), tmp.getString("explain"),
                    tmp.getBoolean("collect")
            );
            list.add(tmpItem);
            if (i == 9) {
                list = moreItemList;
            }
        }
        itemAdapter.notifyDataSetChanged();

        SJDLog.i("size", itemList.size() + ":" + moreItemList.size());
    }


    private void refreshList(String id) {
        closeAll();
        loadMoreListViewContainer.loadMoreFinish(true, true);
        String uid = null;
        try {
            uid = Info.getUid(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(uid)) {
            //TODO 登录异常
        }
        MyVolley.getActivityByTag(this, id, uid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONArray array = (JSONArray) response;
                            SJDLog.i("refreshList", array.toString());
                            SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = s.edit();
                            editor.putString("tag" + TagActivity.this.id, response.toString());
                            editor.commit();
                            getList(array);
                            process.setVisibility(View.GONE);
                            ptrFrameLayout.refreshComplete();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SJDLog.d("getContentError", error);
                        Toast.makeText(TagActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        process.setVisibility(View.GONE);
                        ptrFrameLayout.refreshComplete();
                    }
                });

    }

}
