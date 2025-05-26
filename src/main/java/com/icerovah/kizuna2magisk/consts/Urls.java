package com.icerovah.kizuna2magisk.consts;

import java.util.Map;

public class Urls {

    public static final String BASE_PATH = "https://raw.githubusercontent.com/ZeroJehovah/kizuna2magisk/refs/heads/master/module/";
    public static final String MODULE_FILE_PATH = BASE_PATH + "files/";
    public static final String MODULE_UPDATE_MD = BASE_PATH + "update.md";

    public static String getModule(Map<?,?> apkVersionInfo) {
        String moduleFileName = Paths.getModuleFileName(apkVersionInfo);
        return MODULE_FILE_PATH + moduleFileName;
    }

}
