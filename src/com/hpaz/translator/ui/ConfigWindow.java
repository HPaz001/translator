package com.hpaz.translator.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.hpaz.translator.grafcetelements.Project;
import com.hpaz.translator.ui.constants.ConfigConstants;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.ScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.Box;
import java.awt.Choice;
import java.awt.Panel;

public class ConfigWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigWindow frame = new ConfigWindow();
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
	public ConfigWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 699, 747);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelSignal = new JPanel();
		panelSignal.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Se\u00F1ales ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSignal.setBounds(10, 11, 663, 180);
		contentPane.add(panelSignal);
		panelSignal.setLayout(null);
		
		ScrollPane scrollPanelSignal = new ScrollPane();
		scrollPanelSignal.setBounds(10, 21, 643, 149);
		panelSignal.add(scrollPanelSignal);
		
		Panel panelXXXSignal = new Panel();
		panelXXXSignal.setBounds(0, 0, 300, 25);
		scrollPanelSignal.add(panelXXXSignal);
		
		JLabel lblSignalName = new JLabel("Se\u00F1al");
		panelXXXSignal.add(lblSignalName);
		lblSignalName.setBounds(21, 41, 46, 14);
		
		Choice choiceSignalDataType = new Choice();
		choiceSignalDataType.addItem("");
		choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_INPUT);
		choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_OUTPUT);
		choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_CONSTANT);
		choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_MEMORY);
		choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_SYSTEM);
		choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_NOSIGNAL);
		panelXXXSignal.add(choiceSignalDataType);
		
		Choice choiceSignalVariableType = new Choice();
		choiceSignalVariableType.addItem("");
		choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_BOOLEAN);
		choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_BYTE);
		choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_WORD);
		choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_DOUBLEWORD);
		panelXXXSignal.add(choiceSignalVariableType);
		
		JPanel panelTemp = new JPanel();
		panelTemp.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Temporizadores ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelTemp.setBounds(10, 212, 663, 180);
		contentPane.add(panelTemp);
		panelTemp.setLayout(null);
		
		ScrollPane scrollPanelTemp = new ScrollPane();
		scrollPanelTemp.setBounds(10, 21, 643, 149);
		panelTemp.add(scrollPanelTemp);
		
		Panel panelXXXTemp = new Panel();
		panelXXXTemp.setBounds(0, 0, 300, 25);
		scrollPanelTemp.add(panelXXXTemp);
		
		JLabel lblTempName = new JLabel("Temporizador");
		panelXXXTemp.add(lblTempName);
		
		Choice choiceTempName = new Choice();
		choiceTempName.addItem("");
		choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_TON);
		choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_TOFF);
		choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_TP);
		choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_NOTEMP);
		panelXXXTemp.add(choiceTempName);
		
		JPanel paneCounter = new JPanel();
		paneCounter.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Contadores ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		paneCounter.setBounds(10, 414, 663, 180);
		contentPane.add(paneCounter);
		paneCounter.setLayout(null);
		
		ScrollPane scrollPanelCounter = new ScrollPane();
		scrollPanelCounter.setBounds(10, 21, 643, 149);
		paneCounter.add(scrollPanelCounter);
		
		Panel panelXXXCount = new Panel();
		panelXXXCount.setBounds(0, 0, 300, 25);
		scrollPanelCounter.add(panelXXXCount);
		
		JLabel lblCounterName = new JLabel("Contador");
		panelXXXCount.add(lblCounterName);
		
		Choice choiceCounterName = new Choice();
		choiceCounterName.addItem("");
		choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_CTD);
		choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_CTU);
		choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_CTUD);
		choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_NOCOUNTER);
		panelXXXCount.add(choiceCounterName);
		
		JButton btnFinalizar = new JButton("Finalizar");
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendVariablesConfigToAlgorithm();
			}
		});
		btnFinalizar.setFont(new Font("Verdana", Font.PLAIN, 18));
		btnFinalizar.setBounds(248, 630, 186, 51);
		contentPane.add(btnFinalizar);
		
	}
	
	private void sendVariablesConfigToAlgorithm() {
		exportOutputToFiles();
	}
	
	private void exportOutputToFiles(){
		// Genero las salidas dependiendo del software de compatibilidad
		try {//TODO ESTO DESAPARECE SE LE LLEMARA DESDE LA INTERFACE A LO QUE ESTA DENTRO DEL TRY
			//Project.getProject().printProject();
			Project.getProject().print();
		
			Project.getProject().generateSignals();
			// Project.getProject().print(compatibility);
			// Output.getSalida().exportarFicheroVG(Project.getProject().printVarGlobal(),
			// nameProject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
