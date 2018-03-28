# react-native-xml-sign

XML signature using W3C Signature specifications only Android

## Install

`npm i --save react-native-xml-sign`

## Linking Native Dependencies

### Automatic Linking

`react-native link react-native-xml-sign`

### Manual Linking

#### Android

1. In `android/setting.gradle`
    ```
    ...
    include ':react-native-xml-sign'
    project(':react-native-xml-sign').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-xml-sign/android')
    ```
   
2. In `android/app/build.gradle`
    ```
    ...
    dependencies {
        ...
        compile project(':react-native-xml-sign')
    }
    ```

3. Register module in `MainApplication.java`
    ```
    import br.com.duobr.xml.sign.XmlSignPackage;  // <--- import
    
    public class MainApplication extends Application implements ReactApplication {
      ......
    
      @Override
      protected List<ReactPackage> getPackages() {
          return Arrays.<ReactPackage>asList(
              new MainReactPackage(),
              new XmlSignPackage()      <------- Add this
          );
      }
    
      ......
    
    }
    ```

## Usage (XmlSign)

```jsx
import XmlSign from 'react-native-xml-sign';

XmlSign.signAsync(fileCertPath, passCert, xml, referenceURI)
  .then(signature => console.log(`<root>${xml}${signature}</root>`))
  .catch(error => console.error(error));

```

### Params

#### types

| Name           | Type     | Require | Description                        | Example                   |
| -------------- | -------- | ------- | ---------------------------------- | ------------------------- |
| _fileCertPath_ | *string* | true    | Certificate used in the signature. | "/path-in-phone/cert.pfx" |
| _passCert_     | *string* | true    | Certificate password.              | "123456"                  |
| _xml_          | *string* | true    | XML to be signed.                  | "<anyXML>...</anyXML>"    |
| _referenceURI_ | *string* | true    | <Reference URI="_referenceURI_">   | "http://www.myuri.org/"   |

