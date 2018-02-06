package ru.captive.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.captive.model.Account;
import ru.captive.service.AccountService;
import ru.captive.service.CaptiveService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WebAccountController {

    private final AccountService accountService;
    private final CaptiveService captiveService;

    @Autowired
    public WebAccountController(AccountService accountService, CaptiveService captiveService) {
        this.accountService = accountService;
        this.captiveService = captiveService;
    }

    @RequestMapping("")
    public String getForm(Model model, HttpServletResponse response) {
        if (!model.containsAttribute("account")) {
            model.addAttribute("account", new Account());
        }
        model.addAttribute("action", "/save");
        int networkAuthenticationRequired = 511;
        response.setStatus(networkAuthenticationRequired);
        return "index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveAccount(@Validated(Account.New.class) Account account,
                              BindingResult result,
                              RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.account", result);
            attributes.addFlashAttribute("account", account);
            return "redirect:/";
        }
        accountService.save(account);
        attributes.addFlashAttribute("account", account);
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String afterLogin(@ModelAttribute("account") @Validated(Account.Existing.class) Account account) {
        final String redirectPageString = "redirect:/";
        if (account == null) {
            return redirectPageString;
        }
        captiveService.allowConnection();
        return redirectPageString;
    }
}
