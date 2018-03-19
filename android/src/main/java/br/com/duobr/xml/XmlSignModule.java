package br.com.duobr.xml;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class XmlSignModule extends ReactContextBaseJavaModule {

  public XmlSignModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "XmlSign";
  }

  @ReactMethod
  public void signAsync(String fileCertPath, String passCert, String xml, String referenceURI, Promise promise) {
    try {
      promise.resolve(Xml.sign(fileCertPath, passCert, xml, referenceURI));
    } catch(Exception e) {
      promise.reject("XML_SIGN_EXCEPTION", e);
    }
  }
}
