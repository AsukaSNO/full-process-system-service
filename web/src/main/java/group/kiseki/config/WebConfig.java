package group.kiseki.config;

import group.kiseki.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 *
 * @author Yan
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/users/**")  // 用户相关接口需要认证
                .addPathPatterns("/api/auth/profile")  // 获取用户信息需要认证
                .addPathPatterns("/api/auth/logout")   // 登出需要认证
                .excludePathPatterns("/api/auth/register")  // 注册不需要认证
                .excludePathPatterns("/api/auth/login")     // 登录不需要认证
                .excludePathPatterns("/api/auth/refresh")   // 刷新token不需要认证
                .excludePathPatterns("/api/health/**")      // 健康检查不需要认证
                .excludePathPatterns("/actuator/**");       // 监控端点不需要认证
    }
}
