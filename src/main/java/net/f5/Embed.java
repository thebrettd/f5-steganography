package net.f5;

import james.JpegEncoder;

import java.awt.Image;
import java.io.*;

import net.f5.image.Bmp;

import javax.imageio.ImageIO;

public class Embed {

    public static ByteArrayOutputStream embed(String steganogramFileName, BufferedInputStream steganogramFileStream, String password, Integer quality, InputStream messageStream) throws IOException {

        Image image = getImageFromInput(steganogramFileName, steganogramFileStream);

        String comment = "JPEG Encoder Copyright 1998, James R. Weeks and BioElectroMech.";

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        JpegEncoder jpg = new JpegEncoder(image, quality, dataOut, comment);
        jpg.compress(messageStream, password);

        try {
            dataOut.close();
        } catch (final IOException e) {
        }
        return dataOut;
    }

    private static Image getImageFromInput(String steganogramFileName, BufferedInputStream steganogramFileStream) throws IOException {
        Image image;
        if (steganogramFileName.endsWith(".bmp")) {
            final Bmp bmp = new Bmp(steganogramFileStream);
            image = bmp.getImage();
        } else {
            image = ImageIO.read(steganogramFileStream);
        }
        return image;
    }

}
