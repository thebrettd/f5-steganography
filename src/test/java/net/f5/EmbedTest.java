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

        String unitTestPassword = "unitTestPassword";
        ByteArrayOutputStream embed = Embed.embed(steganogramFileName, new BufferedInputStream(new ByteArrayInputStream(bytes)), unitTestPassword, 75, new ByteArrayInputStream("my test message".getBytes()));

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            Extract.extract(new ByteArrayInputStream(embed.toByteArray()), embed.size(), outputStream, unitTestPassword);

        } catch (final Exception e) {
            e.printStackTrace();
        }

        assertTrue(outputStream.toString().equals("my test message"));
    }
}