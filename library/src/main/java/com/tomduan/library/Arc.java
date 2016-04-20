package com.tomduan.library;

/**
 * Created by tomduan on 16-4-20.
 */
public class Arc {

    private float startArc;
    private float sweepArc;

    public Arc(float startArc, float sweepArc) {
        this.startArc = startArc;
        this.sweepArc = sweepArc;
    }


    public float getStartArc() {
        return startArc;
    }

    public float getSweepArc() {
        return sweepArc;
    }
}
