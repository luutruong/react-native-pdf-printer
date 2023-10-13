package com.pdfprinter;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import com.facebook.react.bridge.Promise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends PrintDocumentAdapter {
  String path;
  String jobName;

  public PdfDocumentAdapter(String path, String jobName){
    this.path = path;
    this.jobName = jobName;
  }

  @Override
  public void onLayout(
    PrintAttributes oldAttributes,
    PrintAttributes newAttributes,
    CancellationSignal cancellationSignal,
    LayoutResultCallback layoutResultCallback,
    Bundle extras
  ) {
    if (cancellationSignal.isCanceled()) {
      layoutResultCallback.onLayoutCancelled();
      return;
    }

    PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder(this.jobName);
    builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
            .build();
    layoutResultCallback.onLayoutFinished(builder.build(), true);
  }

  @Override
  public void onWrite(PageRange[] pages, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
    InputStream input = null;
    OutputStream output = null;

    try {
      input = new FileInputStream(this.path);
      output = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

      byte[] buff = new byte[1024];
      int bytesRead;
      while ((bytesRead = input.read(buff)) >= 0) {
        output.write(buff, 0, bytesRead);
      }

      writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

      input.close();
      output.close();
    } catch (FileNotFoundException ee) {
        System.out.println("file not found: " + ee.getMessage());
    } catch (Exception e) {
        System.out.println("read file error: " + e.getMessage());
        e.printStackTrace();
    }
  }

  @Override
  public void onFinish() {
    // clean up resources if needed
  }
}
