package im.fitdiary.fitdiaryserver.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.config.properties.Mode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ErrorMessageFilter implements Filter {

    private final ConfigProperties properties;
    private final ObjectMapper mapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper((HttpServletResponse) response);
        try {
            chain.doFilter(request, responseWrapper);
        } finally {
            Mode mode = properties.getMode();
            byte[] content = responseWrapper.getContentAsByteArray();
            if (mode != null && mode.equals(Mode.PROD)) {
                int status = ((HttpServletResponse) response).getStatus();
                if (status == 400) {
                    content = mapper.writeValueAsBytes(Response.failure("bad request"));
                } else if (status == 500) {
                    content = mapper.writeValueAsBytes(Response.failure("internal server error"));
                }
            }
            response.getOutputStream().write(content);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
