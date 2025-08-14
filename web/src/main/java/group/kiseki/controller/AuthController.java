package group.kiseki.controller;

import group.kiseki.auth.dto.LoginRequest;
import group.kiseki.auth.dto.RefreshTokenRequest;
import group.kiseki.auth.dto.UserDTO;
import group.kiseki.auth.service.AuthService;
import group.kiseki.auth.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author Yan
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 执行注册
            boolean success = authService.register(userDTO);
            
            if (success) {
                response.put("success", true);
                response.put("message", "注册成功");
            } else {
                response.put("success", false);
                response.put("message", "注册失败，用户名已存在或参数错误");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册异常：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String clientIp = HttpUtil.getClientIp(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");

            group.kiseki.auth.dto.LoginResult loginResult = authService.login(
                loginRequest, 
                clientIp, 
                userAgent
            );

            if (loginResult != null) {
                response.put("success", true);
                response.put("message", "登录成功");
                response.put("token", loginResult.getToken());
                response.put("refreshToken", loginResult.getRefreshToken());
                response.put("user", loginResult.getUserInfo());
                response.put("expireTime", loginResult.getExpireTime());
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "用户名或密码错误");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "登录异常：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 刷新token
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String newToken = authService.refreshToken(refreshTokenRequest);
            
            if (newToken != null) {
                response.put("success", true);
                response.put("message", "Token刷新成功");
                response.put("token", newToken);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "刷新Token失败，Token已过期或无效");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "刷新Token异常：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String extractedToken = HttpUtil.extractTokenFromHeader(token);
            boolean success = authService.logout(extractedToken);
            
            if (success) {
                response.put("success", true);
                response.put("message", "登出成功");
            } else {
                response.put("success", false);
                response.put("message", "登出失败");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "登出异常：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String extractedToken = HttpUtil.extractTokenFromHeader(token);
            Integer userId = authService.validateToken(extractedToken);
            
            if (userId != null) {
                response.put("success", true);
                response.put("message", "获取用户信息成功");
                response.put("userId", userId);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Token无效或已过期");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息异常：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


}
