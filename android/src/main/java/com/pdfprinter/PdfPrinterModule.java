package com.pdfprinter;

import androidx.annotation.NonNull;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.PrintAttributes;
import android.content.Context;
import android.net.Uri;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = PdfPrinterModule.NAME)
public class PdfPrinterModule extends ReactContextBaseJavaModule {
  public static final String NAME = "PdfPrinter";

  public PdfPrinterModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void printPDF(String fileUri, Promise promise) {
    PrintAttributes attributes = new PrintAttributes.Builder()
        .build();
    String jobName = Uri.parse(fileUri).getLastPathSegment();

    PdfDocumentAdapter printAdapter = new PdfDocumentAdapter(fileUri, jobName);

    try {
      PrintManager printManager = (PrintManager) getCurrentActivity().getSystemService(Context.PRINT_SERVICE);

      PrintJob job = printManager.print(jobName, printAdapter, attributes);
      if (job.isCompleted()) {
          promise.resolve("print completed");
      } else if (job.isCancelled()) {
          promise.reject("PRINT_CANCELLED", "print has been canceled");
      } else if (job.isFailed()) {
          promise.reject("PRINT_FAILED", "print failed");
      }
    } catch (Exception ex) {
      promise.reject("PRINT_ERROR", ex);
    }
  }
}
