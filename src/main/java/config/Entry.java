package config;

import config.hibernateConfig.HibernateConfig;
import config.springConfig.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@Import({HibernateConfig.class, WebConfig.class})
public class Entry {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Entry.class);
    }

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
                ctx.register(WebConfig.class);
                ctx.setServletContext(servletContext);
                DispatcherServlet dServlet =  new DispatcherServlet(ctx);
                dServlet.setThrowExceptionIfNoHandlerFound(true);
                ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dServlet);
                servlet.setLoadOnStartup(1);
                servlet.addMapping("/*");

                EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
                CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
                characterEncodingFilter.setEncoding("UTF-8");
                characterEncodingFilter.setForceEncoding(true);
                FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncoding", characterEncodingFilter);
                characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
            }
        };
    }
}
