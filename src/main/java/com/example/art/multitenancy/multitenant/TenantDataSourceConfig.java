package com.example.art.multitenancy.multitenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "spring")
public class TenantDataSourceConfig {
    private Map<String, DataSourceProperties> datasource;
    @Data
    public static class DataSourceProperties {
        private String username;
        private String url;
        private String password;
        private String driverClassName;
    }
}

