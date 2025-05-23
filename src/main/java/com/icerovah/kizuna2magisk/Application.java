package com.icerovah.kizuna2magisk;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.icerovah.kizuna2magisk.config.ApkInfo;
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

public class Application {

    public static void main(String[] args) throws Exception {
        String jarPath = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String jarDirPath = new File(jarPath).getParent();
        jarDirPath = URLDecoder.decode(jarDirPath, StandardCharsets.UTF_8);

        String tempDirPath = concatPath(jarDirPath, "temp");
        System.out.println("临时文件夹: " + tempDirPath);
        FileUtil.del(tempDirPath);

        // 构建命令和参数
        String apktoolPath = concatPath(jarDirPath, "apktool", "apktool");
        String apkPath = concatPath(jarDirPath, "kizuna.apk");
        String apkUnzipPath = concatPath(tempDirPath, "apk");
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", apktoolPath, "d", apkPath, "-o", apkUnzipPath);
        String command = String.join(" ", processBuilder.command());
        System.out.println("apktool命令: " + command);

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

        Map<String, String> iconMap = new HashMap<>();
        String appfilterPath = concatPath(apkUnzipPath, "assets", "appfilter.xml");
        Document document = XmlUtil.readXML(appfilterPath);
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
                iconMap.put(component, drawable);
            }
        }

        String moduleTemplatePath = concatPath(jarDirPath, "module_template");
        FileUtil.copy(moduleTemplatePath, tempDirPath, false);
        String moduleTempPath = concatPath(tempDirPath, "module_template");

        String moduleIconTempDirPath = concatPath(moduleTempPath, "icons_temp", "res", "drawable-xxhdpi");
        String apkIconDirPath = concatPath(apkUnzipPath, "res", "drawable-nodpi");
        for (String appName : iconMap.keySet()) {
            String iconName = iconMap.get(appName);
            String apkIconPath = concatPath(apkIconDirPath, StrUtil.addSuffixIfNot(iconName, ".png"));
            if (!FileUtil.exist(apkIconPath)) {
                System.out.println("图标文件不存在: " + apkIconPath);
                continue;
            }
            String moduleIconPath = concatPath(moduleIconTempDirPath, appName, "0.png");
            System.out.println("图标文件复制: " + apkIconPath + " -> " + moduleIconPath);
            FileUtil.copy(apkIconPath, moduleIconPath, false);
        }

        String moduleIconZipPath = concatPath(moduleTempPath, "icons_temp");
        String moduleIconZipFilePath = concatPath(moduleTempPath, "icons");
        System.out.println("图标打包: " + moduleIconZipPath + " -> " + moduleIconZipFilePath);
        ZipUtil.zip(moduleIconZipPath, moduleIconZipFilePath, false);
        FileUtil.del(moduleIconZipPath);

        String apkInfoPath = concatPath(apkUnzipPath, "apktool.yml");
        ApkInfo apkInfo = YamlUtil.loadByPath(apkInfoPath, ApkInfo.class);
        String moduleInfoPath = concatPath(moduleTempPath, "module.prop");
        String moduleInfo = FileUtil.readString(moduleInfoPath, StandardCharsets.UTF_8);
        moduleInfo = StrUtil.format(moduleInfo, apkInfo.getVersionInfo());
        FileUtil.writeString(moduleInfo, moduleInfoPath, StandardCharsets.UTF_8);

        String moduleZipFileName = StrUtil.format("hyperos_kizuna-{versionName}({versionCode}).zip", apkInfo.getVersionInfo());

        String moduleZipPath = concatPath(jarDirPath, moduleZipFileName);
        System.out.println("模块打包: " + moduleTempPath + " -> " + moduleZipPath);
        ZipUtil.zip(moduleTempPath, moduleZipPath, false);

        FileUtil.del(tempDirPath);
    }
    
    public static String concatPath(String... paths) {
        for (int i = 0; i < paths.length; i++) {
            paths[i] = paths[i].replace("/", "\\");
            paths[i] = StrUtil.removeSuffix(paths[i], "\\");
        }
        return String.join("\\", paths);
    }

}
