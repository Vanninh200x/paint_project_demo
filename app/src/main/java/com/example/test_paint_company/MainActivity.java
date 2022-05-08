package com.example.test_paint_company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.google.android.material.slider.RangeSlider;

public class MainActivity extends AppCompatActivity {
    private DrawView paint;
    private ImageButton save, color, stroke, undo;
    private RangeSlider rangeSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paint = findViewById(R.id.draw_view);
        stroke = findViewById(R.id.btn_stroke);
        rangeSlider = findViewById(R.id.rangebar);


//        A view tree observer is used to register listeners that can be notified of global changes
//        in the view tree.Such global events include, but are not limited to, layout of the
//        whole tree, beginning of the drawing pass, touch mode change....A ViewTreeObserver
//        should never be instantiated by applications as it is provided by the views hierarchy.
//        Refer to
//        Tạo đường kẻ ko giới hạn
        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);
            }
        });


        //       Tạo độ đậm nhạt cho Stroke
        stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rangeSlider.getVisibility() == View.VISIBLE)
                    rangeSlider.setVisibility(View.GONE);
                else
                    rangeSlider.setVisibility(View.VISIBLE);
            }
        });
        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                paint.setStrokeWidth(
                        (int) value
                );
            }
        });

    }
}