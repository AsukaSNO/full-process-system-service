# API测试脚本
Write-Host "=== 全流程系统服务 API 测试 ===" -ForegroundColor Green
Write-Host ""

# 测试自定义健康检查接口
Write-Host "1. 测试自定义健康检查接口:" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/health/checkHealth" -Method GET -ContentType "application/json"
    Write-Host "   状态: $($response.status)" -ForegroundColor Green
    Write-Host "   消息: $($response.message)" -ForegroundColor Green
    Write-Host "   时间戳: $($response.timestamp)" -ForegroundColor Green
    Write-Host "   版本: $($response.version)" -ForegroundColor Green
} catch {
    Write-Host "   错误: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试Spring Boot Actuator健康检查
Write-Host "2. 测试Spring Boot Actuator健康检查:" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET -ContentType "application/json"
    Write-Host "   状态: $($response.status)" -ForegroundColor Green
} catch {
    Write-Host "   错误: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试应用信息
Write-Host "3. 测试应用信息:" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/actuator/info" -Method GET -ContentType "application/json"
    Write-Host "   应用信息: $($response | ConvertTo-Json -Depth 1)" -ForegroundColor Green
} catch {
    Write-Host "   错误: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Write-Host "=== 测试完成 ===" -ForegroundColor Green 