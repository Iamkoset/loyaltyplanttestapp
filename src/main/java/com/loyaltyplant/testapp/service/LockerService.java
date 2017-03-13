package com.loyaltyplant.testapp.service;

import com.loyaltyplant.testapp.service.sync.Locker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope("singleton")
public class LockerService {
    private ConcurrentHashMap<Long, Locker> registry = new ConcurrentHashMap<>(0);

    public Locker getLockerFor(long numberToLock) {
        Locker locker = registry.get(numberToLock);
        if (locker == null) {
            locker = new Locker();
            Locker anotherLocker = registry.putIfAbsent(numberToLock, new Locker());
            return (anotherLocker == null) ? locker : anotherLocker;
        }
        return locker;
    }
}
