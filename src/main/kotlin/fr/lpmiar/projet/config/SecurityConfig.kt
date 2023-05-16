package fr.lpmiar.projet.config

import fr.lpmiar.projet.Services.ProfDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userDetailsService: ProfDetailsService

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder())
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/prof/**").hasRole("PROF")
            .antMatchers("/etudiant/**").hasRole("PROF")
            .antMatchers("/cours/**").hasRole("PROF")
            .antMatchers("/creneau/**").hasRole("PROF")
            .antMatchers("/presence/**").hasAnyRole("PROF")
            .antMatchers("/groupe/**").hasAnyRole("PROF")
            .antMatchers("/formation/**").hasAnyRole("PROF")
            .anyRequest().authenticated()
            .and()
            .csrf().ignoringAntMatchers("/h2-console/**") // désactiver la protection CSRF pour la console H2
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
    }

    @Bean
    fun encoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}