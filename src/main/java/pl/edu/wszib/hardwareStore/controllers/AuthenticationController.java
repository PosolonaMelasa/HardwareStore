package pl.edu.wszib.hardwareStore.controllers;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.edu.wszib.hardwareStore.exceptions.AuthValidationException;
import pl.edu.wszib.hardwareStore.services.AuthenticationService;
import pl.edu.wszib.hardwareStore.session.SessionObject;
import pl.edu.wszib.hardwareStore.validators.LoginValidator;

import javax.annotation.Resource;


@RequestMapping(value = "/")
@Controller
@AllArgsConstructor
public class AuthenticationController {

    AuthenticationService authenticationService;

    @Resource
    SessionObject sessionObject;

    @GetMapping(value = "/login")
    public String getLoginForm() { return "login"; }

    @PostMapping(value = "/login")
    public String signIn(@RequestParam String login, @RequestParam String password){
        try{
            LoginValidator.validateLogin(login);
            LoginValidator.validatePass(password);
        } catch (AuthValidationException e){
            return "redirect:/login";
        }

        authenticationService.authenticate(login, password);

        if(this.sessionObject.isLogged()){
            return "redirect:/main";
        } else{
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/signup")
    public String getSignUpForm() { return "signup"; }

    @PostMapping(value = "/signup")
    public String signUp(@RequestParam String login, @RequestParam String password){
        authenticationService.signUp(login, password);
        try{
            LoginValidator.validateLogin(login);
            LoginValidator.validatePass(password);
        } catch (AuthValidationException e){
            return "redirect:/login";
        }

        if(this.sessionObject.isLogged()){
            return "redirect:/main";
        } else{
            return "redirect:/signup";
        }
    }

    @GetMapping(value = "/logout")
    public String logout() {
        this.sessionObject.setUser(null);
        return "redirect:/main";
    }


}
