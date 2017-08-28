package com.quickjob.quickjobFinal.quickjobFind.other;


import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class ScrollingFABBehavior  extends CoordinatorLayout.Behavior<com.github.clans.fab.FloatingActionButton> {
    private static final String TAG = "ScrollingFABBehavior";
    Handler mHandler;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, final com.github.clans.fab.FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);

        if (mHandler == null)
            mHandler = new Handler();


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
                Log.d("FabAnim","startHandler()");
            }
        },1000);

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, com.github.clans.fab.FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        //child -> Floating Action Button
        if (dyConsumed > 0) {
            Log.d("Scrolling","Up");
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            child.hide(true);
            //.animate().alpha(0).setInterpolator(new LinearInterpolator()).start();
        }
        if (dyConsumed < 0) {
            Log.d("Scrolling","down");
            child.show(true);
            //animate().alpha(100).setInterpolator(new LinearInterpolator()).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, com.github.clans.fab.FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        if(mHandler!=null) {
            mHandler.removeMessages(0);
            Log.d("Scrolling","stopHandler()");
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}