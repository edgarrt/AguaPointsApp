package org.aguapoints.aguapointsapp;

/*
 * Created by edgartrujillo on 6/8/16.
 */

public class SlidingMenuItem {
    String title;
    int icon;

    public SlidingMenuItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
