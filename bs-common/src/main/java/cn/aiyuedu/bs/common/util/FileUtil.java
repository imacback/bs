package cn.aiyuedu.bs.common.util;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by webwyz on 14/10/21.
 */
public class FileUtil {
    public static String getFileEncode(File file) {
        String encoding = null;
        try {
            byte[] buf = new byte[4096];
            FileInputStream fis = new FileInputStream(file);

            // (1)
            UniversalDetector detector = new UniversalDetector(null);

            // (2)
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            // (3)
            detector.dataEnd();

            // (4)
            encoding = detector.getDetectedCharset();
            if (encoding != null) {
                System.out.println("Detected encoding = " + encoding);
            } else {
                System.out.println("No encoding detected.");
            }

            // (5)
            detector.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encoding;
    }
}
