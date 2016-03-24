package sheldon.sanjiaodi.ListItem;

/**
 * Created by Sheldon on 2016/3/23.
 */
public class ItemData {
    private String id;
    private String url;
    private String title;
    private String startTime;
    private String publishTime;

    public ItemData(String id, String url, String title, String startTime, String publishTime) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.startTime = startTime;
        this.publishTime = publishTime;
    }
}
