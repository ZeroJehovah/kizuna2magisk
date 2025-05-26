package com.icerovah.kizuna2magisk.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.icerovah.kizuna2magisk.consts.Paths;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.icerovah.kizuna2magisk.consts.Paths.*;

public class ModuleUtil {

    public static void clearTempDir() {
        FileUtil.del(Paths.MODULE_DIR);
    }

    public static void copyResources() {
        FileUtil.copyContent(MODULE_TEMPLATE_DIR, MODULE_DIR, false);
        FileUtil.copyContent(ARCHIVE_ICON_ZIP_DIR, MODULE_ICON_ZIP_DIR, false);
    }

    public static void packageIcons() {
        System.out.println("图标打包: " + MODULE_ICON_ZIP_DIR + " -> " + MODULE_ICON_ZIP_FILE);
        ZipUtil.zip(MODULE_ICON_ZIP_FILE, false, MODULE_ICON_ZIP_DIR);
        FileUtil.del(MODULE_ICON_ZIP_DIR);
    }

    public static void updateVersion() {
        String moduleInfo = FileUtil.readString(MODULE_INFO_FILE, StandardCharsets.UTF_8);
        Map<?, ?> apkVersionInfo = FileUtil.getApkVersionInfo();
        moduleInfo = StrUtil.format(moduleInfo, apkVersionInfo);
        FileUtil.writeString(moduleInfo, MODULE_INFO_FILE, StandardCharsets.UTF_8);
    }

    public static void packageModule() {
        Map<?, ?> apkVersionInfo = FileUtil.getApkVersionInfo();
        File moduleFile = getModuleFile(apkVersionInfo);
        System.out.println("模块打包: " + MODULE_DIR + " -> " + moduleFile);
        ZipUtil.zip(moduleFile, false, MODULE_DIR);
    }

    public static void updateLog() {
        UpdateFileUtil.saveUpdateJson();
        UpdateFileUtil.saveUpdateMd();
    }

}
