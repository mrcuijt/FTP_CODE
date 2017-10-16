package com.dcits.ftp;

import com.dcits.ftp.ftpUtils.ConfigMessage;
import com.dcits.ftp.quartz.QuartzJob;
import com.dcits.ftp.quartz.QuartzJob_Loop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import sun.applet.Main;
import java.io.*;


/**
 * Describe: ����log��ӡ
 * Company: ����������Ϣϵͳ���޹�˾
 * Version: v1.0
 * User:xiudx
 * Date:2017/9/14 9:31
 */
public class Main_DS {
    private static Log logger = LogFactory.getLog(Main.class);
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    public static String JOB_NAME = "QuartzJob";
    public static String TRIGGER_NAME = "trigger";
    public static String JOB_GROUP_NAME = "group1";
    public static String TRIGGER_GROUP_NAME = "group1";

    public static void addJob(String jobName, String jobGroupName,
                              String triggerName, String triggerGroupName, Class jobClass, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // �������������飬����ִ����
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

            // ������
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // ��������,��������
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // ������ʱ���趨
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // ����Trigger����
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // ������������JobDetail��Trigger
            sched.scheduleJob(jobDetail, trigger);
            sched.start();
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
    }

    //��������
    public static void ds() {
        try {
            logger.info("ϵͳ��ʼ�����ÿ�ʼ");
            ConfigMessage.init();
            String hour = ConfigMessage.getScheduleTime();
            String value = "0  0 " + hour + " * * * ?";
            logger.info("ϵͳÿ��" + hour + "��Ϊ��ͬ����������");
            addJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, QuartzJob.class, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //������������ÿ��Сʱ
    public static void loop() {
        try {
            logger.info("���ó�ʼ����ʼ");
            JOB_NAME = "QuartzJob_Loop";
            TRIGGER_NAME = "trigger";
            JOB_GROUP_NAME = "group2";
            TRIGGER_GROUP_NAME = "group2";
            ConfigMessage.init();
            String hour = ConfigMessage.getScheduleLoopTime();
            String value = "0 0 */" + hour + " * * ?";
            logger.info("ϵͳÿ" + hour + "СʱΪ���������Ƿ����");
            addJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, QuartzJob_Loop.class, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {
               ConfigMessage.init();
               if (ConfigMessage.getLOOP()){
                   loop();
               }else {
                   ds();
               }
    }
}
