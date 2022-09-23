package edunhnil.project.forum.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        @Qualifier("delegatedAuthenticationEntryPoint")
        AuthenticationEntryPoint authEntryPoint;

        @Override
        public void configure(HttpSecurity http) throws Exception {
                http
                                .cors().disable() // block strange domain
                                .csrf()
                                .disable()
                                .authorizeRequests()
                                .antMatchers("/auth/login", "/auth/register").permitAll()
                                .antMatchers("/auth/logout")
                                .access("@guard.checkRoleById(request,{\"ROLE_ADMIN\",\"ROLE_DEV\",\"ROLE_USER\"})")
                                .antMatchers("/account/admin/**", "/post/admin/**", "/comment/admin/**")
                                .access("@guard.checkRoleById(request,{\"ROLE_ADMIN\",\"ROLE_DEV\"})")
                                .antMatchers("/account/user/changePassword", "/account/user/changeUsername",
                                                "/account/user/updateUser", "/account/user/deleteUser",
                                                "/account/user/change2FAStatus",
                                                "/account/user/getProfile",
                                                "/post/user/getListPost",
                                                "/post/user/addNewPost", "/comment/user/addNewComment/**",
                                                "/like/user/**")
                                .access("@guard.checkRoleById(request,{\"ROLE_ADMIN\",\"ROLE_DEV\",\"ROLE_USER\"})")
                                .antMatchers("/post/user/getPost/{postId}", "/post/user/updatePost/{postId}",
                                                "/post/user/deletePost/{postId}",
                                                "/post/user/changeEnabled/{postId}")
                                .access("@guard.checkAuthorId(request,#postId)")
                                .antMatchers("/comment/user/editComment/{commentId}",
                                                "/comment/user/deleteComment/{commentId}")
                                .access("@guard.checkCommentId(request, #commentId)")
                                .antMatchers("/category/getList", "/post/public/**",
                                                "/comment/public/**",
                                                "/like/public/**", "/account/public/**")
                                .permitAll()
                                .antMatchers("/swagger-ui/**", "/api-docs/**",
                                                "/search/totalSearch", "/auth/verifyEmail/**",
                                                "/auth/forgotPassword/**", "/auth/verify2FA/**")
                                .permitAll().anyRequest().authenticated().and()
                                .exceptionHandling()
                                .authenticationEntryPoint(authEntryPoint);
        }

}
