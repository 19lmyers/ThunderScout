package dev.chara.thunderscout.ui.utils;

import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public final class BindingUtils {

    @BindingAdapter("android:text")
    public static void setText(EditText view, int value) {
        view.setText(String.valueOf(value));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    @BindingAdapter("android:text")
    public static void setText(TextView view, Instant date) {
        if (date != null) {
            view.setText(SimpleDateFormat.getDateTimeInstance().format(Date.from(date)));
        }
    }
}