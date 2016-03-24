package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sheldon.sanjiaodi.Activity.ContentActivity;
import sheldon.sanjiaodi.Activity.MainActivity;
import sheldon.sanjiaodi.Activity.TagActivity;
import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener{

    private ListView listView;
    private ItemAdapter itemAdapter;
    private List<String> stringList;
    private TextView[] tags;
    private Map<Integer, TagData> tagDataMap;
    private TagData[] datas;
    private RelativeLayout process;


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        listView = (ListView) view.findViewById(R.id.hot_list_view);
        ((TextView) view.findViewById(R.id.header_text)).setText("活动标签");
        process = (RelativeLayout) view.findViewById(R.id.loading);
        stringList = new ArrayList<>();
        itemAdapter = new ItemAdapter(stringList, getContext());
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
                    i.setClass(getContext(), ContentActivity.class);
                    startActivity(i);
                }

            }
        });

        tags = new TextView[8];
        datas = new TagData[8];
        tagDataMap = new HashMap<>();
        for (int i = 1; i <= 8; ++i) {
            int resource = getResources().getIdentifier("hot_tag_" + i,
                    "id", getContext().getPackageName());
            tags[i - 1] = (TextView) view.findViewById(resource);
            tags[i - 1].setOnClickListener(this);
            TagData tmpData = new TagData();
            datas[i - 1] = tmpData;
            tagDataMap.put(resource, tmpData);
        }
        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity) getActivity());
        return view;
    }

    @Override
    public String getTitle() {
        return "Search";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        for (int i = 0; i < 5; ++i)
        {
            stringList.add(String.valueOf(i));
        }

        itemAdapter.notifyDataSetChanged();

        for (int i = 0; i < 8; ++i) {
            tags[i].setText("" + i);
            datas[i].title = "" + i;
            datas[i].id = 100 + i;
        }
    }

    @Override
    protected void setListener() {

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
        switch (v.getId()) {
            case R.id.hot_tag_1:
                break;
            case R.id.hot_tag_2:
                break;
            case R.id.hot_tag_3:
                break;
            case R.id.hot_tag_4:
                break;
            case R.id.hot_tag_5:
                break;
            case R.id.hot_tag_6:
                break;
            case R.id.hot_tag_7:
                break;
            case R.id.hot_tag_8:
                break;
            default:
        }
    }

    private class TagData {
        public int id;
        public String title;
    }
}
