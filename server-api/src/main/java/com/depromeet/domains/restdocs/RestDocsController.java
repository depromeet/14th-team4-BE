package com.depromeet.domains.restdocs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestDocsController {


    @GetMapping("/docs")
    public String docs() {
        return "forward:/docs/index.html";
    }
}
