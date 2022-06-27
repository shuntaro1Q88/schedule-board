package com.shunproduct.scheduleboard.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.shunproduct.scheduleboard.util.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	// セキュリティ設定を無視するパスを指定
        return (web) -> web.ignoring().antMatchers("/js/**", "/css/**", "/webjars/**");
    }

    @Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .formLogin(login -> login
                .loginProcessingUrl("/login")
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll()
        )
        .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
        )
        .authorizeHttpRequests(authz -> authz
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .mvcMatchers("/login", "/user-register", "/error").permitAll()
                .mvcMatchers("/admin/**").hasRole(Role.ADMIN.getRoleName())
                .anyRequest().authenticated()
        )
        .headers().frameOptions().sameOrigin();
        
        return http.build();
	}
    
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}


	// WebSecurityConfigurerAdapterのインスタンスを作成。@Orderアノテーションで読み込む順番を設定。
	// 参考 https://qiita.com/kanemotos/items/fbb130ef80653b782592
//	@Configuration
//	@Order(1)
//	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//		protected void configure(HttpSecurity http) throws Exception {
//			// 「"/contents/embed/**"」のように、適用したいURLを指定する。
//			// 今回はヘッダ設定無しにしたかったので、「.headers().frameOptions().disable()」とした。
//			// 「.headers().frameOptions().sameOrigin()」などの設定もあるので、調べてみてください。
//			http.antMatcher("/add-schedule/**").headers().frameOptions().sameOrigin();
//		}
//	}

}
