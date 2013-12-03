import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;


public class Key extends JButton implements MouseListener {
	private boolean whiteKey;
	private Point keyCode;
	private KeyListener k;
	
	public Key (boolean whiteKey, Point keyCode, KeyListener k) {
		this.whiteKey = whiteKey;
		this.keyCode = keyCode;
		this.k = k;
		if (whiteKey) 
			this.setBackground(Color.WHITE);
		else
			this.setBackground(Color.BLACK);
		super.setFocusable(false);
		this.addMouseListener(this);
	}
	
	public Point getKeyCode() {
		KeyEvent k = new KeyEvent(this, 0, 0, keyCode.y, keyCode.x);
		return new Point(k.getKeyCode(),k.getModifiers());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		k.keyPressed(new KeyEvent(this, 0, System.currentTimeMillis(), keyCode.y, keyCode.x));
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		k.keyReleased(new KeyEvent(this, 0, System.currentTimeMillis(), keyCode.y, keyCode.x));
	}

}
