package com.hpaz.translator.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Panel;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class MainProgramWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainProgramWindow frame = new MainProgramWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainProgramWindow() {
		setResizable(false);
		setTitle("Traductor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 371);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTranslator = new JLabel("Traductor");
		lblTranslator.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblTranslator.setHorizontalAlignment(SwingConstants.CENTER);
		lblTranslator.setBounds(167, 20, 219, 106);
		contentPane.add(lblTranslator);
		
		JButton btnNewButton = new JButton("Iniciar programa");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new ConfigWindow().setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.setBounds(83, 157, 277, 60);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("Instrucciones");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//dispose();
				
				InstructionsWindow instructionsWindow = new InstructionsWindow();
				instructionsWindow.setVisible(true);
				instructionsWindow.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
			}
		});
		button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		button.setBounds(83, 243, 277, 60);
		contentPane.add(button);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("C:\\Users\\JonAnder\\Dropbox\\AATFG_Helen\\TFG_Documentacion\\Documentacion\\Imagenes\\iconoTrans106x106.png"));
		label.setBounds(63, 20, 106, 106);
		contentPane.add(label);
	}
}
