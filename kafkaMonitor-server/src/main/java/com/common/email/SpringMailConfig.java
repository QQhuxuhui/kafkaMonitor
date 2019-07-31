package com.common.email;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;
import java.util.Properties;


@org.springframework.context.annotation.Configuration
public class SpringMailConfig {

    @Value("${email.host}")
    String host;

    @Value("${email.port}")
    int port;

    @Value("${email.username}")
    String username;

    @Value("${email.password}")
    String password;

    @Bean
    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
        FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean = new FreeMarkerConfigurationFactoryBean();
        return freeMarkerConfigurationFactoryBean;
    }

    @Bean
    public Configuration freemarkerConfiguration(FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean) throws IOException, TemplateException {
        freeMarkerConfigurationFactoryBean.setTemplateLoaderPath("classpath:/mail_template");
        return freeMarkerConfigurationFactoryBean.createConfiguration();
    }

    @Bean
    public SpringMail springMail(JavaMailSenderImpl javaMailSenderImpl, Configuration freemarkerConfiguration) {
        SpringMail springMail = new SpringMail();
        springMail.setMailSender(javaMailSenderImpl);
        springMail.setFreemarkerConfiguration(freemarkerConfiguration);
        springMail.setSenderUser(username);
        return springMail;
    }

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(host);
        javaMailSenderImpl.setPort(port);
        javaMailSenderImpl.setUsername(username);
        javaMailSenderImpl.setPassword(password);
        javaMailSenderImpl.setDefaultEncoding("UTF-8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.debug", "false");
        javaMailProperties.setProperty("mail.smtp.timeout", "0");
        javaMailSenderImpl.setJavaMailProperties(javaMailProperties);
        return javaMailSenderImpl;
    }

}
