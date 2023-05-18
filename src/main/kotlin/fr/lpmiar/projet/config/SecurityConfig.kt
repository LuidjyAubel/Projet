package fr.lpmiar.projet.config

import fr.lpmiar.projet.Services.ProfDetailsService
import fr.lpmiar.projet.web.ProfController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    var logger: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)
    override fun configure(auth: AuthenticationManagerBuilder) {
        logger.info("Configuring authentication")
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder())
    }

    override fun configure(http: HttpSecurity) {
        logger.info("Configuring HTTP security")
        http.authorizeRequests()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/prof/**").hasRole("PROF")
            .antMatchers("/etudiant/**").hasRole("PROF")
            .antMatchers("/cours/**").hasRole("PROF")
            .antMatchers("/creneau/**").hasRole("PROF")
            .antMatchers("/presence/**").hasAnyRole("PROF")
            .antMatchers("/groupe/**").hasAnyRole("PROF")
            .antMatchers("/formation/**").hasAnyRole("PROF")
            //.antMatchers("/formation/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().ignoringAntMatchers("/h2-console/**") // désactiver la protection CSRF pour la console H2
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            .httpBasic()
            .and()
            .cors().and().csrf().disable()

        logger.info("HTTP security configured")
    }

    @Bean
    fun encoder(): PasswordEncoder? {
        logger.info("Creating password encoder")
        return BCryptPasswordEncoder()
    }
}