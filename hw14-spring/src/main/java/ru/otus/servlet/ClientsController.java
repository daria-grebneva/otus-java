package ru.otus.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.io.IOException;
import java.util.Set;

@Controller
public class ClientsController {

    private final DBServiceClient dbServiceClient;

    public ClientsController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/client")
    protected String doGet() {
        return "clients";
    }

    @PostMapping("/client")
    protected RedirectView doPost(@RequestParam(value = "clientName") String clientName, @RequestParam(value = "clientAddress") String clientAddress, @RequestParam(value = "clientPhone") String clientPhone) throws IOException {

        Address address = new Address(clientAddress, null);
        Phone phone = new Phone(clientPhone, null);
        Client client = new Client(clientName, address, Set.of(phone));
        dbServiceClient.saveClient(client);
        return new RedirectView("/client", true);
    }
}
