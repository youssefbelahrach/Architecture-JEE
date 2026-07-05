package ma.enset.productapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité de l'application.
 *
 * Étape 5 (protection par défaut) : pour DÉSACTIVER temporairement la sécurité
 * pendant les tests de la couche DAO / des vues, il suffit de commenter le bean
 * securityFilterChain ci-dessous (ou d'autoriser tout via permitAll()).
 *
 * Étape 7 : on active une authentification par formulaire avec deux rôles :
 *   - USER  : peut consulter la liste et rechercher   (/user/**)
 *   - ADMIN : peut en plus ajouter, éditer, supprimer  (/admin/**)
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Utilisateurs stockés en mémoire (pour la démo).
     * En production on brancherait une base de données (JdbcUserDetailsManager
     * ou un UserDetailsService personnalisé).
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder encoder) {
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("1234"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/index", true)
                        .permitAll())
                .authorizeHttpRequests(auth -> auth
                        // Ressources publiques
                        .requestMatchers("/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // Console H2 (utile en dev)
                        .requestMatchers("/h2-console/**").permitAll()
                        // Zone consultation : rôle USER
                        .requestMatchers("/user/**").hasRole("USER")
                        // Zone administration : rôle ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Le reste nécessite une authentification
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedPage("/notAuthorized"))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                // Désactivations utiles pour la console H2 (frames + CSRF sur H2)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
