package com.example.art.multitenancy.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;


@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {
    @Override
    public String resolveCurrentTenantIdentifier() {
        return (TenantContext.getCurrentTenant() != null) ? TenantContext.getCurrentTenant() : "";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}




