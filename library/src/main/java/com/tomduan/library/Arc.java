package com.tomduan.library;

/**
 * Created by tomduan on 16-4-20.
 */
public class Arc {

    private float startArc;
    private float sweepArc;
    private boolean istouched;
    private int color;

    public Arc(float startArc, float sweepArc, int color) {
        this.startArc = startArc;
        this.sweepArc = sweepArc;
        this.istouched = false;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setStartArc(float startArc){
        this.startArc = startArc;
    }

    public void setSweepArc(float sweepArc){
        this.sweepArc = sweepArc;
    }
}
