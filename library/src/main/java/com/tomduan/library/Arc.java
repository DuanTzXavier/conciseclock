package com.tomduan.library;

/**
 * Created by tomduan on 16-4-20.
 */
public class Arc {

    private float startArc;
    private float sweepArc;
    private boolean istouched;

    public Arc(float startArc, float sweepArc) {
        this.startArc = startArc;
        this.sweepArc = sweepArc;
        this.istouched = false;
    }


    public float getStartArc() {
        return startArc;
    }

    public float getSweepArc() {
        return sweepArc;
    }

    public boolean istouched() {
        return istouched;
    }

    public void setIstouched(boolean is){
        this.istouched = is;
    }
}
