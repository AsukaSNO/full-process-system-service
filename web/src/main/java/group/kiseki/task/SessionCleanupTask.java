package group.kiseki.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import group.kiseki.dal.entity.UserSession;
import group.kiseki.dal.mapper.UserSessionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 会话清理定时任务
 *
 * @author Yan
 */
@Component
public class SessionCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(SessionCleanupTask.class);

    @Autowired
    private UserSessionMapper userSessionMapper;

    /**
     * 每天凌晨2点执行清理任务
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredSessions() {
        logger.info("开始清理过期会话...");
        
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // 查询过期的会话
            QueryWrapper<UserSession> queryWrapper = new QueryWrapper<>();
            queryWrapper.lt("expire_time", now);
            
            int deletedCount = userSessionMapper.delete(queryWrapper);
            
            logger.info("清理过期会话完成，共删除 {} 条记录", deletedCount);
        } catch (Exception e) {
            logger.error("清理过期会话时发生异常", e);
        }
    }

    /**
     * 每小时清理无效状态的会话
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void cleanupInvalidSessions() {
        logger.info("开始清理无效状态会话...");
        
        try {
            // 查询状态为无效的会话
            QueryWrapper<UserSession> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 0);
            
            int deletedCount = userSessionMapper.delete(queryWrapper);
            
            if (deletedCount > 0) {
                logger.info("清理无效状态会话完成，共删除 {} 条记录", deletedCount);
            }
        } catch (Exception e) {
            logger.error("清理无效状态会话时发生异常", e);
        }
    }
}
