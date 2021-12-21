package com.github.lastsunday.moon.data.component;

import com.github.lastsunday.moon.config.AppConfig;
import com.github.lastsunday.moon.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataKeyBuilder {

    @Autowired
    protected AppConfig appConfig;

    public String getKeyWithPrefix(String key) {
        String prefix = appConfig.getDataKey().getPrefix();
        if (StringUtils.isEmpty(prefix)) {
            return key;
        } else {
            return prefix + key;
        }
    }
}
