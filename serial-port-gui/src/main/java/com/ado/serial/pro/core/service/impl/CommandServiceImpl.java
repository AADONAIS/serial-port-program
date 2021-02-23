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
            InputStream inputStream= p.getInputStream();
            InputStream errorStream= p.getErrorStream();
            new Thread(new inputStreamThread(inputStream)).start();
            new Thread(new inputStreamThread(errorStream)).start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class inputStreamThread implements Runnable{
        private InputStream ins = null;
        public inputStreamThread(InputStream ins){
            this.ins = ins;
        }
        @Override
        public void run() {
            int num = 0;
            try {
                BufferedReader bfr=new BufferedReader(new InputStreamReader(ins,"gb2312"));

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
