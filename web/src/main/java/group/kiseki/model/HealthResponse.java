package group.kiseki.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 健康检查响应模型
 * 
 * @author Yan
 * @version 1.0.0
 */
public class HealthResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("version")
    private String version;

    // 默认构造函数
    public HealthResponse() {
    }

    // 带参数的构造函数
    public HealthResponse(String status, String message, String timestamp, String version) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.version = version;
    }

    // Getter和Setter方法
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "HealthResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
} 