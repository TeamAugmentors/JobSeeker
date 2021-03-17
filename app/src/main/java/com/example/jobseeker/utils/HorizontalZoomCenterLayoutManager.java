package com.example.jobseeker.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalZoomCenterLayoutManager extends LinearLayoutManager {
    // Shrink the cards around the center up to 50%
    private final float mShrinkAmount = 0.3f;
    // The cards will be at 50% when they are 75% of the way between the
    // center and the edge.
    private final float mShrinkDistance = 0.75f;


    public HorizontalZoomCenterLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }


    @Override
    public int scrollHorizontallyBy(int dx,
                                    RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

        float midpoint = getWidth() / 2.f;
        float d0 = 0.f;
        float d1 = mShrinkDistance * midpoint;
        float s0 = 1.f;
        float s1 = 1.f - mShrinkAmount;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMidpoint =
                    (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
            float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
            float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
            int translationDirection;
            if (childMidpoint > midpoint) {
                translationDirection = -1;
            } else {
                translationDirection = 1;
            }
            float translationXFromScale = translationDirection * child.getWidth() * (1 - scale) / 2f;
            child.setTranslationX(translationXFromScale);
            child.setScaleX(scale);
            child.setScaleY(scale);
        }

        return scrolled;
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scrollHorizontallyBy(0, recycler, state);
    }

}
