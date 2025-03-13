package com.rvbraga.pontodigital;
import com.digitalpersona.uareu.Fid;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
  private static final long serialVersionUID = 5L;
  
  private BufferedImage m_image;
  
  public void showImage(Fid image) {
    Fid.Fiv view = image.getViews()[0];
    this.m_image = new BufferedImage(view.getWidth(), view.getHeight(), 10);
    this.m_image.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());
    repaint();
  }
  public void clearImage() {
	this.m_image = null;
	repaint();
  }
  
  public void paint(Graphics g) {
		super.paint(g);
		if (this.m_image != null) {
			g.drawImage(this.m_image, 0, 0, null);
		}
	}
}
