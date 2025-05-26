package com.icerovah.kizuna2magisk.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.icerovah.kizuna2magisk.config.ApkInfo;
import com.icerovah.kizuna2magisk.consts.Paths;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.icerovah.kizuna2magisk.consts.Paths.*;

public class ApkUtil {

    private static final Map<String, String> iconMapping = new HashMap<>();

    public static void clearTempDir() {
        FileUtil.del(Paths.APK_DIR);
    }

    @SneakyThrows
    public static void decompile(String apkName) {
        String apktoolFilePath = APKTOOL_FILE.getAbsolutePath();
        String apkFilePath = getApkFilePath(apkName);
        String apkDirPath = APK_DIR.getAbsolutePath();
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", apktoolFilePath, "d", apkFilePath, "-o", apkDirPath);
        System.out.println("apktool命令: " + String.join(" ", processBuilder.command()));

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        int exitCode = process.waitFor();
        System.out.println("apktool命令执行完成: exit code = " + exitCode);
    }

    public static void getIconMapping() {
        Document document = XmlUtil.readXML(APPFILTER_FILE);
        NodeList itemList = document.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Node node = itemList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element item = (Element) node;
                String drawable = item.getAttribute("drawable");
                String component = item.getAttribute("component");
                if (StrUtil.hasBlank(drawable, component)) {
                    continue;
                }

                drawable = StrUtil.trim(drawable);
                component = StrUtil.removePrefix(component, "ComponentInfo{");
                component = StrUtil.removeSuffix(component, "}");
                component = component.split("/")[0];

                System.out.println("drawable: " + drawable + ", component: " + component);
                iconMapping.put(component, drawable);
            }
        }
    }

    public static void copyIcons() {
        for (String appName : iconMapping.keySet()) {
            String iconName = iconMapping.get(appName);
            File apkIconFile = getApkIconFile(iconName);
            if (!FileUtil.exist(apkIconFile)) {
                System.out.println("图标文件不存在: " + apkIconFile);
                continue;
            }
            File archiveIconFile = getArchiveIconFile(appName);
            System.out.println("图标文件复制: " + apkIconFile + " -> " + archiveIconFile);
            FileUtil.copy(apkIconFile, archiveIconFile, true);
        }
    }

    public static void saveApkInfo() {
        ApkInfo apkInfo = YamlUtil.loadByPath(APK_INFO_FILE.getAbsolutePath(), ApkInfo.class);
        String apkVersionInfoJson = JSONUtil.toJsonPrettyStr(apkInfo.getVersionInfo());
        FileUtil.writeString(apkVersionInfoJson, ARCHIVE_APK_INFO_FILE, StandardCharsets.UTF_8);
    }

}
