package org.example.sso.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class HomeController {

    @Autowired
    private WebClient webClient;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        model.addAttribute("userInfo", oidcUser.getClaims());
        model.addAttribute("idToken", oidcUser.getIdToken().getTokenValue());
        return "profile";
    }

    @GetMapping("/resource")
    public String resource(@RegisteredOAuth2AuthorizedClient("client") OAuth2AuthorizedClient authorizedClient,
                           Model model) {
        String resourceResponse = webClient
                .get()
                .uri("http://localhost:8090/api/resource")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        model.addAttribute("resourceResponse", resourceResponse);
        return "resource";
    }
}