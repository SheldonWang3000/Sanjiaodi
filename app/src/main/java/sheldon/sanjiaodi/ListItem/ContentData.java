package sheldon.sanjiaodi.ListItem;

/**
 * Created by Sheldon on 2016/3/30.
 */
public class ContentData extends ItemData {

    public int typeId;
    public ContentData(String id, String bigUrl, String smallUrl,
                       String title, String startTime, String endTime,
                       String publishTime, String deadline, String sign,
                       String explain, Boolean collect, int typeId) {
        super(id, bigUrl, smallUrl, title, startTime, endTime,
                publishTime, deadline, sign, explain, collect);
        this.typeId = typeId;
    }
}
