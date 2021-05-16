package dev.chara.thunderscout.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.ViewMaterialCounterBinding;

@InverseBindingMethods({
        @InverseBindingMethod(type = MaterialCounterView.class, attribute = "value")
})
public final class MaterialCounterView extends FrameLayout implements View.OnClickListener {

    private ViewMaterialCounterBinding binding;

    private OnValueChangeListener listener;

    private double value;

    public MaterialCounterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = ViewMaterialCounterBinding.inflate(inflater, this, true);

        binding.counterMinus.setOnClickListener(this);
        binding.counterPlus.setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MaterialCounterView, 0, 0);

        value = a.getInteger(R.styleable.MaterialCounterView_value, 0);

        binding.setValue(value);
        binding.executePendingBindings();

        a.recycle();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.counter_minus) {
            value--;
        } else if (v.getId() == R.id.counter_plus) {
            value++;
        }

        binding.setValue(value);
        binding.executePendingBindings();

        if (listener != null) {
            listener.onValueChange(this, value);
        }
    }

    public OnValueChangeListener geValueChangeListener() {
        return listener;
    }

    public void setValueChangeListener(OnValueChangeListener listener) {
        this.listener = listener;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double v) {
        value = v;

        binding.setValue(value);
        binding.executePendingBindings();

        if (listener != null) {
            listener.onValueChange(this, value);
        }
    }

    @BindingAdapter("valueAttrChanged")
    public static void setValueListener(MaterialCounterView view, final InverseBindingListener valueChange) {
        if (valueChange == null) {
            view.setValueChangeListener(null);
        } else {
            view.setValueChangeListener((view1, color) -> {
                System.out.println("onValueChange");
                valueChange.onChange();
            });
        }
    }

    @BindingAdapter(value = {"onValueChange", "valueAttrChanged"}, requireAll = false)
    public static void setValueListener(MaterialCounterView view, final OnValueChangeListener listener, final InverseBindingListener colorChange) {
        if (colorChange == null) {
            view.setValueChangeListener(listener);
        } else {
            view.setValueChangeListener((view1, value) -> {
                if (listener != null) {
                    listener.onValueChange(view1, value);
                }
                colorChange.onChange();
            });
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(MaterialCounterView view, double value);
    }
}
