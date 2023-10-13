import React
import UIKit
import WebKit
import Foundation

@objc(PdfPrinter)
class PdfPrinter: NSObject {
  static func moduleName() -> String {
    return "PdfPrinter"
  }
  
  @objc static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc func printPDF(_ fileUri: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    if !UIPrintInteractionController.isPrintingAvailable {
      reject("PRINTER_UNAVAILABLE", "Print feature is not available", nil)
      return
    }
    
    let url = URL(fileURLWithPath: fileUri)
    
    DispatchQueue.main.async {
      if !UIPrintInteractionController.canPrint(url) {
        reject("INVALID_URL", "Cannot print this file \(fileUri)", nil)
        return
      }
      
      let printInfo = UIPrintInfo(dictionary: nil)
      printInfo.jobName = url.lastPathComponent
      printInfo.outputType = .photo
    
      let printController = UIPrintInteractionController.shared
      printController.printInfo = printInfo
      printController.showsNumberOfCopies = false
      printController.printingItem = url
      
      printController.present(animated: true) { (controller, completed, error) in
        if completed {
          resolve("Print job completed successfully")
        } else if let error = error {
          reject("PRINT_ERROR", "Print job failed with error: \(error.localizedDescription)", error)
        } else {
          reject("PRINT_CANCELLED", "Print job was canceled", nil)
        }
      }
    }
  }
}
