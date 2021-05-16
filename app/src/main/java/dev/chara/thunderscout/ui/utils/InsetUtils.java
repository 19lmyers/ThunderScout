package dev.chara.thunderscout.ui.utils;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

public final class InsetUtils {

    public static int dpToPixel(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void requestApplyInsetsWhenAttached(View view) {
        if (view.isAttachedToWindow()) {
            ViewCompat.requestApplyInsets(view);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onViewAttachedToWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);
                    System.out.println("onAttach");
                    ViewCompat.requestApplyInsets(v);
                }

                @Override
                public void onViewDetachedFromWindow(View v) { }
            });
        }
    }
}
