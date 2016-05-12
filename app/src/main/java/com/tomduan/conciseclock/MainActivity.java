package com.tomduan.conciseclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tomduan.library.CircleSeekBar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private boolean isFirst = true;
    CircleSeekBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        bar = (CircleSeekBar) findViewById(R.id.circle_seek_bar);
//        final Button yes = (Button) findViewById(R.id.yes);
//        final TextView textView = (TextView) findViewById(R.id.text);
//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bar.setIsSetStart(false);
//                if(isFirst){
//                    bar.initInvaildStartAngle();
//                    isFirst = false;
//                }
//                textView.append("时间："+bar.getCurrent()+"\n");
//                bar.reSeek();
//                if (bar.isCircle()){
//                    yes.setEnabled(false);
//                }
//            }
//        });
//
//
//        bar.setCircleColor(Color.BLUE);
//        bar.setPointerColor(Color.YELLOW);
//        bar.setInvaildColor(Color.GREEN);
//        bar.setSelectColor(Color.RED);
//        bar.setTextColor(Color.BLACK);
//        bar.setTextSize(50);
//        bar.setCircleWidth(32);
//        bar.setRangeWidth(18);
//        bar.setStyle(CircleSeekBar.CLOCK);
//        bar.build();
    }

    @OnClick(R.id.usage)
    public void startUsage(){
        startActivity(new Intent(getBaseContext(), UsageActivity.class));
    }

    @OnClick(R.id.eidt)
    public void startEidt(){
        startActivity(new Intent(getBaseContext(), EditAcitvity.class));
    }
}
