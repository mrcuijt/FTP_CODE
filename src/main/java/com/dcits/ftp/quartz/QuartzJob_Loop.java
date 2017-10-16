package com.dcits.ftp.quartz;

import com.dcits.ftp.ftpUtils.ConfigMessage;
import com.dcits.ftp.ftpUtils.FtpDownload;
import com.dcits.ftp.ftpUtils.FtpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;
import java.util.Date;

import static com.dcits.ftp.ftpUtils.FtpUpload.upLoadFromProduction;

/**
 * Describe:
 * Company: ����������Ϣϵͳ���޹�˾
 * Version: v1.0
 * User:cuihld
 * Date:2017/9/14 14:42
 */


public class QuartzJob_Loop implements Job {
    private static Log logger = LogFactory.getLog(QuartzJob_Loop.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("��ʱ����ʼִ��" + new Date());
        try {
            ConfigMessage.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = FtpUtils.getFileName();
        String dname = (String) FtpUtils.timeManage().get("mdrdate");
        boolean  flag=connect_ftp(ConfigMessage.getSW_HOST(), ConfigMessage.getSW_PORT(), ConfigMessage.getSW_USERNAME(), ConfigMessage.getSW_PASSWORD(), dname, filename);
       if (!flag){
           logger.info("�ļ������ڣ�����Ϊ��ͬ�����ļ�");
           FtpUtils.createDir(ConfigMessage.getPathMemory() + "/" + FtpUtils.timeManage().get("mdrdate"));
        //�����ļ�������
        FtpDownload.downloadFile(ConfigMessage.getYL_HOST(), ConfigMessage.getYL_PORT(), ConfigMessage.getYL_USERNAME(), ConfigMessage.getYL_PASSWORD(), dname, filename, ConfigMessage.getPathMemory() + "/" + dname);
        //�ϴ��ļ���ftp������
        upLoadFromProduction(ConfigMessage.getSW_HOST(), ConfigMessage.getSW_PORT(), ConfigMessage.getSW_USERNAME(), ConfigMessage.getSW_PASSWORD(), dname, filename, ConfigMessage.getPathMemory() + "/" + dname + "/" + filename);
       }
    }

    public static boolean connect_ftp(String url,// FTP������hostname
                                     int port,// FTP�������˿�
                                     String username, // FTP��¼�˺�
                                     String password, // FTP��¼����
                                     String path, // FTP����������Ŀ¼
                                     String filename // �ϴ���FTP�������ϵ��ļ���
    ) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            logger.info("��ʼ��½˰��FTP���������м��");
            //����FTP������
            ftp.connect(url, port);
            //��¼FTP������
            ftp.login(username, password);
            //��֤FTP�������Ƿ��¼�ɹ�
            int replyCode = ftp.getReplyCode();
            if(!FTPReply.isPositiveCompletion(replyCode)){
                logger.info("��½˰��FTP������ʧ��");
                ftp.disconnect();
            }
            logger.info("���ڼ���ļ��Ƿ����");
            ftp.setDataTimeout(60000);       //���ô��䳬ʱʱ��Ϊ60��
            ftp.setConnectTimeout(60000);       //���ӳ�ʱΪ60��
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            FTPFile[] ftpFiles = ftp.listFiles();    //������Ŀ¼����List
            for(FTPFile file : ftpFiles){
                if(path.equalsIgnoreCase(file.getName())&&file.getSize()>0){
                    logger.info("�ļ��Ѵ���,����ͬ��");
                    success=true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe);
                }
            }
        }
        return success;
    }



}


