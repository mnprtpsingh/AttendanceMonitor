package com.example.pratap.attendancemonitor;

/**
 * Created by pratap on 1/4/17.
 */

public class Subject {
    private int mP;
    private int mA;
    private String mSubject;
    //empty constructor
    public Subject(){}

    //constructor
    public Subject(int P, int A,String Sub)
    {
        this.mP=P;
        this.mA=A;
        this.mSubject=Sub;
    }

    public int getmP(){
        return this.mP;
    }

    public void SetmP(int P)
    {
        this.mP=P;
    }
    public int getmA(){
        return this.mA;
    }

    public void SetmA(int A)
    {
        this.mA=A;
    }
    public String getmSubject(){
        return this.mSubject;
    }

    public void SetSubject(String Sub)
    {
        this.mSubject=Sub;
    }



}
