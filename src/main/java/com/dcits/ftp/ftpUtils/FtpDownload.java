package com.dcits.ftp.ftpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public  class FtpDownload {
    private static Log logger = LogFactory.getLog(FtpDownload.class);
    /**
     * �����ļ�
     * @param hostname FTP��������ַ
     * @param port  FTP�������˿ں�
     * @param username  FTP��¼�ʺ�
     * @param password  FTP��¼����
     * @param pathname  FTP�������ļ�Ŀ¼
     * @param filename  �ļ�����
     * @param localpath ���غ���ļ�·��
     * @return
     */
    public static boolean downloadFile(String hostname, int port, String username, String password, String pathname, String filename, String localpath){
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            logger.info("��ʼ��½����FTP������");
            //����FTP������
            ftpClient.connect(hostname, port);
            //��¼FTP������
            ftpClient.login(username, password);
            //��֤FTP�������Ƿ��¼�ɹ�
            int replyCode = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(replyCode)){
                logger.info("��½����FTP������ʧ��");
                ftpClient.disconnect();
                return flag;
            }
            logger.info("��½����FTP�������ɹ�!");
            //�л�FTPĿ¼
            logger.info("��ʼ�����ļ�...");
            ftpClient.changeWorkingDirectory(pathname);    //�л�Ŀ¼������ƥ���Ŀ¼
            FTPFile[] ftpFiles = ftpClient.listFiles();    //������Ŀ¼����List
            for(FTPFile file : ftpFiles){
                if(filename.equalsIgnoreCase(file.getName())){      //�������ڲ���ƥ�������ļ�
                    File localFile = new File(localpath + "/" + file.getName());
                    OutputStream os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);          //��ƥ�䵽�������ļ����OutputStream��д�뵽ָ����os��
                    os.close();
                    logger.info("�����ļ��ɹ���");
                }else{
                    logger.info("����FTP�����������ڶ����ļ������ֶ�����!");
                }
            }
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally{
            if(ftpClient.isConnected()){
                try {
                    ftpClient.logout();
                } catch (IOException e) {
                       logger.error(e);
                }
            }
        }
        return flag;
    }

}
	
