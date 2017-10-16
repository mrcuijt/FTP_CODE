package com.dcits.ftp.ftpUtils;
import java.io.*;
import java.util.*;

/**
 * Describe: ftp������
 * Company: ����������Ϣϵͳ���޹�˾
 * Version: v1.0
 * User:xiudx
 * Date:2017/9/14 9:31
 */
public class FtpUtils {


    // ����Ŀ¼
    public static void createDir(String destDirName) {
        File dir = new File(destDirName);
        if (!dir.exists()) {
            //�������򴴽�Ŀ¼
            dir.mkdir();
        }
    }

    /*
        ����ϵͳ��ǰʱ��õ�ǰһ���ǰ����������������Ϣ
     */
    public static Map timeManage() {
        Map timemap = new HashMap();
        Date dNow = new Date();   //��ǰʱ��
        Calendar calendar = Calendar.getInstance(); //�õ�����
        calendar.setTime(dNow);//�ѵ�ǰʱ�丳������
        String[] defaultStartDate=new String[2];
        for(int i=0;i<2;i++){
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //����Ϊǰһ��
            String fileyear=String.valueOf(calendar.get(Calendar.YEAR));//��ȡ���
            String filemonth=String.valueOf(calendar.get(Calendar.MONTH)+1);//��ȡ���
            if(filemonth.length()==1){
                filemonth="0"+filemonth;
            }
            String filedate=String.valueOf(calendar.get(Calendar.DATE));
            defaultStartDate[i]=fileyear+filemonth+filedate;
        }
        timemap.put("mdrdate", defaultStartDate[0]);
        timemap.put("filedate", defaultStartDate[1].substring(2));
        return timemap;
    }


    //���ݹ���õ��ļ��������ɷ���
    public static String getFileName() {
        String filename = "INO" + "" + timeManage().get("filedate") + "00TPCPM";
        filename = filename + ConfigMessage.getSkyUnionPayReportSdjgdm();
        return filename;
    }
}

