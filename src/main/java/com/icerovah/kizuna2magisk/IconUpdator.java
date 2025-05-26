package com.icerovah.kizuna2magisk;

import com.icerovah.kizuna2magisk.util.ApkUtil;

public class IconUpdator {

    private static final String APK_NAME = "com.aijiangicon.cc_v1.2.7(20240519)-release.apk";

    public static void main(String[] args) {
        // 1. 清理临时目录
        ApkUtil.clearTempDir();

        // 2. 反编译
        ApkUtil.decompile(APK_NAME);

        // 3. 获取图标映射
        ApkUtil.getIconMapping();

        // 4. 复制图标
        ApkUtil.copyIcons();

        // 5. 保存图标信息
        ApkUtil.saveApkInfo();
    }

}
