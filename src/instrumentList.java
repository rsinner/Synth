import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class instrumentList {
	// prints out a list of all the instruments that come with Java
	public static void main(String[] args) throws MidiUnavailableException {
		javax.sound.midi.Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		Instrument[] orchestra = synthesizer.getAvailableInstruments();

		StringBuilder sb = new StringBuilder();
		String eol = System.getProperty("line.separator");
		sb.append(
				"The orchestra has " + 
						orchestra.length + 
						" instruments." + 
						eol);
		for (Instrument instrument : orchestra) {
			sb.append(instrument.toString());
			sb.append(eol);
		}
		synthesizer.close();
		JOptionPane.showMessageDialog(null,
				new JScrollPane(new JTextArea(sb.toString(),20,30)));
	}
}
