package com.depromeet.domains;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/v1")
public class RestDocsController {

    @GetMapping("/docs")
    public String docs() {
        return "redirect:/docs/index.html";
    }
}
