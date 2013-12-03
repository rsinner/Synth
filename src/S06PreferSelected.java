import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class S06PreferSelected extends PlainDocument {
	JComboBox comboBox;
	ComboBoxModel model;
	JTextComponent editor;
	//JComboBox instrumentSelection;
	// flag to indicate if setSelectedItem has been called
	// subsequent calls to remove/insertString should be ignored
	boolean selecting=false;
	boolean hitBackspace;
	boolean hitBackspaceOnSelection;

	//sdaofje
	public S06PreferSelected(final JComboBox comboBox) throws MidiUnavailableException, BadLocationException {
		this.comboBox = comboBox;
		int y = 3;
		//instrumentSelection = new JComboBox();
		//instrumentNames();
		model = comboBox.getModel();
		editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		editor.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (comboBox.isDisplayable()) comboBox.setPopupVisible(true);
				hitBackspace = false;
				switch (e.getKeyCode()) {
				// determine if the pressed key is backspace (needed by the remove method)
			      case KeyEvent.VK_BACK_SPACE :
			        hitBackspace=true;
			        hitBackspaceOnSelection=editor.getSelectionStart()!=editor.getSelectionEnd();
			        break;
			      // ignore delete key
			      case KeyEvent.VK_DELETE :
			        e.consume();
			        UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
			        break;
				}
			}
		});
		editor.setDocument(this);
		Object selected = comboBox.getSelectedItem();
		if (selected != null) setText(selected.toString());
		highlightCompletedText(0);
	}

	public void remove(int offs, int len) throws BadLocationException {
		// return immediately when selecting an item
		if (selecting) return;
		if (hitBackspace) {
			/// user hit backspace => move the selection backwards
		    // old item keeps being selected
		    if (offs>0) {
		      if (hitBackspaceOnSelection) offs--;
		    } else {
		      // User hit backspace with the cursor positioned on the start => beep
		      UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
		    }
		    highlightCompletedText(offs);
		  } else {
		    super.remove(offs, len);
		  }
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		/*// return immediately when selecting an item
		if (selecting) return;
		// insert the string into the document
		super.insertString(offs, str, a);
		// lookup and select a matching item
		Object item = lookupItem(getText(0, getLength()));
		setSelectedItem(item);
		setText(item.toString());
		// select the completed part
		highlightCompletedText(offs+str.length());*/
		// return immediately when selecting an item
        if (selecting) return;
        // insert the string into the document
        super.insertString(offs, str, a);
        // lookup and select a matching item
        Object item = lookupItem(getText(0, getLength()));
        if (item != null) {
            //setSelectedItem(item);
        } else {
            // keep old item selected if there is no match
            item = comboBox.getSelectedItem();
            // imitate no insert (later on offs will be incremented by str.length(): selection won't move forward)
            offs = offs-str.length();
            // provide feedback to the user that his input has been received but can not be accepted
            comboBox.getToolkit().beep(); // when available use: UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
        }
        setText(item.toString());
        // select the completed part
        highlightCompletedText(offs+str.length());
	}

	private void setText(String text) throws BadLocationException {
		// remove all text and insert the completed string
		super.remove(0, getLength());
		super.insertString(0, text, null);
	}

	private void highlightCompletedText(int start) {
		editor.setSelectionStart(start);
		editor.setSelectionEnd(getLength());
	}

	private void setSelectedItem(Object item) {
		selecting = true;
		model.setSelectedItem(item);
		selecting = false;
	}

	private Object lookupItem(String pattern) {
		Object selectedItem = model.getSelectedItem();
		// only search for a different item if the currently selected does not match
		if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
			System.out.println("Selected item '" + selectedItem + "' matches '" + pattern + "'");
			return selectedItem;
		} else {
			// iterate over all items
			for (int i=0, n=model.getSize(); i < n; i++) {
				Object currentItem = model.getElementAt(i);
				// current item starts with the pattern?
				if (startsWithIgnoreCase(currentItem.toString(), pattern)) {
					System.out.println("New selection: '" + currentItem + "'");
					return currentItem;
				}
			}
		}
		// no item starts with the pattern => return null
		return null;
	}

	// checks if str1 starts with str2 - ignores case
	private boolean startsWithIgnoreCase(String str1, String str2) {
		return str1.toUpperCase().startsWith(str2.toUpperCase());
	}
	/*
	private void instrumentNames() throws MidiUnavailableException {
		javax.sound.midi.Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		Instrument[] orchestra = synthesizer.getAvailableInstruments();
		for (Instrument instrument : orchestra) {
			String name = instrument.getName();
			instrumentSelection.addItem(name);
		}
		synthesizer.close();
	}
	 */
	private static void createAndShowGUI() throws MidiUnavailableException, BadLocationException {
		ArrayList<String> instrumentSelection = new ArrayList<String>();
		javax.sound.midi.Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		Instrument[] orchestra = synthesizer.getAvailableInstruments();
		for (Instrument instrument : orchestra) {
			String name = instrument.getName();
			instrumentSelection.add(name);
		}
		synthesizer.close();

		// the combo box (add/modify items if you like to)
		//JComboBox comboBox = new JComboBox(new Object[] {"Ester", "Jordi", "Jordina", "Jorge", "Sergi"});
		JComboBox comboBox = new JComboBox();
		for (String s : instrumentSelection) {
			comboBox.addItem(s);
		}
		// has to be editable
		comboBox.setEditable(true);
		// get the combo boxes editor component
		JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		// change the editor's document
		editor.setDocument(new S06PreferSelected(comboBox));

		// create and show a window containing the combo box
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().add(comboBox);
		frame.pack(); frame.setVisible(true);
	}


	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}