package in.ajsd.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

/** The main servlet context listener that instantiates the Guice {@link Injector}. */
public class GuiceMain extends GuiceServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(GuiceMain.class);

  private String contextPath = "";

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    contextPath = servletContextEvent.getServletContext().getContextPath();
    super.contextInitialized(servletContextEvent);
  };

  @Override
  protected Injector getInjector() {
    log.info("Creating injector");
    return Guice.createInjector(new MainModule(contextPath));
  }
}
