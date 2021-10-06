package io.shreyash.assignment2.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.JwtException;
import io.shreyash.assignment2.models.JwtUser;
import io.shreyash.assignment2.security.JwtService;

@WebFilter(urlPatterns={"/dashboard/*"})
public class JwtFilter implements Filter{

	
	@Autowired
    private JwtService jwtTokenService;
	
	@Value("${jwt.auth.header}")
    String authHeader;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {	
	}
	@Override
	public void destroy() {	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        
        //check if options field is available in request
        if (httpRequest.getMethod().toLowerCase().equals("options")) {
        	chain.doFilter(request,response);
        	return;
        }
        
        if (authHeader == null || !authHeaderVal.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }
        
        final String authToken = authHeaderVal.substring(7); // The part after "Bearer "
        
        try
        {
            JwtUser jwtUser = jwtTokenService.getUser(authToken);
            httpRequest.setAttribute("jwtUser", jwtUser);
        }
        catch(JwtException e)
        {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
 
        chain.doFilter(httpRequest, httpResponse);
        
	}

	

}
