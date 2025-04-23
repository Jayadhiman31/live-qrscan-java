import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;

public class QRScanner {
    static {
        // Load OpenCV native library
        System.load("C:\\Users\\JAYA DHIMAN\\OneDrive\\Desktop\\java qr\\libs\\opencv_java4110.dll");
    }

    public static void main(String[] args) throws InterruptedException {
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Camera not detected");
            return;
        }

        Mat frame = new Mat();

        while (true) {
            if (camera.read(frame)) {
                // Convert frame to grayscale
                Mat grayFrame = new Mat();
                Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

                // Convert to BufferedImage
                BufferedImage bufferedImage = matToBufferedImage(grayFrame);

                // Convert to BinaryBitmap for ZXing
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    System.out.println("QR Code Data: " + result.getText());

                    // Wait 2 seconds before scanning next QR
                    Thread.sleep(2000);
                } catch (NotFoundException e) {
                    // No QR code found, continue scanning
                }

                HighGui.imshow("Live Feed", frame);
                if (HighGui.waitKey(1) == 27) { // Press ESC to exit
                    break;
                }
            }
        }

        camera.release();
        HighGui.destroyAllWindows();
    }

    // Convert OpenCV Mat to BufferedImage
    public static BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            Mat matRGB = new Mat();
            Imgproc.cvtColor(mat, matRGB, Imgproc.COLOR_BGR2RGB);
            mat = matRGB;
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        byte[] b = new byte[mat.channels() * mat.cols() * mat.rows()];
        mat.get(0, 0, b);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b);
        return image;
    }
}
