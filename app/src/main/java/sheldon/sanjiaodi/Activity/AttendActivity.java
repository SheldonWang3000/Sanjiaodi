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
import java.util.Date;

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
import sheldon.sanjiaodi.RefreshInterface;
import sheldon.sanjiaodi.SJDLog;

public class AttendActivity extends Activity implements View.OnClickListener, RefreshInterface {

    private PtrClassicFrameLayout ptrFrameLayout;
    private ListView listView;
    private ItemAdapter itemAdapter;
    private ArrayList<ItemData> doingList;
    private ArrayList<ItemData> currentList;
    private ArrayList<ItemData> currentMoreList;
    private ArrayList<ItemData> doneList;
    private ArrayList<ItemData> doneMoreList;
    private ArrayList<ItemData> doingMoreList;
    private String current = "doing";
    private RelativeLayout process;
    private LoadMoreListViewContainer loadMoreListViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);
        ((TextView) findViewById(R.id.header_text)).setText("参与的活动");

        process = (RelativeLayout) findViewById(R.id.loading);
        process.setVisibility(View.VISIBLE);

        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.part_ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(4.3f);

        listView = (ListView) findViewById(R.id.part_list_view);

        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.part_load_more);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                closeAll();
                for (int i = 0; i < 3; ++i) {
                    if (currentMoreList.isEmpty()) {
                        loadMoreContainer.loadMoreFinish(false, false);
                        break;
                    }
                    currentList.add(currentMoreList.get(0));
                    currentMoreList.remove(0);
                }
                SJDLog.i("size", currentList.size() + ":" + currentMoreList.size());
                itemAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(!currentMoreList.isEmpty(),
                        !currentMoreList.isEmpty());

            }
        });

        doingList = new ArrayList<>();
        doneList = new ArrayList<>();
        doingMoreList = new ArrayList<>();
        doneMoreList = new ArrayList<>();
        currentList = new ArrayList<>();
        currentMoreList = new ArrayList<>();

        itemAdapter = new ItemAdapter(currentList, this, this);
        listView.setAdapter(itemAdapter);
        itemAdapter.setMode(Attributes.Mode.Single);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int openPosition = itemAdapter.getOpenItems().get(0);
                if (openPosition != -1) {
                    itemAdapter.closeItem(openPosition);
                } else {
                    Intent i = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", currentList.get(position));
                    i.putExtras(bundle);
                    i.setClass(AttendActivity.this, ContentActivity.class);
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
                refresh();
            }
        });

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.part_doing_text).setOnClickListener(this);
        findViewById(R.id.part_done_text).setOnClickListener(this);

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
        String oldAttend = s.getString("attend", "");
        SJDLog.i("initAttendData", oldAttend);
        if (!TextUtils.isEmpty(oldAttend)) {
            try {
                JSONArray array = new JSONArray(oldAttend);
                getList(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.part_doing_text:
                checkToDoing();
                break;
            case R.id.part_done_text:
                checkToDone();
                break;
            default:
                break;
        }
    }

    void checkToDoing() {
        current = "doing";
        loadMoreListViewContainer.loadMoreFinish(true, true);
        currentList.clear();
        currentMoreList.clear();
        currentList.addAll(doingList);
        currentMoreList.addAll(doingMoreList);

        itemAdapter.notifyDataSetChanged();
    }


    void checkToDone() {
        current = "done";
        loadMoreListViewContainer.loadMoreFinish(true, true);
        currentList.clear();
        currentMoreList.clear();
        currentList.addAll(doneList);
        currentMoreList.addAll(doneMoreList);

        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {
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
        MyVolley.getAttend(this, uid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONArray array = (JSONArray) response;
                            SJDLog.i("refreshAttendList", array.toString());
                            SharedPreferences s = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = s.edit();
                            editor.putString("attend", response.toString());
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
                        SJDLog.d("getAttendError", error);
                        Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
                        process.setVisibility(View.GONE);
                        ptrFrameLayout.refreshComplete();
                    }
                });

    }

    private void getList(JSONArray array) throws JSONException {
        doingList.clear();
        doneList.clear();
        doingMoreList.clear();
        doneMoreList.clear();
        long now = new Date().getTime() / 1000;
        for (int i = 0; i < array.length(); ++i) {
            JSONObject tmp = (JSONObject) array.get(i);
            ItemData tmpItem = new ItemData(tmp.getString("id"), tmp.getString("image_big"),
                    tmp.getString("image_big"), tmp.getString("title"), tmp.getString("sTime"),
                    tmp.getString("eTime"), tmp.getString("update_time"),
                    tmp.getString("deadline"), tmp.getString("is_sign"), tmp.getString("explain"),
                    tmp.getBoolean("collect")
            );
            if (Long.valueOf(tmpItem.endTime) >= now) {
                //尚未结束
                if (doingList.size() < 10) {
                    doingList.add(tmpItem);
                }else {
                    doingMoreList.add(tmpItem);
                }
            }else {
                //已结束
                if (doneList.size() < 10) {
                    doneList.add(tmpItem);
                }else {
                    doneMoreList.add(tmpItem);
                }
            }
        }
        currentList.clear();
        currentMoreList.clear();
        if (current.equals("doing")) {
            currentList.addAll(doingList);
            currentMoreList.addAll(doingMoreList);

        }else {
            currentList.addAll(doneList);
            currentMoreList.addAll(doneMoreList);
        }
        itemAdapter.notifyDataSetChanged();

        SJDLog.i("doingSize", doingList.size() + ":" + doingMoreList.size());
        SJDLog.i("doneSize", doneList.size() + ":" + doneMoreList.size());

    }
}
