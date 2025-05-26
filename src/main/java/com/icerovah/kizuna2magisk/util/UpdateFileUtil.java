package com.icerovah.kizuna2magisk.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.icerovah.kizuna2magisk.consts.Paths;
import com.icerovah.kizuna2magisk.consts.Urls;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateFileUtil {

    private static final String UPDATE_MD_TEMPLATE = "同步更新官方apk-{versionName}图标";

    public static void saveUpdateJson() {
        Map<String, Object> content = new LinkedHashMap<>();
        Map<?, ?> apkVersionInfo = FileUtil.getApkVersionInfo();
        content.put("version", apkVersionInfo.get("versionName"));
        content.put("versionCode", apkVersionInfo.get("versionCode"));
        content.put("zipUrl", Urls.getModule(apkVersionInfo));
        content.put("changelog", Urls.MODULE_UPDATE_MD);
        FileUtil.writeString(JSONUtil.toJsonPrettyStr(content), Paths.MODULE_UPDATE_JSON_FILE, StandardCharsets.UTF_8);
    }

    public static void saveUpdateMd() {
        Map<?, ?> apkVersionInfo = FileUtil.getApkVersionInfo();
        String content = StrUtil.format(UPDATE_MD_TEMPLATE, apkVersionInfo);
        FileUtil.writeString(content, Paths.MODULE_UPDATE_MD_FILE, StandardCharsets.UTF_8);
    }

}
