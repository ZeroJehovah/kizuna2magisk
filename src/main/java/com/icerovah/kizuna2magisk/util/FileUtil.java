package com.icerovah.kizuna2magisk.util;

import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.icerovah.kizuna2magisk.consts.Paths.ARCHIVE_APK_INFO_FILE;

public class FileUtil extends cn.hutool.core.io.FileUtil {

    private static Map<?, ?> APK_VERSION_INFO;

    public static Map<?, ?> getApkVersionInfo() {
        if (APK_VERSION_INFO == null) {
            String apkVersionInfoJson = readString(ARCHIVE_APK_INFO_FILE, StandardCharsets.UTF_8);
            APK_VERSION_INFO = JSONUtil.parseObj(apkVersionInfoJson);
        }

        return APK_VERSION_INFO;
    }

}
