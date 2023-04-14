package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DBServiceManager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.mapper.*;

import javax.sql.DataSource;
import java.util.List;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Инициализация кеша
        HwCache<Long, Client> clientCache = new MyCache<>();
        HwCache<Long, Manager> managerCache = new MyCache<>();
        HwListener<Long, Client> clientListener = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        HwListener<Long, Manager> managerListener = new HwListener<Long, Manager>() {
            @Override
            public void notify(Long key, Manager value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        clientCache.addListener(clientListener);
        managerCache.addListener(managerListener);

// Работа с клиентом
        EntityClassMetaData entityClassMetaDataClient = new EntityClassMetaDataImpl(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));
        dbServiceClient.saveClient(new Client("dbServiceSecond"));

// Работа с менеджером
        EntityClassMetaData entityClassMetaDataManager = new EntityClassMetaDataImpl(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));
        dbServiceManager.saveManager(new Manager("ManagerSecond"));

        initClientCache(clientCache, dbServiceClient);
        initManagerCache(managerCache, dbServiceManager);

        var clientFromCache = getClient(clientCache, dbServiceClient, 1L);
        log.info("Fetched client: " + clientFromCache);

        var managerFromCache = getManager(managerCache, dbServiceManager, 2L);
        log.info("Fetched manager: " + managerFromCache);


// Не забываем убирать clientListener
        clientCache.removeListener(clientListener);
        managerCache.removeListener(managerListener);
    }

    private static Client getClient(HwCache<Long, Client> clientCache, DBServiceClient dbServiceClient, Long key) {
        Client client = clientCache.get(key);
        if (client == null) {
            client = dbServiceClient.getClient(key).orElseThrow(() -> new RuntimeException("Client not found, id:" + key));
            clientCache.put(key, client);
        }

        return client;
    }


    private static Manager getManager(HwCache<Long, Manager> managerCache, DBServiceManager dbServiceManager, Long key) {
        Manager manager = managerCache.get(key);
        if (manager == null) {
            manager = dbServiceManager.getManager(key).orElseThrow(() -> new RuntimeException("Manager not found, id:" + key));
            managerCache.put(key, manager);
        }

        return manager;
    }

    private static void initClientCache(HwCache<Long, Client> clientCache, DbServiceClientImpl dbServiceClient) {
        List<Client> clientList = dbServiceClient.findAll();
        clientList.forEach(client -> clientCache.put(client.getId(), client));
    }

    private static void initManagerCache(HwCache<Long, Manager> managerCache, DbServiceManagerImpl dbServiceManager) {
        List<Manager> managerList = dbServiceManager.findAll();
        managerList.forEach(manager -> managerCache.put(manager.getNo(), manager));
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
