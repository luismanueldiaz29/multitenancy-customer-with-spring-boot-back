package com.example.art.multitenancy.multitenant;

import com.example.art.multitenancy.exceptions.TenantException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.Stoppable;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider<String>, Stoppable {

    public static final String NO_TENANT_DATA_SOURCES_HAVE_BEEN_CONFIGURED = "No tenant DataSources have been configured.";
    public static final String NO_DATA_SOURCE_FOUND_FOR_TENANT = "No DataSource found for tenant: ";
    public static final String CLOSED_CONNECTION = "closed connection";
    private final Map<String, DataSource> dataSource = new HashMap<>();

    public MultiTenantConnectionProviderImpl(TenantDataSourceConfig config) {
        config.getDatasource().forEach((tenantId, props) -> {
            log.info(">>> Registered tenant: {}", tenantId);
            this.dataSource.put(tenantId,
                DataSourceBuilder.create()
                    .url(props.getUrl())
                    .username(props.getUsername())
                    .password(props.getPassword())
                    .driverClassName(props.getDriverClassName())
                    .build()
            );
        });
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        if (dataSource.isEmpty()) {
            throw new TenantException(NO_TENANT_DATA_SOURCES_HAVE_BEEN_CONFIGURED);
        }
        return dataSource.values().iterator().next().getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        DataSource source = this.dataSource.get(tenantIdentifier);
        if (source == null) {
            throw new TenantException(NO_DATA_SOURCE_FOUND_FOR_TENANT + tenantIdentifier);
        }
        return source.getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return unwrapType.isAssignableFrom(getClass());
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (unwrapType.isAssignableFrom(getClass())) {
            return (T) this;
        }
        return null;
    }

    @Override
    public void stop() {
        log.info(CLOSED_CONNECTION);
        // opcional: cerrar conexiones si querés
    }
}


