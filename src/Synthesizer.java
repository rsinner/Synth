import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Synthesizer {
	private javax.sound.midi.Synthesizer synth;
	private Instrument[] instr;
	private MidiChannel[] mcd;
	private Map<Point, Integer> keyNotes;
	private Map<Point, Boolean> notePlaying;
	private KeyListener keyListener; 
	private ArrayList<Integer> keyOrder;
	private boolean octave;
	private ArrayList<Key> keys;
	
	//dalfekg
	public Synthesizer() throws MidiUnavailableException {
		keys = new ArrayList<Key>();
		octave = false;
		synth = MidiSystem.getSynthesizer();
		synth.open();
		instr = synth.getAvailableInstruments();
		mcd = synth.getChannels();
			int x = 3;
		keyListener = new synthKeyListener();
		initializeNotes();
		
		notePlaying = new HashMap<Point, Boolean>();
	}

	public KeyListener getKeyListener() {
		return keyListener;
	}
	
	public JButton getKey(Point p) {
		for (Key k : keys) {
			if (p.equals(k.getKeyCode()))
				return k;
		}
		return null;
	}
	
	public void addKey(Key k) {
		keys.add(k);
	}
	
	public Point getKeyCode(int i, boolean white) {
		int key = 0;
		if(white) {
			key = i%7 + (i%7>=1?1:0)+(i%7>=2?1:0)+(i%7>=4?1:0)+(i%7>=5?1:0)+(i%7>=6?1:0);
		} else {
			key = 1 + (i%5>=1?2:0) + (i%5>=2?3:0) + (i%5>=3?2:0) + (i%5>=4?2:0);
		}
		//if(key >= keyOrder.size()) return 0;
		int keycode = keyOrder.get(key);
		return new Point(keycode, (white && i >= 7) || (!white && i >= 5) ? 1 + (white && i == 14 ? 1 : 0) : 0);
	}
	
	public void setInstrument(String s) {
		for (int i = 0; i < instr.length; i ++) {
			if (instr[i].getName().equals(s)) {
				String str = instr[i].toString();
				str = str.substring(str.indexOf('#')+1);
				int bank = Integer.parseInt(str.substring(0,str.indexOf(' ')));
				str = str.substring(str.indexOf('#')+1);
				int preset = Integer.parseInt(str);
				mcd[0].programChange(bank, preset);
			}
		}
	}
	
	public void initializeNotes() {
		keyOrder = new ArrayList<Integer>();
		keyNotes = new HashMap<Point, Integer>();
		
		keyNotes.put(new Point(KeyEvent.VK_A, 0), 60);//C
		keyOrder.add(KeyEvent.VK_A);
		
		keyNotes.put(new Point(KeyEvent.VK_W, 0), 61);//C#
		keyOrder.add(KeyEvent.VK_W);
		
		keyNotes.put(new Point(KeyEvent.VK_S, 0), 62);//D
		keyOrder.add(KeyEvent.VK_S);
		
		keyNotes.put(new Point(KeyEvent.VK_E, 0), 63);//Eb
		keyOrder.add(KeyEvent.VK_E);
		
		keyNotes.put(new Point(KeyEvent.VK_D, 0), 64);//E
		keyOrder.add(KeyEvent.VK_D);
		
		keyNotes.put(new Point(KeyEvent.VK_F, 0), 65);//F
		keyOrder.add(KeyEvent.VK_F);
		
		keyNotes.put(new Point(KeyEvent.VK_T, 0), 66);//F#
		keyOrder.add(KeyEvent.VK_T);
		
		keyNotes.put(new Point(KeyEvent.VK_G, 0), 67);//G
		keyOrder.add(KeyEvent.VK_G);
		
		keyNotes.put(new Point(KeyEvent.VK_Y, 0), 68);//Ab
		keyOrder.add(KeyEvent.VK_Y);
		
		keyNotes.put(new Point(KeyEvent.VK_H, 0), 69);//A
		keyOrder.add(KeyEvent.VK_H);
		
		keyNotes.put(new Point(KeyEvent.VK_U, 0), 70);//Bb
		keyOrder.add(KeyEvent.VK_U);
		
		keyNotes.put(new Point(KeyEvent.VK_J, 0), 71);//B
		keyOrder.add(KeyEvent.VK_J);
		
		keyNotes.put(new Point(KeyEvent.VK_K, 0), 72);//C
		
		
		
		keyNotes.put(new Point(KeyEvent.VK_A, KeyEvent.SHIFT_DOWN_MASK), 72);//C
		keyOrder.add(KeyEvent.VK_A);
		
		keyNotes.put(new Point(KeyEvent.VK_W, KeyEvent.SHIFT_DOWN_MASK), 73);//C#
		keyOrder.add(KeyEvent.VK_W);
		
		keyNotes.put(new Point(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK), 74);//D
		keyOrder.add(KeyEvent.VK_S);
		
		keyNotes.put(new Point(KeyEvent.VK_E, KeyEvent.SHIFT_DOWN_MASK), 75);//Eb
		keyOrder.add(KeyEvent.VK_E);
		
		keyNotes.put(new Point(KeyEvent.VK_D, KeyEvent.SHIFT_DOWN_MASK), 76);//E
		keyOrder.add(KeyEvent.VK_D);
		
		keyNotes.put(new Point(KeyEvent.VK_F, KeyEvent.SHIFT_DOWN_MASK), 77);//F
		keyOrder.add(KeyEvent.VK_F);
		
		keyNotes.put(new Point(KeyEvent.VK_T, KeyEvent.SHIFT_DOWN_MASK), 78);//F#
		keyOrder.add(KeyEvent.VK_T);
		
		keyNotes.put(new Point(KeyEvent.VK_G, KeyEvent.SHIFT_DOWN_MASK), 79);//G
		keyOrder.add(KeyEvent.VK_G);
		
		keyNotes.put(new Point(KeyEvent.VK_Y, KeyEvent.SHIFT_DOWN_MASK), 80);//Ab
		keyOrder.add(KeyEvent.VK_Y);
		
		keyNotes.put(new Point(KeyEvent.VK_H, KeyEvent.SHIFT_DOWN_MASK), 81);//A
		keyOrder.add(KeyEvent.VK_H);
		
		keyNotes.put(new Point(KeyEvent.VK_U, KeyEvent.SHIFT_DOWN_MASK), 82);//Bb
		keyOrder.add(KeyEvent.VK_U);
		
		keyNotes.put(new Point(KeyEvent.VK_J, KeyEvent.SHIFT_DOWN_MASK), 83);//B
		keyOrder.add(KeyEvent.VK_J);
		
		keyNotes.put(new Point(KeyEvent.VK_K, KeyEvent.SHIFT_DOWN_MASK), 84);//C
		//keyOrder.add(KeyEvent.VK_K);

		keyNotes.put(new Point(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), 84);//C
		keyOrder.add(KeyEvent.VK_K);
	}

	private class synthKeyListener implements KeyListener, MouseListener {

		@Override
		public void keyPressed(KeyEvent a) {
			Point p = new Point(a.getKeyCode(), a.getModifiersEx());
			if(keyNotes.get(p) != null && (notePlaying.get(p) == null || notePlaying.get(p) != true)) {
				JButton key = getKey(p);
				if (key != null) {
					key.getModel().setArmed(true);
					key.getModel().setPressed(true);
				}
				mcd[0].noteOn(keyNotes.get(p), 600);
				notePlaying.put(p, true);
			}
			
		}

		@Override
		public void keyReleased(KeyEvent a) {
			Point p = new Point(a.getKeyCode(), a.getModifiersEx());
			if(keyNotes.get(p) != null) {
				JButton key = getKey(p);
				if (key != null) {
					key.getModel().setArmed(false);
					key.getModel().setPressed(false);
				}
				mcd[0].noteOff(keyNotes.get(p), 600);
				notePlaying.put(p, false);
			}
		}

		@Override
		public void keyTyped(KeyEvent a) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent a) {

		}

		@Override
		public void mouseEntered(MouseEvent a) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent a) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent a) {

		}

		@Override
		public void mouseReleased(MouseEvent a) {

		}

	}

	

}
