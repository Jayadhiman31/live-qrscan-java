import java.awt.image.BufferedImage;

import com.google.zxing.LuminanceSource;

public final class BufferedImageLuminanceSource extends LuminanceSource {
    private final byte[] luminances;

    public BufferedImageLuminanceSource(BufferedImage image) {
        super(image.getWidth(), image.getHeight());

        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        luminances = new byte[width * height];

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int r = (pixel >> 16) & 0xff;
            int g = (pixel >> 8) & 0xff;
            int b = pixel & 0xff;
            if (r == g && g == b) {
                luminances[i] = (byte) r;
            } else {
                luminances[i] = (byte) ((r + g + g + b) >> 2);
            }
        }
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }
        System.arraycopy(luminances, y * width, row, 0, width);
        return row;
    }

    @Override
    public byte[] getMatrix() {
        return luminances;
    }
}
