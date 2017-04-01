package com.example.pratap.attendancemonitor;

/**
 * Created by pratap on 1/4/17.
 */

public class Subject {

    private String mName;
    private int mPresent = 0;
    private int mAbsent = 0;
    private int mMonday;
    private int mTuesday;
    private int mWednesday;
    private int mThursday;
    private int mFriday;

    public Subject(String Name, int monday, int tuesday, int wednesday, int thursday, int friday) {
        mName = Name;
        mMonday = monday;
        mTuesday = tuesday;
        mWednesday = wednesday;
        mThursday = thursday;
        mFriday = friday;
    }

    public String getName() {
        return mName;
    }

    public int getMonday() { return mMonday; }

    public int getTuesday() { return mTuesday; }

    public int getWednesday() { return mWednesday; }

    public int getThursday() { return mThursday; }

    public int getFriday() { return mFriday; }

    public int getPresent() { return mPresent; }

    public int getAbsent() { return mAbsent; }
}
