package com.blogcode.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.servlet.Filter;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Configuration
@EnableOAuth2Client
public class OAuthConfig {

    private final OAuth2ClientContext oauth2ClientContext;

    public OAuthConfig(OAuth2ClientContext oauth2ClientContext) {
        this.oauth2ClientContext = oauth2ClientContext;
    }

    @Bean
    public Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter githubFilter = new OAuth2ClientAuthenticationProcessingFilter("/login");
        OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate(googleClient(), oauth2ClientContext);
        githubFilter.setRestTemplate(githubTemplate);
        githubFilter.setTokenServices(new UserInfoTokenServices(googleResource().getUserInfoUri(), googleClient().getClientId()));
        return githubFilter;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public OAuth2ProtectedResourceDetails googleClient() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("google.yml"));

        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}
