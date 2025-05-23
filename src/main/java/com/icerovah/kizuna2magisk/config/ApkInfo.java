package com.icerovah.kizuna2magisk.config;

import java.util.HashMap;
import java.util.Map;

public class ApkInfo extends HashMap<String, Object> {

    public Map<?, ?> getVersionInfo() {
        Object versionInfo = get("versionInfo");
        if (versionInfo instanceof Map) {
            return (Map<?, ?>) versionInfo;
        }
        return null;
    }

}
