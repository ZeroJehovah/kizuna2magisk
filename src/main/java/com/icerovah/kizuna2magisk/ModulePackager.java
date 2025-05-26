package com.icerovah.kizuna2magisk;

import com.icerovah.kizuna2magisk.util.ModuleUtil;

public class ModulePackager {

    public static void main(String[] args) throws Exception {
        // 1. 清理临时目录
        ModuleUtil.clearTempDir();

        // 2. 复制资源文件
        ModuleUtil.copyResources();

        // 3. 打包图标目录
        ModuleUtil.packageIcons();

        // 4. 更新模块版本号
        ModuleUtil.updateVersion();

        // 5. 打包模块
        ModuleUtil.packageModule();

        // 6. 更新模块说明文件
        ModuleUtil.updateLog();
    }

}
