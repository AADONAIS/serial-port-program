package com.ado.serial.pro.core.service.impl;



import com.ado.serial.pro.core.service.CommandService;
import com.ado.serial.pro.core.ui.HomeUI;

import java.io.*;

/**
 * CommandUtils
 *
 * @author Leon
 * @version 1.0
 * @date 2021/2/23 11:54
 **/
public class CommandServiceImpl implements CommandService {

    private HomeUI homeUI;

    public CommandServiceImpl(HomeUI homeUI) {
        this.homeUI=homeUI;

    }

    @Override
    public void exec(String[] command){
        Runtime run =Runtime.getRuntime();
        try {
            Process p = run.exec(command);
            InputStream inputStream= p.getInputStream();
            InputStream errorStream= p.getErrorStream();
            new Thread(new inputStreamThread(inputStream,homeUI)).start();
            new Thread(new inputStreamThread(errorStream,homeUI)).start();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class inputStreamThread implements Runnable{

        private HomeUI homeUI;

        private InputStream ins = null;

        public inputStreamThread(InputStream ins, HomeUI homeUI){
            this.ins = ins;
            this.homeUI=homeUI;

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
                    homeUI.changeDataShow(ch);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
