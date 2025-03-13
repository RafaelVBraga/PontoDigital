package com.rvbraga.pontodigital;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import javax.swing.JOptionPane;

public class MessageBox {
  public static void BadQuality(Reader.CaptureQuality q) {
    JOptionPane.showMessageDialog(null, q.toString(), "Bad quality", 2);
  }
  
  public static void BadStatus(Reader.Status s) {
    String str = String.format("Reader status: %s", new Object[] { s.toString() });
    JOptionPane.showMessageDialog(null, str, "Reader status", 0);
  }
  
  public static void DpError(String strFunctionName, UareUException e) {
    String str = String.format("%s returned DP error %d \n%s", new Object[] { strFunctionName, Integer.valueOf(e.getCode() & 0xFFFF), e.toString() });
    JOptionPane.showMessageDialog(null, str, "Error", 0);
  }
  
  public static void Warning(String strText) {
    JOptionPane.showMessageDialog(null, strText, "Warning", 2);
  }
}
