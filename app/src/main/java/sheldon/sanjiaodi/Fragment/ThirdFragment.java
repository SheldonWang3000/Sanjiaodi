package sheldon.sanjiaodi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class ThirdFragment extends BaseFragment {


    private Button btn_get;
    private Button btn_post;
    private RelativeLayout process;
    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_third, null);
        btn_get = (Button) view.findViewById(R.id.button_get);
        btn_post = (Button) view.findViewById(R.id.button_post);
        process = (RelativeLayout) view.findViewById(R.id.loading);
        return view;
    }

    @Override
    protected String getTitle() {
        return "Third";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process.setVisibility(View.VISIBLE);
                MyVolley.getInstance(getContext()).get(
                        "http://192.168.1.102/get.php?id=123",
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                SJDLog.i("onResponse", response.toString());
                                try {
                                    JSONArray array = response.getJSONArray("array");
                                    process.setVisibility(View.GONE);
                                    btn_get.setText(array.getJSONObject(0).getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                SJDLog.d("third get error", error);
                                btn_get.setText("error volley");
                            }
                        });

            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "908");
                JSONObject jsonObject = new JSONObject(params);
                MyVolley.getInstance(getContext()).post(
                        "http://192.168.1.102/post.php",
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                SJDLog.i("onResponse", response.toString());
                                try {
                                    btn_post.setText(response.getString("idd"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                SJDLog.d("third get error post", error.getMessage());
                                btn_post.setText("error volley post");
                            }
                        });

            }
        });
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

}
