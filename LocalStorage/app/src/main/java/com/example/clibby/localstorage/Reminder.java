package com.example.clibby.localstorage;

public class Reminder {
    private boolean mChecked;
    private String mTitle;
    private String mDescription;

    public Reminder(String mTitle, String mDescription,boolean mChecked) {
        this.mChecked = mChecked;
        this.mTitle = mTitle;
        this.mDescription = mDescription;

    }

    public boolean ismChecked() {return mChecked;}
    public String getmTitle() {return mTitle;}
    public String getmDescription() {return mDescription;}
    public void setmChecked(boolean mChecked) {this.mChecked = mChecked;}
    public void setmTitle(String mTitle) {this.mTitle = mTitle;}
    public void setmDescription(String mDescription) {this.mDescription = mDescription;}
}
