package ru.captive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.servlet.http.HttpServletResponse;

@Controller
public class WebAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAccountController.class);

    private static final String ACCOUNT_ATTR = "account";
    private static final String REDIRECTED_ATTR = "redirected";
    private final AccountService accountService;
    private final CaptiveService captiveService;

    private static class RedirectedWrapper {
        private boolean redirected;

        public RedirectedWrapper() {
        }

        public RedirectedWrapper(boolean b) {
            redirected = b;
        }

        public boolean isRedirected() {
            return redirected;
        }

        public void setRedirected(boolean redirected) {
            this.redirected = redirected;
        }
    }

    @Autowired
    public WebAccountController(AccountService accountService, CaptiveService captiveService) {
        this.accountService = accountService;
        this.captiveService = captiveService;
    }

    @RequestMapping("")
    public String getForm(Model model, HttpServletResponse response,
                          @ModelAttribute(REDIRECTED_ATTR) RedirectedWrapper redirectedWrapper) {
        if (!model.containsAttribute(ACCOUNT_ATTR)) {
            model.addAttribute(ACCOUNT_ATTR, new Account());
        }
        model.addAttribute("action", "/save");
        if (redirectedWrapper == null || !redirectedWrapper.isRedirected()) {
            int networkAuthenticationRequired = 511;
            response.setStatus(networkAuthenticationRequired);
            return "index";
        } else {
            return "success";
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveAccount(@Validated(Account.New.class) Account account,
                              BindingResult result,
                              RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("org.springframework.validation.BindingResult." + ACCOUNT_ATTR, result);
            attributes.addFlashAttribute(ACCOUNT_ATTR, account);
            return "redirect:/";
        }
        accountService.save(account);
        attributes.addFlashAttribute(ACCOUNT_ATTR, account);
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String afterLogin(@ModelAttribute(ACCOUNT_ATTR) @Validated(Account.Existing.class) Account account,
                             RedirectAttributes attributes) {
        if (account != null) {
            captiveService.allowConnection();
        }
        attributes.addFlashAttribute(REDIRECTED_ATTR, new RedirectedWrapper(true));
        return "redirect:/";
    }
}