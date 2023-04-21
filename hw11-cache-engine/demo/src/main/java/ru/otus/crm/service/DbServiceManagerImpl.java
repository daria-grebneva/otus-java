package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Manager> managerCache = new MyCache<>();

    private final HwListener<String, Manager> managerListener = new HwListener<String, Manager>() {
        @Override
        public void notify(String key, Manager value, String action) {
            log.info("key:{}, value:{}, action: {}", key, value, action);
        }
    };

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
    }

    @Override
    public Manager saveManager(Manager manager) {
        try {
            managerCache.addListener(managerListener);
            return transactionRunner.doInTransaction(connection -> {
                if (manager.getNo() == null) {
                    var managerNo = managerDataTemplate.insert(connection, manager);
                    var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                    log.info("created manager: {}", createdManager);
                    return createdManager;
                }
                managerDataTemplate.update(connection, manager);
                log.info("updated manager: {}", manager);
                return manager;
            });
        } finally {
            managerCache.removeListener(managerListener);
        }
    }

    @Override
    public Optional<Manager> getManager(long no) {
        Manager manager = managerCache.get(getKeyForCache(no));
        if (manager == null) {
            manager = getManagerFromDb(no).orElseThrow(() -> new RuntimeException("Manager not found, id:" + no));
            managerCache.put(getKeyForCache(no), manager);
        }

        return Optional.of(manager);
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }

    private static String getKeyForCache(Long key) {
        return "keyManager:" + key;
    }

    private Optional<Manager> getManagerFromDb(long no) {
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }
}
