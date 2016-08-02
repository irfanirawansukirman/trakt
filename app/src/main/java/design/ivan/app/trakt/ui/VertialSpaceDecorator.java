package design.ivan.app.trakt.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ivanm on 8/2/16.
 */
public class VertialSpaceDecorator extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;

    public VertialSpaceDecorator(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
    }
}
