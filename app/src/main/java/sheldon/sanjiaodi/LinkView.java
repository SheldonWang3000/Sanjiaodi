package sheldon.sanjiaodi;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Sheldon on 2016/3/18.
 */
public class LinkView extends TextView{

    private Context context;
    private boolean click;

    public LinkView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public LinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LinkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        this.setClickable(true);
        int[][] states = new int[2][];
        states[0] = new int[] {android.R.attr.state_pressed};
        states[1] = new int[] {};
        int[] colors = new int[] {Color.RED, Color.BLUE};
        this.setTextColor(new ColorStateList(states, colors));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = ((TextView)v).getText().toString();
                if (Character.isDigit(link.charAt(0))) {
                    Intent callIntent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + link));
                    context.startActivity(callIntent);
                } else {
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                    urlIntent.setData(Uri.parse(link));
                    context.startActivity(urlIntent);
                }
            }
        });
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        super.setText(content, type);
    }

}
