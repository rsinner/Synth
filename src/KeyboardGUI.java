import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public class KeyboardGUI extends JFrame {
	private ArrayList<Key> keyboard;
	private final int numOfKeys = 15;
	private final int keyWidth = 50;
	private final int blackKeyWidth = keyWidth*3/4;
	private final int keyHeight = 250;
	private JLayeredPane keys;
	private JComboBox instrumentSelection;
	private Synthesizer synth;
	private S06PreferSelected select;
	
	// asfelkhg
	public KeyboardGUI() throws FileNotFoundException, MidiUnavailableException, BadLocationException {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(775,325);
		this.setLayout(new FlowLayout());
		this.setTitle("Synthesizer");
		int x = 3;

		keyboard = new ArrayList<Key>();
		keys = new JLayeredPane();
		keys.setMaximumSize(new Dimension(numOfKeys*keyWidth, keyHeight));
		keys.setMinimumSize(new Dimension(numOfKeys*keyWidth, keyHeight));
		keys.setPreferredSize(new Dimension(numOfKeys*keyWidth, keyHeight));
		keys.setLayout(null);

		synth = new Synthesizer();
		this.addKeyListener(synth.getKeyListener());
		
		initializeKeys();
		this.add(keys);
		
		instrumentSelection = new JComboBox();
		initializeInstruments();
		instrumentSelection.setEditable(true);
		
		instrumentSelection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				KeyboardGUI.this.requestFocusInWindow();
			}
			
		});
		
		((JTextComponent) instrumentSelection.getEditor().getEditorComponent()).setDocument(new S06PreferSelected(instrumentSelection));
		this.add(instrumentSelection);
		//instrumentSelection.setFocusable(false);
		instrumentSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				synth.setInstrument(instrumentSelection.getSelectedItem().toString());
			}
		});
		
		this.requestFocus();
	}

	public void initializeKeys() {
		int xPosition = -keyWidth/2;
		for (int i = 0; i < ((numOfKeys+1)/8)*5; i++) {
			if (i % 5 == 2 || i % 5 == 0)
				xPosition += keyWidth;
			Key b = new Key(false, synth.getKeyCode(i, false), synth.getKeyListener());
			b.setBounds(xPosition+(keyWidth-blackKeyWidth)/2, 0, blackKeyWidth, keyHeight/2);
			xPosition += keyWidth;
			keyboard.add(b);
			keys.add(b, JLayeredPane.PALETTE_LAYER);
			synth.addKey(b);
		}
		for (int i = 0; i < numOfKeys; i++) {
			Key w = new Key(true, synth.getKeyCode(i, true), synth.getKeyListener());
			w.setBounds(keyWidth*i, 0, keyWidth, keyHeight);
			keyboard.add(w);
			keys.add(w, JLayeredPane.DEFAULT_LAYER);
			synth.addKey(w);
		}
	}

	public void initializeInstruments() throws MidiUnavailableException {
		javax.sound.midi.Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		Instrument[] orchestra = synthesizer.getAvailableInstruments();
		for (Instrument instrument : orchestra) {
			String name = instrument.getName();
			instrumentSelection.addItem(name);
		}
		synthesizer.close();
	}

	public static void main(String[] args) throws FileNotFoundException, MidiUnavailableException, BadLocationException {
		KeyboardGUI gui = new KeyboardGUI();
		gui.setVisible(true);
	}

}