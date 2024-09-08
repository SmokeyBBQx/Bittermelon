package net.smokeybbq.bittermelon.character;

public class SpeechProperties {
    private int rangeAmplifier = 0;
    private boolean mute = false;

    SpeechProperties() {

    }

    public int getRangeAmplifier() {
        return rangeAmplifier;
    }

    public void setRangeAmplifier(int rangeAmplifier) {
        this.rangeAmplifier = rangeAmplifier;
    }

    public void modifyRangeAmplifier(int delta) {
        this.rangeAmplifier += rangeAmplifier;
    }

    public void resetRangeAmplifier() {
        rangeAmplifier = 0;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
