package com.meow.ops.androidinfo.root;

import java.io.File;

/**
 * Created on 6/13/16.
 * http://stackoverflow.com/a/23952951/169691
 */
public class RootUtil3 {

    public static boolean isRooted() {
        return findBinary("su");
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        String[] places = {"/sbin/", "/system/bin/", "/system/xbin/",
                "/data/local/xbin/", "/data/local/bin/",
                "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
        for (String where : places) {
            if (new File(where + binaryName).exists()) {
                found = true;

                break;
            }
        }
        return found;
    }
}
