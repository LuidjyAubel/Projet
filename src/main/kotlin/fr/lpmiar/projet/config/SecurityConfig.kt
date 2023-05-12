package fr.lpmiar.projet.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
    override fun configure(http: HttpSecurity) {
            http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/prof/**").hasRole("PROF")
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/etudiant/**").hasRole("PROF")
            .antMatchers("/cours/**").hasRole("PROF")
            .antMatchers("/creneau/**").hasRole("PROF")
            .antMatchers("/presence/**").hasAnyRole("PROF")
            .antMatchers("/groupe/**").hasAnyRole("PROF")
            .antMatchers("/formation/**").hasAnyRole("PROF")
            .and()
            .csrf().disable()
            .formLogin().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("prof")
            .password(encoder().encode("prof"))
            .roles("PROF")
    }
}