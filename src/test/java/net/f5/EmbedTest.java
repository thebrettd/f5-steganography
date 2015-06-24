package net.f5;

import obfusc8r.Utils;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by brett on 6/18/15.
 */
public class EmbedTest {

    @Test
    public void testEmbedMain() throws Exception {

        Path path = Paths.get(this.getClass().getResource("/small.jpg").getFile());
        byte[] bytes = Files.readAllBytes(path);
        String steganogramFileName = Utils.dateFormat.format(new Date()) + "-steganogram.jpg";

        String encodedFileName = System.getProperty("user.dir") + File.separator + Utils.dateFormat.format(new Date()) + "-encoded.jpg";

        String unitTestPassword = "unitTestPassword";
        Embed.embed(encodedFileName, steganogramFileName, new BufferedInputStream(new ByteArrayInputStream(bytes)), unitTestPassword, 75, new ByteArrayInputStream("my test message".getBytes()));

        ByteArrayOutputStream outputStream = null;
        try {
            File encodedFile = new File(encodedFileName);
            final FileInputStream fis = new FileInputStream(encodedFile);
            outputStream = new ByteArrayOutputStream();
            Extract.extract(fis, (int) encodedFile.length(), outputStream, unitTestPassword);

        } catch (final Exception e) {
            e.printStackTrace();
        }

        assertTrue(outputStream.toString().equals("my test message"));
    }
}