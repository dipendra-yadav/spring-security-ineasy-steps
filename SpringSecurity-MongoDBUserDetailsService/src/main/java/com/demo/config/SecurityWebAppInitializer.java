package com.demo.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author deependra
 *
 */
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
 
    public SecurityWebAppInitializer() {
        super(SecurityConfig.class);
    }

}
