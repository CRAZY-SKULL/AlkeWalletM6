package cl.AlkeWalletM6.AlkeWalletM6.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Lógica de manejo de errores
        return "error";
    }

    //@Override
    public String getErrorPath() {
        return "/error";
    }
}
