package sheldon.sanjiaodi.ListItem;

import java.io.Serializable;

/**
 * Created by Sheldon on 2016/3/23.
 */
public class ItemData implements Serializable {

    public String id;
    public String bigUrl;
    public String title;
    public String startTime;
    public String smallUrl;
    public String endTime;
    public String publishTime;
    public String deadline;
    public String sign;
    public String explain;
    public Boolean collect;

    public ItemData(String id, String bigUrl, String smallUrl,
                    String title, String startTime, String endTime,
                    String publishTime, String deadline, String sign,
                    String explain, Boolean collect) {
        this.id = id;
        this.bigUrl= bigUrl;
        this.smallUrl = smallUrl;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.publishTime = publishTime;
        this.deadline = deadline;
        this.sign = sign;
        this.explain = explain;
        this.collect = collect;
    }
}
