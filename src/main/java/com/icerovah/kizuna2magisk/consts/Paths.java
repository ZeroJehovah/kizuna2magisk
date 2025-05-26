package com.icerovah.kizuna2magisk.consts;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.Map;

public class Paths {

    public static final File ROOT_DIR;
    public static final File WORKSPACE_DIR;
    public static final File MODULE_RESULT_DIR;

    public static final File ARCHIVE_DIR;
    public static final File ARCHIVE_ICON_ZIP_DIR;
    public static final File ARCHIVE_ICON_DIR;
    public static final File ARCHIVE_APK_INFO_FILE;
    public static final File ARCHIVE_OFFICIAL_APK_DIR;

    public static final File APKTOOL_FILE;
    public static final File APK_DIR;
    public static final File APPFILTER_FILE;
    public static final File APK_ICON_DIR;
    public static final File APK_INFO_FILE;

    public static final File MODULE_TEMPLATE_DIR;
    public static final File MODULE_DIR;
    public static final File MODULE_INFO_FILE;
    public static final File MODULE_ICON_ZIP_DIR;
    public static final File MODULE_ICON_ZIP_FILE;
    public static final File MODULE_FILE_DIR;
    public static final File MODULE_UPDATE_JSON_FILE;
    public static final File MODULE_UPDATE_MD_FILE;

    static {
        ROOT_DIR = getFile(System.getProperty("user.dir"));
        WORKSPACE_DIR = getFile(ROOT_DIR, "workspace");
        MODULE_RESULT_DIR = getFile(ROOT_DIR, "module");

        ARCHIVE_DIR = getFile(ROOT_DIR, "archive");
        ARCHIVE_ICON_ZIP_DIR = getFile(ARCHIVE_DIR, "icons");
        ARCHIVE_ICON_DIR = getFile(ARCHIVE_ICON_ZIP_DIR, "res", "drawable-xxhdpi");
        ARCHIVE_APK_INFO_FILE = getFile(ARCHIVE_DIR, "apk-info.json");
        ARCHIVE_OFFICIAL_APK_DIR = getFile(ARCHIVE_DIR, "official_apks");

        APKTOOL_FILE = getFile(ROOT_DIR, "apktool", "apktool");
        APK_DIR = getFile(WORKSPACE_DIR, "apk");
        APPFILTER_FILE = getFile(APK_DIR, "assets", "appfilter.xml");
        APK_ICON_DIR = getFile(APK_DIR, "res", "drawable-nodpi");
        APK_INFO_FILE = getFile(APK_DIR, "apktool.yml");

        MODULE_TEMPLATE_DIR = getFile(ARCHIVE_DIR, "module_template");
        MODULE_DIR = getFile(WORKSPACE_DIR, "module");
        MODULE_INFO_FILE = getFile(MODULE_DIR, "module.prop");
        MODULE_ICON_ZIP_DIR = getFile(MODULE_DIR, "icons_temp");
        MODULE_ICON_ZIP_FILE = getFile(MODULE_DIR, "icons");
        MODULE_FILE_DIR = getFile(MODULE_RESULT_DIR, "files");
        MODULE_UPDATE_JSON_FILE = getFile(MODULE_RESULT_DIR, "update.json");
        MODULE_UPDATE_MD_FILE = getFile(MODULE_RESULT_DIR, "update.md");
    }

    public static String getApkFilePath(String apkName) {
        return getFile(ARCHIVE_OFFICIAL_APK_DIR, apkName).getAbsolutePath();
    }

    public static File getApkIconFile(String iconName) {
        return getFile(APK_ICON_DIR, StrUtil.addSuffixIfNot(iconName, ".png"));
    }

    public static File getArchiveIconFile(String appName) {
        return getFile(ARCHIVE_ICON_DIR, StrUtil.addSuffixIfNot(appName, ".png"));
    }

    public static File getModuleFile(Map<?, ?> apkVersionInfo) {
        String moduleFileName = getModuleFileName(apkVersionInfo);
        return getFile(MODULE_FILE_DIR, moduleFileName);
    }

    public static String getModuleFileName(Map<?, ?> apkVersionInfo) {
        return StrUtil.format("HyperOS-KizunaAI-{versionName}({versionCode}).zip", apkVersionInfo);
    }

    private static File getFile(String parent, String... childs) {
        File file = new File(parent);
        return getFile(file, childs);
    }

    private static File getFile(File parent, String... childs) {
        File file = parent;
        for (String child : childs) {
            file = new File(file, child);
        }
        return file;
    }

}
