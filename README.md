# react-native-xml-sign

XML signature using W3C Signature specifications only Android

## Install

`npm i --save react-native-xml-sign`


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

