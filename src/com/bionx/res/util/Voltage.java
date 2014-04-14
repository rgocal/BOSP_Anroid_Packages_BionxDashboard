package com.bionx.res.util;

public class Voltage {
    private String mFreq;
    private String mCurrentMv;
    private String mSavedMv;

    public void setFreq(final String freq) {
        this.mFreq = freq;
    }

    public String getFreq() {
        return mFreq;
    }

    public void setCurrentMV(final String currentMv) {
        this.mCurrentMv = currentMv;
    }

    public String getCurrentMv() {
        return mCurrentMv;
    }

    public void setSavedMV(final String savedMv) {
        this.mSavedMv = savedMv;
    }

    public String getSavedMV() {
        return mSavedMv;
    }
}
