package com.hpaz.translator.ui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.hpaz.translator.grafcetelements.Project;
import com.hpaz.translator.ui.constants.ConfigConstants;

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

	JPanel scrollPanelSignalContainer;
	JPanel scrollPanelTempContainer;
	JPanel scrollPanelCounterContainer;

	Map<String, String> variablesTypesMap = new HashMap<String, String>();
	int numberOfVariables = 0;

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
		panelSignal.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Se\u00F1ales ",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSignal.setBounds(10, 11, 663, 180);
		panelSignal.setLayout(null);
		contentPane.add(panelSignal);

		scrollPanelSignalContainer = new JPanel();
		scrollPanelSignalContainer.setLayout(new BoxLayout(scrollPanelSignalContainer, BoxLayout.Y_AXIS));
		scrollPanelSignalContainer.setBounds(10, 21, 643, 149);

		createSignalViewsWithAlgorithmElements();

		JScrollPane scrollPanelSignal = new JScrollPane(scrollPanelSignalContainer);
		scrollPanelSignal.setBounds(10, 21, 643, 149);
		scrollPanelSignal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanelSignal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panelSignal.add(scrollPanelSignal);

		JPanel panelTemp = new JPanel();
		panelTemp.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Temporizadores ",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelTemp.setBounds(10, 212, 663, 180);
		contentPane.add(panelTemp);
		panelTemp.setLayout(null);

		scrollPanelTempContainer = new JPanel();
		scrollPanelTempContainer.setLayout(new BoxLayout(scrollPanelTempContainer, BoxLayout.Y_AXIS));
		scrollPanelTempContainer.setBounds(10, 21, 643, 149);

		createTimersViewsWithAlgorithmElements();

		JScrollPane scrollPanelTemp = new JScrollPane(scrollPanelTempContainer);
		scrollPanelTemp.setBounds(10, 21, 643, 149);
		scrollPanelTemp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanelTemp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panelTemp.add(scrollPanelTemp);

		JPanel paneCounter = new JPanel();
		paneCounter.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), " Contadores ",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		paneCounter.setBounds(10, 414, 663, 180);
		contentPane.add(paneCounter);
		paneCounter.setLayout(null);

		scrollPanelCounterContainer = new JPanel();
		scrollPanelCounterContainer.setLayout(new BoxLayout(scrollPanelCounterContainer, BoxLayout.Y_AXIS));
		scrollPanelCounterContainer.setBounds(10, 21, 643, 149);

		createCountersViewsWithAlgorithmElements();

		JScrollPane scrollPanelCounter = new JScrollPane(scrollPanelCounterContainer);
		scrollPanelCounter.setBounds(10, 21, 643, 149);
		scrollPanelCounter.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanelCounter.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneCounter.add(scrollPanelCounter);

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

	private void createCountersViewsWithAlgorithmElements() {
		LinkedList<String> counterNamesList = Project.getProject().getListCountersUI();

		numberOfVariables += counterNamesList.size();

		for (String string : counterNamesList) {
			Panel panelCount = new Panel();
			panelCount.setBounds(0, 0, 663, 25);
			panelCount.setName(string);
			scrollPanelCounterContainer.add(panelCount);

			JLabel lblCounterName = new JLabel(string);
			panelCount.add(lblCounterName);

			Choice choiceCounterName = new Choice();
			choiceCounterName.addItem("");
			choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_CTD);
			choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_CTU);
			choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_CTUD);
			choiceCounterName.addItem(ConfigConstants.SIGNAL_COUNTER_TYPE_NOCOUNTER);
			panelCount.add(choiceCounterName);

			choiceCounterName.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					variablesTypesMap.put(string, choiceCounterName.getSelectedItem());
				}
			});
		}

		// TODO Auto-generated method stub

	}

	private void createTimersViewsWithAlgorithmElements() {
		LinkedList<String> timerNamesList = Project.getProject().getListTimersUI();

		numberOfVariables += timerNamesList.size();

		for (String string : timerNamesList) {
			Panel panelTemp = new Panel();
			panelTemp.setName(string);
			panelTemp.setBounds(0, 0, 663, 25);
			scrollPanelTempContainer.add(panelTemp);

			JLabel lblTempName = new JLabel(string);
			panelTemp.add(lblTempName);

			Choice choiceTempName = new Choice();
			choiceTempName.addItem("");
			choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_TON);
			choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_TOFF);
			choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_TP);
			choiceTempName.addItem(ConfigConstants.SIGNAL_TEMP_TYPE_NOTEMP);

			choiceTempName.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					variablesTypesMap.put(string, choiceTempName.getSelectedItem());
				}
			});
			panelTemp.add(choiceTempName);
		}
		// TODO Auto-generated method stub

	}

	private void createSignalViewsWithAlgorithmElements() {
		LinkedList<String> signalNamesList = Project.getProject().generateSignals();

		for (String string : signalNamesList) {
			Panel panelSignal = new Panel();
			// para no poner los tem y cont
			Pattern pat = Pattern.compile(
					"^Temp.*/X[0-9]./[0-9].*|^Cont.*==[0-9]$|^Cont.*=[0-9]$|^Cont.*=Cont.*\\+[0-9]|^Cont.*=Cont.*\\-[0-9]$");
			Matcher mat = pat.matcher(string);

			if (!mat.matches()) {
				numberOfVariables++;
				panelSignal.setBounds(0, 0, 663, 25);
				panelSignal.setName(string);
				scrollPanelSignalContainer.add(panelSignal);

				JLabel lblSignalName = new JLabel(string);
				panelSignal.add(lblSignalName);
				lblSignalName.setBounds(21, 41, 46, 14);

				Choice choiceSignalDataType = new Choice();
				choiceSignalDataType.addItem("");
				choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_INPUT);
				choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_OUTPUT);
				choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_CONSTANT);
				choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_MEMORY);
				choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_SYSTEM);
				choiceSignalDataType.addItem(ConfigConstants.SIGNAL_DATA_TYPE_NOSIGNAL);
				panelSignal.add(choiceSignalDataType);

				Choice choiceSignalVariableType = new Choice();
				choiceSignalVariableType.addItem("");
				choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_BOOLEAN);
				choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_BYTE);
				choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_WORD);
				choiceSignalVariableType.addItem(ConfigConstants.SIGNAL_VARIABLE_TYPE_DOUBLEWORD);
				panelSignal.add(choiceSignalVariableType);

				choiceSignalDataType.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (choiceSignalDataType.getSelectedItem() == ConfigConstants.SIGNAL_DATA_TYPE_NOSIGNAL) {
							choiceSignalVariableType.setEnabled(false);
							choiceSignalVariableType.select(0);
							variablesTypesMap.put(string, choiceSignalDataType.getSelectedItem() + ":");
						} else {
							choiceSignalVariableType.setEnabled(true);
							if (variablesTypesMap.containsKey(string)) {
								String actualValue = variablesTypesMap.get(string);
								actualValue = actualValue.substring(actualValue.indexOf(":"), actualValue.length());
								variablesTypesMap.put(string, choiceSignalDataType.getSelectedItem() + actualValue);
							} else {
								variablesTypesMap.put(string, choiceSignalDataType.getSelectedItem() + ":");
							}
						}
					}
				});

				choiceSignalVariableType.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (variablesTypesMap.containsKey(string)) {
							String actualValue = variablesTypesMap.get(string);
							actualValue = actualValue.substring(0, actualValue.indexOf(":") + 1);
							variablesTypesMap.put(string, actualValue + choiceSignalVariableType.getSelectedItem());
						} else {
							variablesTypesMap.put(string, ":" + choiceSignalVariableType.getSelectedItem());
						}
					}
				});
			}

		}

		// TODO Auto-generated method stub

	}

	private void sendVariablesConfigToAlgorithm() {
		if (checkIfMapIsFilled()) {
			Project.getProject().setProjectVariablesFromUserInterface(variablesTypesMap);
			exportOutputToFiles();
		} else {
			JOptionPane.showMessageDialog(contentPane, "Tiene que rellenar todas las variables", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean checkIfMapIsFilled() {
		boolean isFilled = true;
		if (variablesTypesMap.keySet().size() == numberOfVariables) {
			for (String key : variablesTypesMap.keySet()) {
				String value = variablesTypesMap.get(key);
				if (value == "" || value.startsWith(":")
						|| (value.endsWith(":") && !value.equals(ConfigConstants.SIGNAL_DATA_TYPE_NOSIGNAL + ":"))) {
					isFilled = false;
					break;
				}
			}
		} else {
			isFilled = false;
		}
		return isFilled;
	}

	private void exportOutputToFiles() {
		// Genero las salidas dependiendo del software de compatibilidad
		try {// TODO exportOutputToFiles
				// Project.getProject().printProject();
			Project.getProject().print();
			
			JOptionPane.showMessageDialog(contentPane, "Se han generado los ficheros de su proyecto en la carpeta seleccionada.", "Finalizado",
					JOptionPane.OK_OPTION);

			// Project.getProject().print(compatibility);
			// Output.getSalida().exportarFicheroVG(Project.getProject().printVarGlobal(),
			// nameProject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
