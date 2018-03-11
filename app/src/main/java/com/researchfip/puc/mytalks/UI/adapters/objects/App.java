package com.researchfip.puc.mytalks.UI.adapters.objects;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by joaocastro on 01/11/17.
 */

public class App {

    private String name;
    private String usage;
    private Drawable icon;

    public App(String name, String usage, Drawable icon) {
        this.name = name;
        this.usage = usage;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
