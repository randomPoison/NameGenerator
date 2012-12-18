package com.github.excaliburHisSheath;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class MainWindow extends JPanel implements Runnable {

	private FileChooserPanel inputSelector;
	private FileChooserPanel outputSelector;
	private GeneratorPanel generatorPanel;
	private CharacterPanel characterPanel;
	private JScrollPane outputPane;
	private JTextArea output;
	private NameGenerator names;
	
	private volatile boolean generating;
	private volatile int numberNamesToGenerate;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		
		JFrame main = new JFrame("NameGenerator");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(800, 400);
		main.setLocationRelativeTo(null);
		main.add(new MainWindow());
		main.setVisible(true);
	}

	public MainWindow() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		inputSelector = new FileChooserPanel(FileChooserPanel.INPUT);
		c.gridx = 0;
		c.gridy = 0;
		add(inputSelector, c);

		outputSelector = new FileChooserPanel(FileChooserPanel.OUTPUT);
		c.gridx = 0;
		c.gridy = 1;
		add(outputSelector, c);
		
		generatorPanel = new GeneratorPanel();
		c.gridx = 0;
		c.gridy = 2;
		add(generatorPanel, c);
		
		characterPanel = new CharacterPanel();
		c.gridx = 0;
		c.gridy = 3;
		add(characterPanel, c);
		
		output = new JTextArea(5, 10);
		output.setEditable(false);
		outputPane = new JScrollPane(output);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridheight = 10;
		add(outputPane, c);

		names = new NameGenerator();
	}
	
	public void inputFileSelected() {
		names.resetProbabilities();
		names.generateProbabilities(inputSelector.selectedFile());
	}

	public void start(int th) {
		if (names.isInitialized()) {
			numberNamesToGenerate = th;
			generating = true;
			(new Thread(this)).start();
		} else {
			output.append("you must first provide a source file to generate names\n");
		}
	}
	
	public void kill() {
		generating = false;
	}
	
	@Override
	public void run() {
		while (generating && numberNamesToGenerate-- > 0) {
			String name = names.generateName();
			if (!names.hasName(name)) {
				names.addName(name);
				output.append(name + "\n");
			}
		}
		generating = false;
	}
	
	public void output(File file) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
			String[] namesList = names.listOfNames();
			if (namesList.length == 0) {
				output.append("no names have been generated\n");
				return;
			}
			for (String name : namesList) {
				out.write(name + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addCharacters(char[] characters) {
		for (char c : characters) {
			if (names.recognizesCharacter(c)) {
				output.append("'" + c + "' already recognized\n");
			} else {
				names.addCharacter(c);
				output.append("'" + c + "' added to generator\n");
			}
		}
	}
	
	public void removeCharacters(char[] characters) {
		for (char c : characters) {
			if (names.recognizesCharacter(c)) {
				names.removeCharacter(c);
				output.append("'" + c + "' removed from generator\n");
			} else {
				output.append("'" + c + "' not recognized by generator\n");
			}
		}
	}
	
	public class FileChooserPanel extends JPanel implements ActionListener {

		private int type;
		private JLabel label;
		private JTextField textField;
		private JButton openButton;
		private JFileChooser chooser;
		private File file;
		
		private static final String outputText = "Enter output file name...";
		public static final int INPUT = 0;
		public static final int OUTPUT = 1;

		public FileChooserPanel(int type) {
			this.type = type;
			file = null;

			textField = new JTextField(30);
			if (type == INPUT) {
				label = new JLabel("Input: ");
				textField.setText("Select input file...");
				textField.setEditable(false);
				openButton = new JButton("Open a File...");
				chooser = new JFileChooser();
			} else if (type == OUTPUT) {
				label = new JLabel("Output: ");
				textField = new JTextField(outputText, 15);
				openButton = new JButton("Save");
			}
			add(label);
			add(textField);
			openButton.addActionListener(this);
			add(openButton);
		}

		public File selectedFile() {
			return file;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if (type == INPUT) {
				if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					String extension = chooser.getSelectedFile().getPath().substring(chooser.getSelectedFile().getPath().lastIndexOf('.'));
					if (extension.equals(".txt")) {
						file = chooser.getSelectedFile();
						textField.setText(file.getPath());
						output.append("File succesfully opened\n");
						inputFileSelected();
					} else {
						output.append("Please choose a .txt source file\n");
					}
				}
			} else if (type == OUTPUT) {
				if (textField.getText().length() > 0 && !textField.getText().equals(outputText)) {
					file = new File(textField.getText() + ".txt");
					output(file);
				} else {
					output.append("Invalid output file\n");
				}
			}
		}
	}

	public class GeneratorPanel extends JPanel implements ActionListener {

		private JLabel label;
		private JTextField textField;
		private JButton beginButton;
		private JButton killButton;
		
		public GeneratorPanel() {
			label = new JLabel("Names to Generate");
			add(label);
			
			textField = new JTextField(5);
			add(textField);
			
			beginButton = new JButton("Begin Generating");
			beginButton.addActionListener(this);
			add(beginButton);
			
			killButton = new JButton("Kill");
			killButton.addActionListener(this);
			add(killButton);
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == beginButton) {
				try {
					int value = Integer.parseInt(textField.getText());
					if (value > 0) {
						start(value);
					} else {
						output.append("value must be greater than 0\n");
					}
				} catch (NumberFormatException e) {
					output.append("input must be a valid integer greater than 0\n");
				}
			} else if (event.getSource() == killButton) {
				kill();
			}
		}
		
	}
	
	public class CharacterPanel extends JPanel implements ActionListener {

		private JLabel label;
		private JTextField textField;
		private JButton add;
		private JButton remove;
		
		public CharacterPanel() {
			label = new JLabel("Add/Remove Characters");
			add(label);
			
			textField = new JTextField(10);
			add(textField);
			
			add = new JButton("Add");
			add.addActionListener(this);
			add(add);
			
			remove = new JButton("Remove");
			remove.addActionListener(this);
			add(remove);
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == add) {
				addCharacters(textField.getText().toLowerCase().toCharArray());
			} else if (event.getSource() == remove) {
				removeCharacters(textField.getText().toLowerCase().toCharArray());
			}
		}
		
	}
	
}
