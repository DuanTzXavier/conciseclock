package com.tomduan.conciseclock;

import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomduan.library.CircleSeekBar;
import com.tomduan.library.RangeSeekBar;

public class MainActivity extends AppCompatActivity {

    private boolean isFirst = true;
    CircleSeekBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final TextView mMinText = (TextView) findViewById(R.id.min);
//        final TextView mMaxText = (TextView) findViewById(R.id.max);
//
//        // create RangeSeekBar as Integer range between 20 and 75
//        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 24, this);
//        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
//            @Override
//            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
//                                                    Integer minValue, Integer maxValue) {
//                // handle changed range values
//                mMinText.setText(String.valueOf(minValue));
//                mMaxText.setText(String.valueOf(maxValue));
//            }
//        });
//
//        // add RangeSeekBar to pre-defined layout
//        LinearLayout layout = (LinearLayout) findViewById(R.id.view);
//        layout.addView(seekBar);


        bar = (CircleSeekBar) findViewById(R.id.circle_seek_bar);
        final Button yes = (Button) findViewById(R.id.yes);
        final TextView textView = (TextView) findViewById(R.id.text);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setIsSetStart(false);
                if(isFirst){
                    bar.initInvaildStartAngle();
                    isFirst = false;
                }
                textView.append("时间："+bar.getCurrent()+"\n");
                bar.reSeek();
                if (bar.isCircle()){
                    yes.setEnabled(false);
                }
            }
        });


        bar.setCircleColor(Color.BLUE);
        bar.setPointerColor(Color.YELLOW);
        bar.setInvaildColor(Color.GREEN);
        bar.setSelectColor(Color.RED);
        bar.setTextColor(Color.BLACK);
        bar.setTextSize(80);
        bar.setCircleWidth(32);
        bar.setRangeWidth(18);
        bar.setStyle(CircleSeekBar.CLOCK);
        bar.build();
    }
}
