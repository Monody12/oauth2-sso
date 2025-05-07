package org.example.sso.authorizationserver.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class WebController {
    @GetMapping("/sso-error")
    public String ssoErrorPage(
            @RequestParam(name = OAuth2ParameterNames.ERROR, required = false) String errorCode,
            @RequestParam(name = OAuth2ParameterNames.ERROR_DESCRIPTION, required = false) String errorDescriptionParam,
            @RequestParam(name = OAuth2ParameterNames.ERROR_URI, required = false) String errorUriParam,
            @RequestParam(name = "client_redirect_uri", required = false) String encodedClientRedirectUri,
            @RequestParam(name = OAuth2ParameterNames.STATE, required = false) String state,
            @RequestParam(name = "username", required = false) String usernameFromParam,
            Model model) {

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorDescriptionParam);
        model.addAttribute("errorUri", errorUriParam);

        // 用户名逻辑：优先使用SecurityContext中的，其次是参数传递的
        String finalUsername = usernameFromParam;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            finalUsername = authentication.getName();
        }
        model.addAttribute("username", finalUsername);

        // 构建“返回客户端”的URL
        if (StringUtils.hasText(encodedClientRedirectUri)) {
            try {
                String decodedClientRedirectUri = URLDecoder.decode(encodedClientRedirectUri, StandardCharsets.UTF_8);
                UriComponentsBuilder returnToClientUriBuilder = UriComponentsBuilder.fromUriString(decodedClientRedirectUri);

                if (StringUtils.hasText(errorCode)) {
                    returnToClientUriBuilder.queryParam(OAuth2ParameterNames.ERROR, errorCode);
                }
                if (StringUtils.hasText(errorDescriptionParam)) {
                    // errorDescriptionParam已经是解码的，为了放入URL参数，需要重新编码
                    returnToClientUriBuilder.queryParam(OAuth2ParameterNames.ERROR_DESCRIPTION,
                            URLEncoder.encode(errorDescriptionParam, StandardCharsets.UTF_8));
                }
                if (StringUtils.hasText(errorUriParam)) {
                    returnToClientUriBuilder.queryParam(OAuth2ParameterNames.ERROR_URI,
                            errorUriParam);
                }
                if (StringUtils.hasText(state)) {
                    returnToClientUriBuilder.queryParam(OAuth2ParameterNames.STATE, state);
                }
                model.addAttribute("returnToClientUrl", returnToClientUriBuilder.build().encode().toUriString());
            } catch (Exception e) {
                // 日志记录解码/编码错误 e.printStackTrace();
                model.addAttribute("returnToClientUrl", null);
            }
        } else {
            model.addAttribute("returnToClientUrl", null);
        }

        return "sso-error";
    }
}
