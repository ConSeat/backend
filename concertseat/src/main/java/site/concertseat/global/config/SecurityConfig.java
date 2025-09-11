package site.concertseat.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import site.concertseat.global.jwt.filter.JwtAuthenticationFilter;
import site.concertseat.global.jwt.service.JwtUtils;
import site.concertseat.global.oauth.CustomOAuth2UserService;
import site.concertseat.global.oauth.Oauth2SuccessHandler;
import site.concertseat.global.security.CustomAuthenticationEntryPoint;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${FRONT_URL}")
    private String frontURL;

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/members/**").authenticated()
                        .requestMatchers("/api/reviews/{reviewId}").authenticated()
                        .requestMatchers(POST, "/api/**").authenticated()
                        .requestMatchers(DELETE, "/api/**").authenticated()
                        .requestMatchers("/api/**", "/h2-console/**", "/docs/**").permitAll()
                        .requestMatchers("/actuator/prometheus").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer
                        .configurationSource(corsConfigurationSource())
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .authorizationEndpoint(authEndPoint -> authEndPoint
                                .baseUri("/api/auth/login"))
                        .redirectionEndpoint(authEndPoint -> authEndPoint
                                .baseUri("/api/auth/oauth2/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                        .successHandler(oauth2SuccessHandler)
                );


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", frontURL, "https://conseat-ruddy.vercel.app"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
