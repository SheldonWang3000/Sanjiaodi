package sheldon.sanjiaodi;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Sheldon on 2016/3/28.
 */
public class Info {
    private static String uid;

    public static String getUid(Context context) throws Exception {
        if (TextUtils.isEmpty(uid)) {
            SJDLog.w("getUid", "getShared");
            SharedPreferences s = context.getSharedPreferences("sjd", Context.MODE_PRIVATE);
            uid = s.getString("uid", "");
            if (TextUtils.isEmpty(uid)) {
                throw new Exception("No Uid");
            }else {
                return uid;
            }
        }else {
            return uid;
        }
    }

    public static void removeUid() {
        uid = null;
    }
}
