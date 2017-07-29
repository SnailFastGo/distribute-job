package com.xxl.job.core.executor;

import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.impl.ExecutorBizImpl;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.rpc.netcom.NetComServerFactory;
import com.xxl.job.core.thread.ExecutorRegistryThread;
import com.xxl.job.core.thread.JobThread;
import com.xxl.job.core.thread.TriggerCallbackThread;
import com.xxl.job.core.util.AdminApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @project Job执行器
 * @file XxlJobExecutor.java 创建时间:2017年7月28日下午5:17:01
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class XxlJobExecutor implements ApplicationContextAware, ApplicationListener {
    
    /**
     * @description 日志
     * @value value:logger
     */
    private static final Logger logger = LoggerFactory.getLogger(XxlJobExecutor.class);

    /**
     * @description 执行器IP
     * @value value:ip
     */
    private String ip;
    
    /**
     * @description 执行器端口号
     * @value value:port
     */
    private int port = 9999;
    
    /**
     * @description 执行器名字
     * @value value:appName
     */
    private String appName;
    
    /**
     * @description 调度器地址
     * @value value:adminAddresses
     */
    private String adminAddresses;
    
    /**
     * @description 日志路径
     * @value value:logPath
     */
    public static String logPath;

    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }
    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    // ---------------------------------- job server ------------------------------------
    private NetComServerFactory serverFactory = new NetComServerFactory();
    
    public void start() throws Exception {
        // 根据配置文件初始化调度器地址列表
        AdminApiUtil.init(adminAddresses);

        // 绑定服务接口和服务类
        NetComServerFactory.putService(ExecutorBiz.class, new ExecutorBizImpl());
        
        //启动Jetty服务器作为RPC调用服务器
        serverFactory.start(port, ip, appName);

        // 以守护线程的形式启动触发器回调线程
        TriggerCallbackThread.getInstance().start();
    }
    
    public void destroy(){
        // 1、executor registry thread stop
        ExecutorRegistryThread.getInstance().toStop();

        // 2、executor stop
        serverFactory.destroy();

        // 3、job thread repository destory
        if (JobThreadRepository.size() > 0) {
            for (Map.Entry<Integer, JobThread> item: JobThreadRepository.entrySet()) {
                JobThread jobThread = item.getValue();
                jobThread.toStop("Web容器销毁终止");
                jobThread.interrupt();

            }
            JobThreadRepository.clear();
        }

        // 4、trigger callback thread stop
        TriggerCallbackThread.getInstance().toStop();
    }

    // ---------------------------------- init job handler ------------------------------------
    public static ApplicationContext applicationContext;
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        XxlJobExecutor.applicationContext = applicationContext;

        // init job handler action
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(JobHander.class);

        if (serviceBeanMap!=null && serviceBeanMap.size()>0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                if (serviceBean instanceof IJobHandler){
                    String name = serviceBean.getClass().getAnnotation(JobHander.class).value();
                    IJobHandler handler = (IJobHandler) serviceBean;
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException("xxl-job jobhandler naming conflicts.");
                    }
                    registJobHandler(name, handler);
                }
            }
        }
	}

    // ---------------------------------- destory job executor ------------------------------------
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ContextClosedEvent){
            // TODO
        }
    }

    // ---------------------------------- job handler repository
    private static ConcurrentHashMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();
    public static IJobHandler registJobHandler(String name, IJobHandler jobHandler){
        logger.info("xxl-job register jobhandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }
    public static IJobHandler loadJobHandler(String name){
        return jobHandlerRepository.get(name);
    }

    // ---------------------------------- job thread repository
    private static ConcurrentHashMap<Integer, JobThread> JobThreadRepository = new ConcurrentHashMap<Integer, JobThread>();
    public static JobThread registJobThread(int jobId, IJobHandler handler, String removeOldReason){
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();
        logger.info(">>>>>>>>>>> xxl-job regist JobThread success, jobId:{}, handler:{}", new Object[]{jobId, handler});

        JobThread oldJobThread = JobThreadRepository.put(jobId, newJobThread);	// putIfAbsent | oh my god, map's put method return the old value!!!
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }

        return newJobThread;
    }
    public static void removeJobThread(int jobId, String removeOldReason){
        JobThread oldJobThread = JobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
    }
    public static JobThread loadJobThread(int jobId){
        JobThread jobThread = JobThreadRepository.get(jobId);
        return jobThread;
    }

}
