package com.ado.serial.pro.core.ui;

import com.ado.serial.pro.core.service.ExecuteService;
import com.ado.serial.pro.core.service.impl.ExecuteServiceImpl;
import enums.SerialPortConfigEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


/**
 * ϵ�y���
 *
 * @author ADONAIS
 */
public class HomeUI extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JLabel serialConfig = new JLabel("串口配置");

    private JLabel serialNumber = new JLabel("串口号");

    private Choice serialNumberChoiceBox = new Choice();

    private JLabel baudRate = new JLabel("波特率");

    private Choice baudRateChoice = new Choice();

    private JLabel parityCheck = new JLabel("奇偶校验");

    private Choice parityCheckChoice = new Choice();

    private JLabel stopBits = new JLabel("停止位");

    private Choice stopBitsChoice = new Choice();

    private JLabel dataBits = new JLabel("数据位");

    private Choice dataBitsChoice = new Choice();

    private JButton startBtn = new JButton("开始监听");

    private JLabel currentStatus = new JLabel("当前状态：空闲");

    /**
     * 数据展示文本框
     */
    private JTextArea dataShow = new JTextArea();

    public HomeUI() {
        this.setTitle("主页");
        this.setSize(800, 600);
        this.setVisible(true);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        serialNumberChoiceBox.add("COM1");
        serialNumberChoiceBox.add("COM2");

        baudRateChoice.add("9600");
        baudRateChoice.add("11500");

        parityCheckChoice.add("无");
        parityCheckChoice.add("有");

        stopBitsChoice.add("1");
        stopBitsChoice.add("1.5");
        stopBitsChoice.add("2");

        dataBitsChoice.add("8");
        dataBitsChoice.add("16");


        serialConfig.setBounds(20, 20, 80, 30);
        serialConfig.setFont(new Font("楷体", Font.BOLD, 15));


        serialNumber.setBounds(20, 55, 80, 30);
        serialNumber.setFont(new Font("楷体", Font.BOLD, 15));

        serialNumberChoiceBox.setBounds(20, 90, 80, 30);

        baudRate.setBounds(150, 55, 80, 30);
        baudRate.setFont(new Font("楷体", Font.BOLD, 15));

        baudRateChoice.setBounds(150, 90, 80, 30);

        parityCheck.setBounds(290, 55, 80, 30);
        parityCheck.setFont(new Font("楷体", Font.BOLD, 15));

        parityCheckChoice.setBounds(290, 90, 80, 30);

        stopBits.setBounds(20, 125, 80, 30);
        stopBits.setFont(new Font("楷体", Font.BOLD, 15));

        stopBitsChoice.setBounds(20, 160, 80, 30);

        dataBits.setBounds(150, 125, 80, 30);
        dataBits.setFont(new Font("楷体", Font.BOLD, 15));

        dataBitsChoice.setBounds(150, 160, 80, 30);

        //显示文本框
        dataShow.setBounds(20, 240, 300, 200);

        startBtn.setBounds(300, 160, 100, 50);
        startBtn.setBackground(Color.YELLOW);

        currentStatus.setBounds(20, 200, 120, 30);
        currentStatus.setFont(new Font("楷体", Font.BOLD, 15));


        this.add(serialConfig);
        this.add(serialNumberChoiceBox);
        this.add(baudRateChoice);
        this.add(parityCheckChoice);

        this.add(serialNumber);
        this.add(baudRate);
        this.add(parityCheck);
        this.add(stopBits);
        this.add(stopBitsChoice);
        this.add(dataBits);
        this.add(dataBitsChoice);

        this.add(dataShow);
        this.add(startBtn);

        this.add(currentStatus);

        final ExecuteService executeService = new ExecuteServiceImpl(this);


        startBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String serialNumberChoiceSelected = serialNumberChoiceBox.getSelectedItem();
                String baudRateChoiceSelected = baudRateChoice.getSelectedItem();
                String parityCheckChoiceSelected = parityCheckChoice.getSelectedItem();
                String stopBitsChoiceSelected = stopBitsChoice.getSelectedItem();
                String dataBitsChoiceSelected = dataBitsChoice.getSelectedItem();
                JOptionPane.showMessageDialog(new JFrame(), "开始监听.....");
                currentStatus.setText("当前状态：监听中...");
                System.out.println("串口号为:" + serialNumberChoiceSelected + "\n波特率为:" + baudRateChoiceSelected + "\n奇偶校验:" + parityCheckChoiceSelected +
                        "\n停止位:" + stopBitsChoiceSelected + "\n数据位:" + dataBitsChoiceSelected);

//                startBtn.

                Map<String, String> map = new HashMap<String, String>();
                map.put(SerialPortConfigEnum.PARITY_CHECK.getCode(), parityCheckChoiceSelected);
                map.put(SerialPortConfigEnum.SERIAL_NUMBER.getCode(), serialNumberChoiceSelected);
                map.put(SerialPortConfigEnum.BAUD_RATE.getCode(), baudRateChoiceSelected);
                map.put(SerialPortConfigEnum.STOP_BITS.getCode(), stopBitsChoiceSelected);
                map.put(SerialPortConfigEnum.DATA_BITS.getCode(), dataBitsChoiceSelected);

                executeService.dealData(map);


            }
        });


    }

    public void changeDataShow(Character msg){
            dataShow.append(msg.toString());
    }


//	public static void main(String[] args) {
//		new HomeUI() ;
//	}
}
