package com.tomduan.conciseclock;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomduan.library.CircleSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tomduan on 16-4-29.
 */
public class UsageActivity extends AppCompatActivity implements CircleSeekBar.ClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.circle_seek_bar)
    CircleSeekBar mSeekBar;
    @Bind(R.id.text_v)
    TextView log;
    @Bind(R.id.message)
    EditText leaveMessage;
    @Bind(R.id.isRest)
    CheckBox mIsRest;
    @Bind(R.id.yes)
    Button set;

    private boolean isFirst = true;
    private String startTime = "";
    private List<Item> messages = new ArrayList<>();

    private boolean isAddMessage = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);
        ButterKnife.bind(this);
        mIsRest.setOnCheckedChangeListener(this);
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

        Toast.makeText(this, "Drag the slider to seek the start time.", Toast.LENGTH_SHORT).show();
        leaveMessage.setEnabled(false);
    }

    @OnClick(R.id.yes)
    public void restart(){
        mSeekBar.setIsSetStart(false);
        if (isAddMessage){
            if (!leaveMessage.getText().toString().equals("")){
                messages.add(new Item(startTime,
                        mSeekBar.getCurrent(),
                        leaveMessage.getText().toString()));
                startTime = mSeekBar.getCurrent();

                isAddMessage = false;
            }else {
                Toast.makeText(this, "leave a message", Toast.LENGTH_SHORT).show();
            }
        }else {
            if (isFirst){
                mSeekBar.initInvaildStartAngle();
                isFirst = false;
                startTime = mSeekBar.getCurrent();
            }else {
                isAddMessage = true;
            }
        }


        mSeekBar.reSeek();
        leaveMessage.setText("");
        log.append(mSeekBar.getCurrent() + "\n");

        leaveMessage.setEnabled(isAddMessage);
        mSeekBar.setIsCanSet(!isAddMessage);
        mIsRest.setEnabled(!isAddMessage);


        if (mSeekBar.isCircle() && !isAddMessage){
            Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
            set.setEnabled(false);
        }else {
            if (isAddMessage){
                Toast.makeText(this, "add a message", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Drag the slider to seek the end time in this range.", Toast.LENGTH_SHORT).show();
            }
        }


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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mSeekBar.isRestWhole(isChecked);
    }
}
