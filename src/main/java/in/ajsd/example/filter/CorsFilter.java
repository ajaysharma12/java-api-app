package in.ajsd.example.filter;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class CorsFilter implements Filter {

  private static final Set<Pattern> allowedOrigins = Sets.newHashSet(
      Pattern.compile("^http://127.0.0.1(:\\d{2,4})?.*$"),
      Pattern.compile("^http://localhost(:\\d{2,4})?.*$"));

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) (request);
    HttpServletResponse httpResponse = (HttpServletResponse) (response);
    String origin = httpRequest.getHeader("Origin");
    if (Strings.isNullOrEmpty(origin)) {
      chain.doFilter(request, response);
      return;
    }
    for (Pattern regex : allowedOrigins) {
      if (regex.matcher(origin).matches()) {
        httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        break;
      }
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
}
