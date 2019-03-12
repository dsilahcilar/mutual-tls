package org.deniz.secureapi

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig(@Value("\${client.user}") val userName: String) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService())
    }

    override fun userDetailsService() = UserDetailService(userName)

    class UserDetailService(private val userName: String) : UserDetailsService {
        override fun loadUserByUsername(user: String?): UserDetails {
            if (user == this.userName) {
                return User(user, "",
                        AuthorityUtils.commaSeparatedStringToAuthorityList(ROLE_CLIENT)
                )
            } else {
                throw UsernameNotFoundException(String.format("User %s not found", user))
            }
        }
    }

    companion object {
        const val ROLE_CLIENT = "ROLE_CLIENT"
    }

}