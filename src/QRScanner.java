import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class QRScanner {

    static {
        System.load("C:\\Users\\JAYA DHIMAN\\OneDrive\\Desktop\\java qr\\libs\\opencv_java4110.dll");
    }

    public static void main(String[] args) throws Exception {
        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("Camera not detected!");
            return;
        }

        Mat frame = new Mat();
        System.out.println("Starting camera...");

        while (true) {
            if (camera.read(frame)) {
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);

                MatOfByte mem = new MatOfByte();
                Imgcodecs.imencode(".jpg", frame, mem);
                byte[] byteArray = mem.toArray();
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(byteArray));

                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    System.out.println("QR Code Detected: " + result.getText());

                    // Pause for 2 seconds before looking for another QR code
                    Thread.sleep(2000);
                    System.out.println("Looking for next QR code...");
                } catch (NotFoundException e) {
                    // No QR code detected in this frame
                }
            }
        }
    }
}
