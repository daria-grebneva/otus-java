package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.ClientDao;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ClientsApiServlet extends HttpServlet {

    private final ClientDao clientDao;
    private final Gson gson;

    public ClientsApiServlet(ClientDao clientDao, Gson gson) {
        this.clientDao = clientDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Client> clients = clientDao.findAll();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if ("submit".equals(action)) {
            Long id = getIdOfNewFields();
            Address address = new Address(id, request.getParameter("clientAddress"));
            Phone phone = new Phone(id, request.getParameter("clientPhone"));
            Client client = new Client(id, request.getParameter("clientName"), address, Collections.singletonList(phone));
            try {
                clientDao.saveClient(client);
            } catch (Exception e) {
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(SC_OK);
            response.sendRedirect("/client");
        }
    }

    private Long getIdOfNewFields() {
        List<Client> clients = clientDao.findAll();
        return (long) clients.size() + 1L;
    }
}
