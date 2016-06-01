package com.tomduan.conciseclock;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tomduan.library.Arc;
import com.tomduan.library.CircleSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tomduan on 16-4-29.
 */
public class EditAcitvity extends AppCompatActivity implements CircleSeekBar.ClickListener {

    @Bind(R.id.circle_seek_bar)
    CircleSeekBar mSeekBar;

    private List<Arc> arcs = new ArrayList<>();

    private boolean first = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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
        mSeekBar.setTextSize(80);
        mSeekBar.setCircleWidth(7);
        mSeekBar.setRangeWidth(18);
        mSeekBar.setStyle(CircleSeekBar.CLOCK);
        mSeekBar.setmPointerPosition(CircleSeekBar.OUTSIDE);
        mSeekBar.setmPointerStyle(CircleSeekBar.CIRCLE);
        mSeekBar.build();
    }

    @OnClick(R.id.yes)
    public void yes(){
        mSeekBar.setIsSetStart(false);
        if (first){
            mSeekBar.initInvaildStartAngle();
            first = false;
        }
        mSeekBar.reSeek();
    }

    @OnClick(R.id.re)
    public void re(){
//        arcs.clear();
//        arcs.addAll(mSeekBar.getArcs());
//        arcs.remove(arcs.size()-2);
//        mSeekBar.clear();
    }

    @OnClick(R.id.set)
    public void set(){
//        mSeekBar.setArcs(arcs);
    }

    @Override
    public void clicked(int position) {
        if (mSeekBar.isCircle()){
//            mSeekBar.deleteArc(position);
            mSeekBar.setIsCanSet(false);
        }
    }
}
