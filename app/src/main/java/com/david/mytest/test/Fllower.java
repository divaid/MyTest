package com.david.mytest.test;

import android.graphics.Path;

/**
 * Created by weixi on 2017/6/10.
 */

public class Fllower {
    private Path path;
    private float value;

    public void setPath(Path path) {
        this.path = path;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Path getPath() {
        return path;
    }

    public float getValue() {
        return value;
    }
}
