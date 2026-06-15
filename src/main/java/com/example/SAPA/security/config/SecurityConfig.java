package com.example.SAPA.security.config;

import com.example.SAPA.security.filters.JwtAuthenticationFilter;
import com.example.SAPA.security.filters.RestAuthenticationEntryPoint;
import com.example.SAPA.security.repositories.CredentialRepository;
import com.example.SAPA.security.service.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager
    authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                //ACCESO PUBLICO (sin token)

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/specialities").permitAll() //cambiar acceso
                        .requestMatchers("/api/medications/search/**").permitAll()
                //WebSockets
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/uploads/chat/**").permitAll()
                        //ABM de usuarios
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/update").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/profile").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                //Explorar la comunidad/foros/posts sin estar registrado
                        .requestMatchers(HttpMethod.GET, "/forums/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                //Ver medicamentos
                        .requestMatchers(HttpMethod.GET, "/fda/search-medication").permitAll()
                //Visualizar perfil publico de medicos
                        .requestMatchers(HttpMethod.GET, "/doctors/public/**").permitAll()
                //Documentacion de Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                //ACCESO RESTRINGIDO POR PERMISOS (hasAuthority)

                //RF14, RF15: Solo el medico diseña y configura cuestionarios
                        .requestMatchers(HttpMethod.POST, "/questionnaires/**").hasAuthority("CUESTIONARIO_CREAR")
                        .requestMatchers(HttpMethod.PUT, "/questionnaires/**").hasAuthority("CUESTIONARIO_EDITAR")
                        .requestMatchers(HttpMethod.DELETE, "/questionnaires/**").hasAuthority("CUESTIONARIO_ELIMINAR")
                //RF16: Solo el medico lee respuestas de cuestionariosde sus pacientes
                        .requestMatchers(HttpMethod.GET, "/questionnaires/responses/**").hasAuthority("CUESTIONARIO_LEER")
                //RF13: El medico consulta el historial de cuestionarios
                        .requestMatchers(HttpMethod.GET, "/patients/*/questionnaires-history").hasAuthority("CUESTIONARIO_LEER")
                //RF17: Solo el medico publica consejos de salud
                        .requestMatchers(HttpMethod.POST, "/health-tips/create").hasAuthority("CONSEJO_PUBLICAR")
                        .requestMatchers(HttpMethod.PUT, "/health-tips/update/**").hasAuthority("CONSEJO_EDITAR")
                        .requestMatchers(HttpMethod.DELETE, "/health-tips/delete/**").hasAuthority("CONSEJO_ELIMINAR")
                        .requestMatchers(HttpMethod.GET, "/health-tips").permitAll()
                        .requestMatchers(HttpMethod.GET, "/health-tips/my-tips").hasRole("DOCTOR")
                //RF09: El medico aprueba o rechaza solicitudes de seguimiento
                        .requestMatchers(HttpMethod.PUT, "/follow-ups/**").hasAuthority("SEGUIMIENTO_GESTIONAR")
                //RF08: El paciente solicita seguimiento
                        .requestMatchers(HttpMethod.POST, "/follow-ups/request").hasAuthority("SEGUIMIENTO_SOLICITAR")

                //ACCESO GENERAL COMPARTIDO (Cualquier usuario registrado)

                //RF11, RF12, RF27: Chat, historial de mensajes y subida de archivos (Analisis clinicos)
                //Requiere que el usuario este logueado (hasAnyRole) porque se controla el vinculo por codigo
                        .requestMatchers("/chats/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                //RF19, RF22: ABM de foros y publicaciones
                        .requestMatchers(HttpMethod.POST, "/forums").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/posts/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/posts/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                //RF10: Disolver seguimiento
                        .requestMatchers(HttpMethod.DELETE, "/follow-ups/**").hasAnyRole("DOCTOR", "PATIENT")
                //RF18: Ver consejos de salud
                        .requestMatchers(HttpMethod.GET, "/tips/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                //RF25: Reportar contenido inapropiado (Cualquier registrado)
                        .requestMatchers(HttpMethod.POST, "/reports").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                //RF26: Redireccion a turnos del hospital (Solo registrados)
                        .requestMatchers(HttpMethod.GET, "/doctors/*/appointment-link").hasAnyRole("DOCTOR", "PATIENT")
                        //RF28: Guardar articulos del foro como favoritos
                        .requestMatchers(HttpMethod.POST, "/forums/*/favorite").hasAnyRole("DOCTOR", "PATIENT")
                //Cualquier otra ruta no especificada arriba requiere inicio de sesion obligatorio
                        .anyRequest().authenticated())

                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers
                        -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            String jsonResponse = String.format(
                                    "{\"error\": \"%s\", \"status\": %d, \"path\": \"%s\"}",
                                    "Acceso denegado",
                                    HttpServletResponse.SC_FORBIDDEN,
                                    request.getRequestURI()
                            );
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        }));

        return http.build();
    }
}


