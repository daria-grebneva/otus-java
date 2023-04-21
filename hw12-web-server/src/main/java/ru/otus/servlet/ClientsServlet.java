package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private final TemplateProcessor templateProcessor;

    private final DBServiceClient dbServiceClient;

    public ClientsServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if ("submit".equals(action)) {
            Long id = getIdOfNewFields();
            Address address = new Address(id, request.getParameter("clientAddress"));
            Phone phone = new Phone(id, request.getParameter("clientPhone"));
            Client client = new Client(id, request.getParameter("clientName"), address, Collections.singletonList(phone));
            dbServiceClient.saveClient(client);
            response.sendRedirect("/client");
        }
    }

    private Long getIdOfNewFields() {
        List<Client> clients = dbServiceClient.findAll();
        return (long) clients.size() + 1L;
    }
}
