package sheldon.sanjiaodi;

import android.graphics.Bitmap;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sheldon on 2016/3/25.
 */
public class Image {

    private String paramsName;
    private String fileName;

    private String mineType;
    private Bitmap mBitmap ;

    public Image(Bitmap mBitmap, String location) {
        this.mBitmap = mBitmap;
        mineType = MimeTypeMap.getSingleton().
                getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(location));
        fileName = location.substring(location.lastIndexOf("/") + 1, location.length());
    }

    public String getParamsName() {
//        return paramsName;
        return "file" ;
    }

    public String getFileName() {
        SJDLog.i("fileName", fileName);
        return fileName;
    }

    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }

    public String getMime() {
        SJDLog.i("mineType", mineType);
        return mineType;
    }
}
