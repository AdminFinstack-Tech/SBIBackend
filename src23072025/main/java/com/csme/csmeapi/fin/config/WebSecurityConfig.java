package com.csme.csmeapi.fin.config;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	FinUtil finUtil;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${custom.security.enabled}")
    private boolean securityEnabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (securityEnabled) {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers(getAllowedEndpoints()).permitAll()
                    .anyRequest().authenticated()
                    .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        } else {
            http
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .and()
                    .csrf().disable()
                    .httpBasic().disable();
        }
    }

    private String[] getAllowedEndpoints() throws IOException {
    	System.out.println("====================================="+FinUtil.intPath);
        Resource resource = resourceLoader.getResource("file:"+FinUtil.intPath+"security-config.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try (InputStream inputStream = resource.getInputStream()) {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList endpoints = document.getElementsByTagName("endpoint");
            String[] allowedEndpoints = new String[endpoints.getLength()];
            for (int i = 0; i < endpoints.getLength(); i++) {
                allowedEndpoints[i] = endpoints.item(i).getTextContent();
            }
            return allowedEndpoints;
        } catch (ParserConfigurationException | SAXException e) {
            // Handle exceptions
            throw new RuntimeException("Error reading security configuration XML file.", e);
        }
    }
}

