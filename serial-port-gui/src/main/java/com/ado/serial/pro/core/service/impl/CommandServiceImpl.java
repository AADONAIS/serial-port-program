package com.ado.serial.pro.core.service.impl;



import com.ado.serial.pro.core.service.CommandService;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * CommandUtils
 *
 * @author Leon
 * @version 1.0
 * @date 2021/2/23 11:54
 **/
public class CommandServiceImpl implements CommandService {

    @Override
    public void exec(String[] command){
        Runtime run =Runtime.getRuntime();
        try {
            Process p = run.exec(command);
//            InputStream ins= p.getInputStream();
            InputStream ins= p.getErrorStream();
            new Thread(new inputStreamThread(ins)).start();
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class inputStreamThread implements Runnable{
        private InputStream ins = null;
        private BufferedReader bfr = null;
        public inputStreamThread(InputStream ins){
            this.ins = ins;
            try {
                this.bfr = new BufferedReader(new InputStreamReader(ins,"gb2312"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            String line = null;
            byte[] b = new byte[2];
            int num = 0;
            try {
//                String outStr ="";
//                while(ins.available()>0){
//                    StringBuilder stringBuilder = new StringBuilder();
//                    while (true) {
//                        int read = ins.read(b);
//                        String tempStr = new String(b, "gb2312");
////                        byte[] bytes = tempStr.getBytes(StandardCharsets.UTF_8);
////                        tempStr = new String(bytes, StandardCharsets.UTF_8);
//
//                        stringBuilder.append(tempStr);
//                        if (stringBuilder.toString().contains("\r\n")){
//                            outStr=stringBuilder.toString();
//                            break;
//                        }
//
//                    }
//                    System.out.print(outStr);
//                }
                char ch;
                while ((num = bfr.read()) != -1) {
                    ch = (char) num;
                    System.out.print(ch);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
