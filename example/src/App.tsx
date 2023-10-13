import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity, Alert } from 'react-native';
import { printPdf } from 'react-native-pdf-printer';
import RNFS from 'react-native-fs';

const examplePdfUri =
  'https://file-examples.com/storage/feaade38c1651bd01984236/2017/10/file-example_PDF_1MB.pdf';

export default function App() {
  const handlePrint = React.useCallback(async () => {
    const tempFile = `${RNFS.TemporaryDirectoryPath}/temp-example.pdf`;

    try {
      await RNFS.downloadFile({
        fromUrl: examplePdfUri,
        toFile: tempFile,
      }).promise;

      await printPdf(tempFile);
    } catch (e: any) {
      Alert.alert(e.message);
    }
  }, []);

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={handlePrint}>
        <View>
          <Text>Print</Text>
        </View>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
