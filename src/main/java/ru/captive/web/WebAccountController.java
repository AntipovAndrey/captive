package ru.captive.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.captive.model.Account;
import ru.captive.service.AccountService;

@Controller
public class WebAccountController {

    private final AccountService accountService;

    @Autowired
    public WebAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("")
    public String getForm(Model model) {
        if (!model.containsAttribute("account")) {
            model.addAttribute("account", new Account());
        }
        model.addAttribute("action", "/save");
        return "index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveAccount(@Validated(Account.New.class) Account account, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.account", result);
            attributes.addFlashAttribute("account", account);
            return "redirect:/";
        }
        accountService.save(account);
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public @ResponseBody
    String afterLogin() {
        // TODO: connect to after auth page
        return " not implemented yet ";
    }
}
