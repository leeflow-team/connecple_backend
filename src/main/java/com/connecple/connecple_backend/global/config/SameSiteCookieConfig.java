package com.connecple.connecple_backend.global.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SameSiteCookieConfig {

    @Bean
    public FilterRegistrationBean<Filter> sameSiteFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter((request, response, chain) -> {
            chain.doFilter(request, response);

            if (response instanceof HttpServletResponse) {
                HttpServletResponse httpResp = (HttpServletResponse) response;
                Collection<String> headers = httpResp.getHeaders("Set-Cookie");
                List<String> newHeaders = headers.stream()
                        .map(header -> {
                            if (header.contains("JSESSIONID")) {
                                if (!header.toLowerCase().contains("samesite")) {
                                    return header + "; SameSite=None; Secure";
                                }
                            }
                            return header;
                        })
                        .toList();
                httpResp.setHeader("Set-Cookie", String.join(",", newHeaders));
            }
        });
        registrationBean.setOrder(1); // 우선순위
        return registrationBean;
    }
}
