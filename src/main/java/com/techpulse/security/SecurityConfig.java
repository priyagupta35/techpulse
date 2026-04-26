package com.techpulse.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techpulse.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final  JwtFilter jwtFilter;
  private final UserDetailsServiceImpl userDetailsService;

  public SecurityConfig(JwtFilter jwtFilter,UserDetailsServiceImpl userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService=userDetailsService;
    }


@Bean
   public SecurityFilterChain securityFilterChain(
        HttpSecurity http) throws Exception {
     http
        //disable CSRF-not needed for statelss REST APIS
        .csrf(csrf->csrf.disable())
         .cors(cors -> {})
        //define which endpoint require what level of access
        .authorizeHttpRequests(auth->auth

        //public endpoints-no authentication neede
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET,
                 "/api/articles/**").permitAll()
             .requestMatchers(HttpMethod.GET,
                 "/api/categories/**").permitAll()
            .requestMatchers(HttpMethod.GET,
                "/api/community-posts/**").permitAll()

                     //admin only endpouints
            .requestMatchers(HttpMethod.POST,
                "/api/articles/fetch").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT,
                "/api/community-posts/*/status")
                .hasAuthority("ROLE_ADMIN")
           .requestMatchers(HttpMethod.DELETE,
             "/api/articles/**").hasAuthority("ROLE_ADMIN")
              
             //contributor endpoints-registered users only
            .requestMatchers(HttpMethod.POST,
                "/api/community-posts").hasAnyAuthority(
                    "ROLE_CONTRIBUTOR" , "ROLE_ADMIN")

            //EVERYTHING ELSE REQUIRES AUTHENTICATION
             .anyRequest().authenticated()
                )
            
            //stateless session-spring will nver create an http session because jwt handles state
            .sessionManagement(session->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex -> ex
        .authenticationEntryPoint((req, res, e) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Unauthorized");
        })
    )
            
            //set our custom authentication provider
            .authenticationProvider(authenticationProvider())

            //jwt filter before springs default
            .addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

                return http.build();
            }

            @Bean
            public PasswordEncoder passwordEncoder()
            {
                return new BCryptPasswordEncoder();
            }
            @Bean
            public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
      
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
            } 

            //authentication manager is neede by the authenticationcontroller

            @Bean
            public AuthenticationManager authenticationManager(
                AuthenticationConfiguration config) throws Exception {
                    return config.getAuthenticationManager();
                }
          

}
