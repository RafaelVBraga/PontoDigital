package com.rvbraga.pontodigital.util;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class CaptureThread extends Thread {
  public static final String ACT_CAPTURE = "capture_thread_captured";
  
  private ActionListener m_listener;
  
  private boolean m_bCancel;
  
  private Reader m_reader;
  
  private boolean m_bStream;
  
  private Fid.Format m_format;
  
  private Reader.ImageProcessing m_proc;
  
  private CaptureEvent m_last_capture;
  
  public class CaptureEvent extends ActionEvent {
    private static final long serialVersionUID = 101L;
    
    public Reader.CaptureResult capture_result;
    
    public Reader.Status reader_status;
    
    public UareUException exception;
    
    public CaptureEvent(Object source, String action, Reader.CaptureResult cr, Reader.Status st, UareUException ex) {
      super(source, 1001, action);
      this.capture_result = cr;
      this.reader_status = st;
      this.exception = ex;
    }
  }
  
  public CaptureThread(Reader reader, boolean bStream, Fid.Format img_format, Reader.ImageProcessing img_proc) {
    this.m_bCancel = false;
    this.m_reader = reader;
    this.m_bStream = bStream;
    this.m_format = img_format;
    this.m_proc = img_proc;
  }
  
  public void start(ActionListener listener) {
    this.m_listener = listener;
    start();
  }
  
  public void join(int milliseconds) throws InterruptedException{
    super.join(milliseconds); 
  }
  
  public CaptureEvent getLastCaptureEvent() {
    return this.m_last_capture;
  }
  
  private void Capture() {
    try {
      boolean bReady = false;
      while (!bReady && !this.m_bCancel) {
        Reader.Status rs = this.m_reader.GetStatus();
        if (Reader.ReaderStatus.BUSY == rs.status)
          try {
            Thread.sleep(100L);
            continue;
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }  
        if (Reader.ReaderStatus.READY == rs.status || Reader.ReaderStatus.NEED_CALIBRATION == rs.status) {
          bReady = true;
          break;
        } 
        NotifyListener("capture_thread_captured", null, rs, null);
      } 
      if (this.m_bCancel) {
        Reader.CaptureResult cr = new Reader.CaptureResult();
        cr.quality = Reader.CaptureQuality.CANCELED;
        NotifyListener("capture_thread_captured", cr, null, null);
      } 
      if (bReady) {
        Reader.CaptureResult cr = this.m_reader.Capture(this.m_format, this.m_proc, 500, -1);
        NotifyListener("capture_thread_captured", cr, null, null);
      } 
    } catch (UareUException e) {
      NotifyListener("capture_thread_captured", null, null, e);
    } 
  }
  
  private void Stream() {
    try {
      boolean bReady = false;
      while (!bReady && !this.m_bCancel) {
        Reader.Status rs = this.m_reader.GetStatus();
        if (Reader.ReaderStatus.BUSY == rs.status)
          try {
            Thread.sleep(100L);
            continue;
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }  
        if (Reader.ReaderStatus.READY == rs.status || Reader.ReaderStatus.NEED_CALIBRATION == rs.status) {
          bReady = true;
          break;
        } 
        NotifyListener("capture_thread_captured", null, rs, null);
      } 
      if (bReady) {
        this.m_reader.StartStreaming();
        while (!this.m_bCancel) {
          Reader.CaptureResult cr = this.m_reader.GetStreamImage(this.m_format, this.m_proc, 500);
          NotifyListener("capture_thread_captured", cr, null, null);
        } 
        this.m_reader.StopStreaming();
      } 
    } catch (UareUException e) {
      NotifyListener("capture_thread_captured", null, null, e);
    } 
    if (this.m_bCancel) {
      Reader.CaptureResult cr = new Reader.CaptureResult();
      cr.quality = Reader.CaptureQuality.CANCELED;
      NotifyListener("capture_thread_captured", cr, null, null);
    } 
  }
  
  private void NotifyListener(String action, Reader.CaptureResult cr, Reader.Status st, UareUException ex) {
    final CaptureEvent evt = new CaptureEvent(this, action, cr, st, ex);
    this.m_last_capture = evt;
    if (null == this.m_listener || null == action || action.equals(""))
      return; 
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            CaptureThread.this.m_listener.actionPerformed(evt);
          }
        });
  }
  
  public void cancel() {
    this.m_bCancel = true;
    try {
      if (!this.m_bStream)
        this.m_reader.CancelCapture(); 
    } catch (UareUException e) {}
  }
  
  public void run() {
    if (this.m_bStream) {
      Stream();
    } else {
      Capture();
    } 
  }
}
