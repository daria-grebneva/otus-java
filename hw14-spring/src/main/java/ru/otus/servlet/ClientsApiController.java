package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.Client;

import java.io.IOException;
import java.util.List;


@Controller
public class ClientsApiController {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiController(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @GetMapping("/api/clients")
    protected void getAllClients(HttpServletResponse response) throws IOException {
        List<Client> clients = dbServiceClient.findAll();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }
}
