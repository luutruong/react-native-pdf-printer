#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(PdfPrinter, NSObject)

RCT_EXTERN_METHOD(printPDF:(NSString *)fileUri
                    resolver: (RCTPromiseResolveBlock)resolve
                    rejecter: (RCTPromiseRejectBlock)reject
                  )

+ (BOOL)requiresMainQueueSetup
{
  return TRUE;
}

@end
