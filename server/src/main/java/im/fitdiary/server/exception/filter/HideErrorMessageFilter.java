package im.fitdiary.server.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.server.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class HideErrorMessageFilter implements Filter {

    private final ObjectMapper mapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper((HttpServletResponse) response);
        try {
            chain.doFilter(request, responseWrapper);
        } finally {
            byte[] content = responseWrapper.getContentAsByteArray();
            int status = ((HttpServletResponse) response).getStatus();
            if (status == 400) {
                content = mapper.writeValueAsBytes(Response.failure("bad request"));
            } else if (status == 500) {
                content = mapper.writeValueAsBytes(Response.failure("internal server error"));
            }
            response.getOutputStream().write(content);
        }
    }
}
