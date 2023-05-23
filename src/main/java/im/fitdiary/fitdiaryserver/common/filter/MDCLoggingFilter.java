package im.fitdiary.fitdiaryserver.common.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

public class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String uuid = UUID.randomUUID().toString().split("-")[0];
            MDC.put("traceId", uuid);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
