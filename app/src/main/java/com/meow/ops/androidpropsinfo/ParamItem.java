package com.meow.ops.androidpropsinfo;

import android.text.TextUtils;


/**
 * Created on 6/10/16.
 */
public class ParamItem implements Item {
    public String key;
    public String value;

    public ParamItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ParamItem(String key, String values[]) {
        this.key = key;
        this.value = TextUtils.join("\n", values);
    }

    public ParamItem(String key, int value) {
        this.key = key;
        this.value = Integer.toString(value);
    }

    public ParamItem(String key, long value) {
        this.key = key;
        this.value = Long.toString(value);
    }

    public ParamItem(String key, boolean value) {
        this.key = key;
        this.value = Boolean.toString(value);
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
