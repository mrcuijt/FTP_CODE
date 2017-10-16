package com.dcits.ftp.ftpUtils;

/**
 * Created by zhangyial on 2017/9/15.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUpload {
    private static Log logger = LogFactory.getLog(FtpUpload.class);
    /**
     * Description: ��FTP�������ϴ��ļ�
     *
     * @param url      FTP������hostname
     * @param port     FTP�������˿�
     * @param username FTP��¼�˺�
     * @param password FTP��¼����
     * @param path     FTP����������Ŀ¼
     * @param filename �ϴ���FTP�������ϵ��ļ���
     * @param input    ������
     * @return �ɹ�����true�����򷵻�false *
     * @Version 1.0
     */
    public static boolean uploadFile(String url,// FTP������hostname
                                     int port,// FTP�������˿�
                                     String username, // FTP��¼�˺�
                                     String password, // FTP��¼����
                                     String path, // FTP����������Ŀ¼
                                     String filename, // �ϴ���FTP�������ϵ��ļ���
                                     InputStream input // ������
    ) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("GBK");
        try {
            logger.info("��ʼ��½˰��FTP������");
            int reply;
            ftp.connect(url, port);// ����FTP������
            // �������Ĭ�϶˿ڣ�����ʹ��ftp.connect(url)�ķ�ʽֱ������FTP������
            ftp.login(username, password);// ��¼
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.info("��½˰��FTP������ʧ��");
                ftp.disconnect();
                return success;
            }
            logger.info("��½˰��FTP�������ɹ���");
            logger.info("��ʼ�ϴ��ļ�...");
            ftp.setDataTimeout(60000);       //���ô��䳬ʱʱ��Ϊ60��
            ftp.setConnectTimeout(60000);       //���ӳ�ʱΪ60��
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.makeDirectory(path);
            ftp.changeWorkingDirectory(path);
            ftp.storeFile(filename, input);
            logger.info("�ϴ��ļ��ɹ���");
            input.close();
            ftp.logout();
            success = true;
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

    /**
     * �������ļ��ϴ���FTP�������� *
     */
    public static void upLoadFromProduction(String url,// FTP������hostname
                                            int port,// FTP�������˿�
                                            String username, // FTP��¼�˺�
                                            String password, // FTP��¼����
                                            String path, // FTP����������Ŀ¼
                                            String filename, // �ϴ���FTP�������ϵ��ļ���
                                            String orginfilename // �������ļ���
    ) {
        try {
            FileInputStream in = new FileInputStream(new File(orginfilename));
            boolean flag = uploadFile(url, port, username, password, path, filename, in);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

}