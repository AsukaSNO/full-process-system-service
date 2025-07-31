package com.example.controller;

import com.example.model.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 健康检查控制器
 * 
 * @author system
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * 健康检查接口
     * 
     * @return 健康状态响应
     */
    @GetMapping("/checkHealth")
    public ResponseEntity<HealthResponse> checkHealth() {
        HealthResponse response = new HealthResponse();
        response.setStatus("UP");
        response.setMessage("系统运行正常");
        response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        response.setVersion("1.0.0");
        
        return ResponseEntity.ok(response);
    }
} 