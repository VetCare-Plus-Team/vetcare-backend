package com.vetcare.api_gateway.filter;


import com.vetcare.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    
    
    public AuthenticationFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            
            String path = exchange.getRequest().getURI().getPath();
            // Auth endpoints and  Image endpoints goes without token
            if ( path.contains("/api/auth") || path.contains("/api/employees/image/") || path.contains("/api/qr/image/") || path.contains("api/medical-records/history/")) {
                return chain.filter(exchange);
            }
            
            // Header eke Authorization kalla thiyenawada balanawa
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            
            try {
                // Token eka validate karanawa
                jwtUtil.validateToken(authHeader);
                
                // Token eken role ekai user ID ekai gannawa
                Claims claims = jwtUtil.getAllClaimsFromToken(authHeader);
                String role = claims.get("role", String.class);
                String userId = claims.get("userId", String.class);
                
                // E tika headers walata dala anith service ekata yawanawa
                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header("X-User-Role", role)
                        .header("X-User-Id", userId)
                        .build();
                
                return chain.filter(exchange.mutate().request(request).build());
                
            } catch (Exception e) {
                System.out.println("Invalid Token Encountered: " + e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        });
    }
    
    public static class Config {
        // Configuration properties can go here
    }
}
