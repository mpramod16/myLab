package com.ahstores.app.dynamic.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.ahstores.app.model.SchedulerConfig;
import com.ahstores.app.repository.SchedulerConfigRepository;
import com.ahstores.app.service.SchedulerConfigService;
import com.ahstores.app.util.QueueEventListenerTask;
import com.ahstores.app.util.Task;
import com.ahstores.app.util.TopicEventListenerTask;
@Configuration 
public class ScheduledConfiguration implements SchedulingConfigurer {
	private static final Logger LOG = LoggerFactory.getLogger(ScheduledConfiguration.class);
	
	private TaskScheduler taskScheduler; 
	
	@Autowired
    private SchedulerConfigService schedulerConfigService;
	@Autowired 
	private Task task;
	@Autowired 
	private QueueEventListenerTask queueEventListenerTask;
	@Autowired 
	private TopicEventListenerTask topicEventListenerTask;
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    	ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);// Set the pool of threads
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize(); 
        LOG.info("loading threadpoolTaskScheduler...");
        this.taskScheduler=threadPoolTaskScheduler;
        //Add EventListener tasks for subscription on Queue & Topic
        //taskScheduler.scheduleAtFixedRate(queueEventListenerTask, 1000);
        taskScheduler.scheduleAtFixedRate(topicEventListenerTask, 1000);
        List<SchedulerConfig> schedulerConfigList=schedulerConfigService.getAllSchedulerConfig();
        LOG.info("schedulerConfigList>>>"+schedulerConfigList.isEmpty());
        
        if(!schedulerConfigList.isEmpty()) {
        for (SchedulerConfig schedulerConfig : schedulerConfigList) {
        	String taskMessage="Task:"+schedulerConfig.getJobName()+" , iterating after every "+schedulerConfig.getJobRefreshRateInMillis()+" milliseconds";
        	//Task task = new Task("Task:"+schedulerConfig.getJobName()+" , iterating after every "+schedulerConfig.getJobRefreshRateInMillis()+" milliseconds"); // New object of task
         	task.setTask(taskMessage);  
        	taskScheduler.scheduleAtFixedRate(task,schedulerConfig.getJobRefreshRateInMillis());
		}
        }

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    	
        
        
        // this will be used in later part of the article during refreshing the cron expression dynamically
         /* this.taskScheduler=threadPoolTaskScheduler;
          * for (int i = 1; i <= 5; i++) {
       	  Task task = new Task("Task " + i + ", iterating after every " + i + " minute"); // New object of task
       	  taskScheduler.scheduleAtFixedRate(task, i * 60000);
       	 
       	  *  //Alternate code for setting the scheduler timing
       	  *  taskScheduler.schedule(task, new Trigger() {

               @Override

               public Date nextExecutionTime(TriggerContext triggerContext) {

                String cronExp = "0/5 * * * * ?";// Can be pulled from a db .

                return new CronTrigger(cronExp).nextExecutionTime(triggerContext);

             }});
       	 }
		
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
       	 */
         

	}

}
