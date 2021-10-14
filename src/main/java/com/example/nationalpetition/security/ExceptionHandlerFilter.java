package com.example.nationalpetition.security;

import com.example.nationalpetition.utils.error.exception.UnAuthorizedException;
import com.example.nationalpetition.utils.message.MessageType;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private static final String UTF8 = "application/json;charset=UTF-8";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnAuthorizedException e) {
            log.error(e.getMessage());
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, UnAuthorizedException e) throws IOException {
        response.setContentType(UTF8);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put(MessageType.CODE.getMessage(), e.getErrorCode().getCode());
        responseJson.put(MessageType.MESSAGE.getMessage(), e.getErrorCode().getMessage());

        response.getWriter().print(responseJson);
    }
}
