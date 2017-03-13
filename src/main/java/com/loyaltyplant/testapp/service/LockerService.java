package com.loyaltyplant.testapp.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope("singleton")
public class LockerService {
    private ConcurrentHashMap<Long, Object> registry = new ConcurrentHashMap<>(0);

    public ConcurrentHashMap<Long, Object> getRegistry() {
        return registry;
    }

    public void setRegistry(ConcurrentHashMap<Long, Object> registry) {
        this.registry = registry;
    }
}
