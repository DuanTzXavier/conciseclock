package com.tomduan.conciseclock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tomduan.library.CircleSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tomduan on 16-4-29.
 */
public class EditAcitvity extends AppCompatActivity {

    @Bind(R.id.circle_seek_bar)
    CircleSeekBar mSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
    }
}
