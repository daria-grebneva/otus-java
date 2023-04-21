package ru.otus.crm.service;

import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> clientCache = new MyCache<>();

    private final HwListener<String, Client> clientListener = new HwListener<String, Client>() {
        @Override
        public void notify(String key, Client value, String action) {
            log.info("key:{}, value:{}, action: {}", key, value, action);
        }
    };

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                clientCache.put(getKeyForCache(clientId), createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            clientCache.put(getKeyForCache(client.getId()), client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = clientCache.get(getKeyForCache(id));
        if (client == null) {
            client = getClientFromDb(id).orElseThrow(() -> new RuntimeException("Client not found, id:" + id));
            clientCache.put(getKeyForCache(id), client);
        }

        return Optional.of(client);
    }

    @Override
    public List<Client> findAll() {
        try {
            clientCache.addListener(clientListener);
            return transactionRunner.doInTransaction(connection -> {
                var clientList = dataTemplate.findAll(connection);
                log.info("clientList:{}", clientList);
                clientList.forEach(client -> clientCache.put(getKeyForCache(client.getId()), client));
                return clientList;
            });
        } finally {
            clientCache.removeListener(clientListener);
        }
    }

    private static String getKeyForCache(Long key) {
        return "keyClient:" + key;
    }

    private Optional<Client> getClientFromDb(long id) {
        try {
            clientCache.addListener(clientListener);
            return transactionRunner.doInTransaction(connection -> {
                var clientOptional = dataTemplate.findById(connection, id);
                log.info("client: {}", clientOptional);
                clientOptional.ifPresent(client -> clientCache.put(getKeyForCache(id), client));
                return clientOptional;
            });
        } finally {
            clientCache.removeListener(clientListener);
        }
    }
}
