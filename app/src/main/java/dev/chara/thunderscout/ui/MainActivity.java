package dev.chara.thunderscout.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.ActivityMainBinding;

public final class MainActivity extends AppCompatActivity {

    private OnBackPressedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThunderScout);
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragment, (v, insets) -> {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
            params.topMargin = insets.getSystemWindowInsetTop();
            params.leftMargin = insets.getSystemWindowInsetLeft();
            params.rightMargin = insets.getSystemWindowInsetRight();
            return insets;
        });

        setContentView(binding.getRoot());
    }

    @Override
    public void onBackPressed() {
        if (listener == null || listener.onBackPressed()) {
            // Allow activity to propagate back button press
            super.onBackPressed();
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.listener = listener;
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }
}
