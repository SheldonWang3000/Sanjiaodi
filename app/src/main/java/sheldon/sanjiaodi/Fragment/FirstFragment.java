package sheldon.sanjiaodi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import sheldon.sanjiaodi.Activity.ContentActivity;
import sheldon.sanjiaodi.Activity.MainActivity;
import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.ListItem.ContentData;
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class FirstFragment extends BaseFragment {

    private ListView listView;
    private PtrClassicFrameLayout ptrFrameLayout;
    private ItemAdapter itemAdapter;
    private List<ContentData> itemList;
    private List<ContentData> moreItemList;
    private List<ContentData> currentItemList;
    private List<ContentData> currentMoreItemList;
    private RelativeLayout process;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private RadioGroup typeGroup;
    //0代表全部，1代表社团，4代表学术,7代表校外,6代表教师
    private int typeId = 0;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_first, null);
        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity)context);
        ((TextView) view.findViewById(R.id.header_text)).setText("三角地");

        process = (RelativeLayout) view.findViewById(R.id.loading);
        typeGroup = (RadioGroup) view.findViewById(R.id.btn_content_type);
        process.setVisibility(View.VISIBLE);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(4.3f);
        listView = (ListView) view.findViewById(R.id.list_view);

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.content_type_0:
                        typeId = 0;
                        break;
                    case R.id.content_type_1:
                        typeId = 1;
                        break;
                    case R.id.content_type_2:
                        typeId = 4;
                        break;
                    case R.id.content_type_3:
                        typeId = 7;
                        break;
                    case R.id.content_type_4:
                        typeId = 6;
                        break;
                    default:
                        break;
                }
                changeSelectedFontSize();
                switchType();
                listView.setSelection(0);
                loadMoreListViewContainer.loadMoreFinish(true, true);
            }
        });

        loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                closeAll();
                for (int i = 0; i < 3; ++i) {
                    if (currentMoreItemList.isEmpty()) {
                        loadMoreContainer.loadMoreFinish(false, false);
                        break;
                    }
                    currentItemList.add(currentMoreItemList.get(0));
                    currentMoreItemList.remove(0);
                }
                SJDLog.i("size", currentItemList.size() + ":" + currentMoreItemList.size());
                itemAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(!currentMoreItemList.isEmpty(),
                        !currentMoreItemList.isEmpty());
            }
        });

        itemList = new ArrayList<>();
        moreItemList = new ArrayList<>();
        currentItemList = new ArrayList<>();
        currentMoreItemList = new ArrayList<>();

        itemAdapter = new ItemAdapter(currentItemList, context);
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
                    bundle.putSerializable("item", currentItemList.get(position));
                    i.putExtras(bundle);
                    i.setClass(context, ContentActivity.class);
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
                refreshList();
            }
        });
        return view;
    }

    private void changeSelectedFontSize() {
        for (int i = 0; i < typeGroup.getChildCount(); ++i) {
            RadioButton tmp = (RadioButton) typeGroup.getChildAt(i);
            tmp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        }
        switch(typeId) {
            case 0:
                ((RadioButton)typeGroup.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                break;
            case 1:
                ((RadioButton)typeGroup.getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                break;
            case 4:
                ((RadioButton)typeGroup.getChildAt(2)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                break;
            case 7:
                ((RadioButton)typeGroup.getChildAt(3)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                break;
            case 6:
                ((RadioButton)typeGroup.getChildAt(4)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                break;
            default:
                break;
        }
    }

    private void switchType() {
        currentItemList.clear();
        currentMoreItemList.clear();
        if (typeId == 0) {
            currentItemList.addAll(itemList);
            currentMoreItemList.addAll(moreItemList);
        } else {
            for (int i = 0; i < itemList.size(); ++i) {
                ContentData tmp = itemList.get(i);
                if (tmp.typeId == typeId) {
                    currentItemList.add(tmp);
                }
            }
            int i = 0;
            for (; i < moreItemList.size(); ++i) {
                if (currentItemList.size() < 9) {
                    ContentData tmp = moreItemList.get(i);
                    if (tmp.typeId == typeId) {
                        currentItemList.add(tmp);
                    }
                } else {
                    break;
                }
            }
            for (; i < moreItemList.size(); ++i) {
                ContentData tmp = moreItemList.get(i);
                if (tmp.typeId == typeId) {
                    currentMoreItemList.add(tmp);
                }
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public String getTitle() {
        return "First";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SharedPreferences s = context.getSharedPreferences("sjd", Context.MODE_PRIVATE);
        String oldContent = s.getString("content", "");
        SJDLog.i("initData", oldContent);
        if (!TextUtils.isEmpty(oldContent)) {
            try {
                JSONArray array = new JSONArray(oldContent);
                getList(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        refreshList();
    }

    private void refreshList() {
        closeAll();
        loadMoreListViewContainer.loadMoreFinish(true, true);
        String uid = null;
        try {
            uid = Info.getUid(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(uid)) {
            //TODO 登录异常
        }
        MyVolley.getContent(context, uid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONArray array = (JSONArray) response;
                            SJDLog.i("refreshList", array.toString());
                            SharedPreferences s = context.getSharedPreferences("sjd", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = s.edit();
                            editor.putString("content", response.toString());
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
                        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                        process.setVisibility(View.GONE);
                        ptrFrameLayout.refreshComplete();
                    }
                });
    }
    private void closeAll() {
        int openPosition = itemAdapter.getOpenItems().get(0);
        if (openPosition != -1) {
            itemAdapter.closeItem(openPosition);
        }
    }

    private void getList(JSONArray array) throws JSONException{
        itemList.clear();
        moreItemList.clear();
        List<ContentData> list = itemList;
        for (int i = 0; i < array.length(); ++i) {
            JSONObject tmp = (JSONObject) array.get(i);
            ContentData tmpItem = new ContentData(tmp.getString("id"), tmp.getString("image_big"),
                    tmp.getString("image_big"), tmp.getString("title"), tmp.getString("sTime"),
                    tmp.getString("eTime"), tmp.getString("update_time"),
                    tmp.getString("deadline"), tmp.getString("is_sign"), tmp.getString("explain"),
                    tmp.getBoolean("collect"), tmp.getInt("type"));
            list.add(tmpItem);
            if (i == 9) {
                list = moreItemList;
            }
        }
        switchType();

        SJDLog.i("size", currentItemList.size() + ":" + currentMoreItemList.size());
    }

}
