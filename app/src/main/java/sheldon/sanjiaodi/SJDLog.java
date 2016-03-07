package sheldon.sanjiaodi;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class SJDLog {
    private static boolean open = true;
    private static String header = "--------------SJD------------";

    public static void d(Object tag, Object msg) {
        if (open == false) {
            return;
        }
        tag = handleMsgOrTag(tag);
        msg = handleMsgOrTag(msg);
        android.util.Log.d(header + String.valueOf(tag), String.valueOf(msg));
    }
    public static void i(Object tag, Object msg) {
        if (open == false) {
            return;
        }
        tag = handleMsgOrTag(tag);
        msg = handleMsgOrTag(msg);
        android.util.Log.d(header + String.valueOf(tag), String.valueOf(msg));
    }
    public static void e(Object tag, Object msg) {
        if (open == false) {
            return;
        }
        tag = handleMsgOrTag(tag);
        msg = handleMsgOrTag(msg);
        android.util.Log.d(header + String.valueOf(tag), String.valueOf(msg));
    }
    public static void v(Object tag, Object msg) {
        if (open == false) {
            return;
        }
        tag = handleMsgOrTag(tag);
        msg = handleMsgOrTag(msg);
        android.util.Log.d(header + String.valueOf(tag), String.valueOf(msg));
    }
    public static void w(Object tag, Object msg) {
        if (open == false) {
            return;
        }
        tag = handleMsgOrTag(tag);
        msg = handleMsgOrTag(msg);
        android.util.Log.d(header + String.valueOf(tag), String.valueOf(msg));
    }
    private static Object handleMsgOrTag(Object msgOrTag) {
        if (msgOrTag == null) {
            msgOrTag = "[null]";
        } else if (msgOrTag.toString().trim().length() == 0) {
            msgOrTag = "[\"\"]";
        } else {
            msgOrTag = msgOrTag.toString().trim();
        }
        return msgOrTag;
    }

}
