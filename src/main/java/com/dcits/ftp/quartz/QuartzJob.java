package com.dcits.ftp.quartz;

import com.dcits.ftp.ftpUtils.ConfigMessage;
import com.dcits.ftp.ftpUtils.FtpDownload;
import com.dcits.ftp.ftpUtils.FtpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.Date;

import static com.dcits.ftp.ftpUtils.FtpUpload.upLoadFromProduction;

/**
 * Describe:
 * Company: ����������Ϣϵͳ���޹�˾
 * Version: v1.0
 * User:cuihld
 * Date:2017/9/14 14:42
 */


public class QuartzJob implements Job {
    private static Log logger = LogFactory.getLog(QuartzJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("��ʱ����ʼִ��" + new Date());
        try {
            ConfigMessage.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = FtpUtils.getFileName();
        String dname = (String) FtpUtils.timeManage().get("mdrdate");
        //�ڱ��ش����ļ���
        FtpUtils.createDir(ConfigMessage.getPathMemory() + "/" + FtpUtils.timeManage().get("mdrdate"));
        //�����ļ�������
        FtpDownload.downloadFile(ConfigMessage.getYL_HOST(), ConfigMessage.getYL_PORT(), ConfigMessage.getYL_USERNAME(), ConfigMessage.getYL_PASSWORD(), dname, filename, ConfigMessage.getPathMemory() + "/" + dname);
        //�ϴ��ļ���ftp������
        upLoadFromProduction(ConfigMessage.getSW_HOST(), ConfigMessage.getSW_PORT(), ConfigMessage.getSW_USERNAME(), ConfigMessage.getSW_PASSWORD(), dname, filename, ConfigMessage.getPathMemory() + "/" + dname + "/" + filename);
        logger.info("��ʱ����ִ�н���" + new Date());

    }
}



