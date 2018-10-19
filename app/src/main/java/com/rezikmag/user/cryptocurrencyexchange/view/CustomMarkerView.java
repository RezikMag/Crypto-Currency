package com.rezikmag.user.cryptocurrencyexchange.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.rezikmag.user.cryptocurrencyexchange.R;
import com.rezikmag.user.cryptocurrencyexchange.view.formatters.NumberUtils;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview

        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(new NumberUtils().formatPrice(e.getY())); // set the entry-value as the display text
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), 0);
    }


    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        posX += getOffset().x;
        posY = 0;

        canvas.translate(posX, posY);
        draw(canvas);
        canvas.translate(-posX, -posY);
    }
}
