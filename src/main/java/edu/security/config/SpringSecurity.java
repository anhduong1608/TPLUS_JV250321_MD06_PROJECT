package edu.security.config;


import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import edu.security.jwt.JwtFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SpringSecurity {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JWTAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req -> req

                        // ========== AUTH ==========
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/me").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        // ========== USER MANAGEMENT ==========
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // ========== STUDENTS ==========
                        .requestMatchers(HttpMethod.GET, "/api/students").hasAnyRole("ADMIN", "MENTOR")
                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**").hasAnyRole("ADMIN", "STUDENT")

                        // ========== MENTORS ==========
                        .requestMatchers(HttpMethod.GET, "/api/mentors").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/mentors/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/mentors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/mentors/**").hasAnyRole("ADMIN", "MENTOR")

                        // ========== INTERNSHIP PHASES ==========
                        .requestMatchers(HttpMethod.GET, "/api/internship_phases/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/internship_phases").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/internship_phases/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/internship_phases/**").hasRole("ADMIN")

                        // ========== EVALUATION CRITERIA ==========
                        .requestMatchers(HttpMethod.GET, "/api/evaluation_criteria/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/evaluation_criteria").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/evaluation_criteria/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/evaluation_criteria/**").hasRole("ADMIN")

                        // ========== ASSESSMENT ROUNDS ==========
                        .requestMatchers(HttpMethod.GET, "/api/assessment_rounds/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/assessment_rounds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/assessment_rounds/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/assessment_rounds/**").hasRole("ADMIN")

                        // ========== ROUND CRITERIA ==========
                        .requestMatchers(HttpMethod.GET, "/api/round_criteria/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/round_criteria").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/round_criteria/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/round_criteria/**").hasRole("ADMIN")

                        // ========== INTERNSHIP ASSIGNMENTS ==========
                        .requestMatchers(HttpMethod.GET, "/api/internship_assignments/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/internship_assignments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/internship_assignments/**/status").hasRole("ADMIN")

                        // ========== ASSESSMENT RESULTS ==========
                        .requestMatchers(HttpMethod.GET, "/api/assessment_results/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/assessment_results").hasRole("MENTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/assessment_results/**").hasRole("MENTOR")


                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint()))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}