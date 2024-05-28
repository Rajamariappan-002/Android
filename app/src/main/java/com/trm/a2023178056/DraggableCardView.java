package com.trm.a2023178056;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class DraggableCardView extends CardView {
    private float dX, dY; // Variables to store initial touch position
    private LinearLayout linearLayout;

    public DraggableCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public DraggableCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Find the LinearLayout inside the DraggableCardView
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            linearLayout = (LinearLayout) getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                dX = getX() - event.getRawX();
                dY = getY() - event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                // Move the card view
                animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();

                // Adjust the height of the linear layout
                if (linearLayout != null) {
                    ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height -= (event.getRawY() - getY());
                        linearLayout.setLayoutParams(layoutParams);
                    }
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        // Handle click event here
        return true;
    }
}
