package com.ado.serial.pro.core.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class LoginUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel password=new JLabel("�����������:");
	
	private JLabel title=new JLabel("Leon-Tony����");
	
	private JPasswordField pwdField=new JPasswordField(30);
	
	private JButton jButton = new JButton("ȷ��") ;
	
	public LoginUI() {
		this.setTitle("��ӭҳ��");
		this.setSize(500, 500);
		this.setVisible(true);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		title.setBounds(180, 20, 160, 60);
		title.setFont(new Font("����", Font.BOLD, 25));
		
		password.setBounds(60, 100, 120, 60);
		password.setFont(new Font("����", Font.BOLD, 15));
		
		pwdField.setBounds(60, 160, 150, 40);
		
		jButton.setBounds(240, 160, 80, 40);
		jButton.setBackground(Color.YELLOW);
		
		this.add(title) ;
		this.add(pwdField) ;
		this.add(password) ;
		this.add(jButton) ;
		
		jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("deprecation")
				String pwd = pwdField.getText() ;
				if(pwd.equals("123456")) {
					LoginUI.this.setVisible(false);
					JOptionPane.showMessageDialog(new JFrame(), "������ҳ.....");
					new HomeUI() ;	
				}else {
					JOptionPane.showMessageDialog(new JFrame(), "��֤����,������.....");
					pwdField.setText("");
				}
			}
		});
	}
	
	public static void main(String[] args) {
		new LoginUI() ;
	}

}
