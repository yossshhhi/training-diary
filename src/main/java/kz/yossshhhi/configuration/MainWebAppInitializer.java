package kz.yossshhhi.configuration;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Custom web application initializer that configures the servlet context for the application.
 * It sets up the Spring context and registers the main Spring DispatcherServlet, and
 * configures a filter for JWT authentication on specific URL patterns.
 */
public class MainWebAppInitializer implements WebApplicationInitializer {

    /**
     * Configures the provided ServletContext with Spring's web application settings.
     * This includes setting up the application context, registering servlets and filters.
     *
     * @param servletContext the {@link ServletContext} to be configured
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("kz.yossshhhi");
        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("mvc", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");

        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("jwtTokenFilter", new DelegatingFilterProxy());
        filterRegistration.addMappingForUrlPatterns(null, false, "/admin/*", "/user/*");
        filterRegistration.setAsyncSupported(true);
    }
}
