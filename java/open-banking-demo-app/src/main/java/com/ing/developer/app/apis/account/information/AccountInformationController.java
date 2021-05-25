package com.ing.developer.app.apis.account.information;

import com.ing.developer.account.information.client.model.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountInformationController {

    private final UUID userId = UUID.randomUUID(); // For Demo Purposes only!

    private final AccountInformationService service;

    public AccountInformationController(AccountInformationService service) {
        this.service = service;
    }

    @GetMapping
    public String getRoot() {
        return "<h1>My Awesome 3rd Party Demo App</h1>" +
                "<h2>Account endpoint</h2>" +
                "<a href=\"/account/me\">My Accounts</a><br><br>" +
                "<a href=\"/account/authorize\">Authorize this awesome app to access my ING accounts</a>";
    }

    @GetMapping("/me")
    public List<Account> getMyAccounts() {
        return service.getAccounts(userId);
    }

    @GetMapping("/authorize")
    public String getConsentUri() {
        String consentUri = service.getConsentUri();
        return "<a href=\"" + consentUri + "\">" + consentUri + "</a>";
    }

    @GetMapping("/authorize/token")
    public String getCustomerAccessToken(@RequestParam String code) {
        return "<a href=\"/account/me\">My accounts</a><br><br>" + service.getCustomerAccessToken(userId, code);
    }

}
