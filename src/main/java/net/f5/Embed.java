package net.f5;

import james.JpegEncoder;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;

import net.f5.image.Bmp;

public class Embed {

    public static void embed(String outFilePath, String steganogramFileName, String password, Integer quality, String messageFilePath) throws FileNotFoundException {

        Image image;
        if (steganogramFileName.endsWith(".bmp")) {
            final Bmp bmp = new Bmp(steganogramFileName);
            image = bmp.getImage();
        } else {
            image = Toolkit.getDefaultToolkit().getImage(steganogramFileName);
        }

        String comment = "JPEG Encoder Copyright 1998, James R. Weeks and BioElectroMech.  ";
        JpegEncoder jpg;

        FileOutputStream dataOut = null;
        File outFile = new File(outFilePath);
        try {
            dataOut = new FileOutputStream(outFile);
        } catch (final IOException e) {
        }

        jpg = new JpegEncoder(image, quality, dataOut, comment);
        jpg.Compress(new FileInputStream(messageFilePath), password);

        try {
            dataOut.close();
        } catch (final IOException e) {
        }

    }

}
