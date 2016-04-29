package com.tomduan.conciseclock;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.tomduan.library.CircleSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tomduan on 16-4-29.
 */
public class UsageActivity extends AppCompatActivity implements CircleSeekBar.ClickListener {

    @Bind(R.id.circle_seek_bar)
    CircleSeekBar mSeekBar;
    @Bind(R.id.text_v)
    TextView log;
    @Bind(R.id.message)
    EditText leaveMessage;

    private boolean isFirst = true;
    private String startTime = "";
    private List<Item> messages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);
        ButterKnife.bind(this);

        initSeekBar();
    }

    private void initSeekBar() {
        mSeekBar.setClickListener(this);
        mSeekBar.setCircleColor(Color.BLUE);
        mSeekBar.setPointerColor(Color.YELLOW);
        mSeekBar.setInvaildColor(Color.GREEN);
        mSeekBar.setSelectColor(Color.RED);
        mSeekBar.setTextColor(Color.BLACK);
        mSeekBar.setTextSize(50);
        mSeekBar.setCircleWidth(32);
        mSeekBar.setRangeWidth(18);
        mSeekBar.setStyle(CircleSeekBar.CLOCK);
        mSeekBar.build();
    }

    @OnClick(R.id.yes)
    public void restart(){
        mSeekBar.setIsSetStart(false);
        if(isFirst){
            mSeekBar.initInvaildStartAngle();
            isFirst = false;
            startTime = mSeekBar.getCurrent();
        }else {
            messages.add(new Item(startTime,
                    mSeekBar.getCurrent(),
                    leaveMessage.getText().toString()));
            startTime = mSeekBar.getCurrent();
        }
        mSeekBar.reSeek();
        leaveMessage.setText("");
        log.append(mSeekBar.getCurrent() + "\n");
    }

    @Override
    public void clicked(int position) {
        String text = messages.get(position).getStartTime() +
                " ~ " +
                messages.get(position).getEndTime() +
                " : " +
                messages.get(position).getMessage();

        mSeekBar.setCenterText(text);
    }
}
