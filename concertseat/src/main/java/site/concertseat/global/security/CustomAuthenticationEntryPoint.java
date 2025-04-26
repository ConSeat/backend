package site.concertseat.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import site.concertseat.global.dto.ResponseData;
import site.concertseat.global.dto.ResponseHeader;

import java.io.IOException;

import static site.concertseat.global.statuscode.ErrorCode.INVALID_ACCESS_TOKEN;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException{

        response.setStatus(INVALID_ACCESS_TOKEN.getHttpStatusCode());
        response.setContentType("application/json");

        ResponseData res = new ResponseData(new ResponseHeader(INVALID_ACCESS_TOKEN.getMessage()), null);
        response.getWriter().write(mapper.writeValueAsString(res));
    }
}
