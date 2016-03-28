package sheldon.sanjiaodi;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Sheldon on 2016/3/25.
 */
public class UploadRequest extends Request<String> {

    private Response.Listener mListener ;

    private Image uploadImage;

    private String BOUNDARY = "-------------sjd";
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public UploadRequest(String url, Image uploadImage,
                         Response.Listener listener,
                         Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        setShouldCache(false);
        this.uploadImage = uploadImage;

        setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 这里开始解析数据
     * @param response Response from the network
     * @return
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String mString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            SJDLog.v("parseNetworkResponse", "====mString===" + mString);

            return Response.success(mString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 回调正确的数据
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (uploadImage == null) {
            return super.getBody() ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        StringBuilder sb= new StringBuilder() ;
        /*第一行*/
        //`"--" + BOUNDARY + "\r\n"`
        sb.append("--").append(BOUNDARY);
        sb.append("\r\n") ;
        /*第二行*/
        //Content-Disposition: form-data; name="参数的名称"; filename="上传的文件名" + "\r\n"
        sb.append("Content-Disposition: form-data;");
        sb.append(" name=\"");
        sb.append(uploadImage.getParamsName()) ;
        sb.append("\"") ;
        sb.append("; filename=\"") ;
        sb.append(uploadImage.getFileName()) ;
        sb.append("\"");
        sb.append("\r\n") ;
        /*第三行*/
        //Content-Type: 文件的 mime 类型 + "\r\n"
        sb.append("Content-Type: ");
        sb.append(uploadImage.getMime());
        sb.append("\r\n");
        /*第四行*/
        //"\r\n"
        sb.append("\r\n");
        try {
            bos.write(sb.toString().getBytes("utf-8"));
            /*第五行*/
            //文件的二进制数据 + "\r\n"
            bos.write(uploadImage.getValue());
            bos.write("\r\n".getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*结尾行*/
        //`"--" + BOUNDARY + "--" + "\r\n"`
        String endLine = "--" + BOUNDARY + "--" + "\r\n" ;
        try {
            bos.write(endLine.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SJDLog.v("getBody", "=====formImage====\n" + bos.toString()) ;
        return bos.toByteArray();
    }

    //Content-Type: multipart/form-data; boundary=----------8888888888888
    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA + "; boundary="+BOUNDARY;
    }
}
