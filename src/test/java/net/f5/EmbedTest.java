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
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(steganogramFileName)));
        stream.write(bytes);
        stream.close();

        //Write encoded file to disk..
        String encodedFileName = System.getProperty("user.dir") + File.separator + Utils.dateFormat.format(new Date()) + "-encoded.jpg";

        String unitTestPassword = "unitTestPassword";
        Embed.embed(encodedFileName, steganogramFileName, unitTestPassword, 75, new ByteArrayInputStream("my test message".getBytes()));

        String messageFile = Utils.dateFormat.format(new Date()) + "out.txt";

        try {
            File encodedFile = new File(encodedFileName);
            final FileInputStream fis = new FileInputStream(encodedFile);
            FileOutputStream fos = new FileOutputStream(new File(messageFile));
            Extract.extract(fis, (int) encodedFile.length(), fos, unitTestPassword);

        } catch (final Exception e) {
            e.printStackTrace();
        }

        StringBuilder messageBuilder = new StringBuilder();
        FileReader fr = new FileReader(messageFile);
        BufferedReader br = new BufferedReader(fr);
        String s;
        while((s = br.readLine()) != null) {
            messageBuilder.append(s);
        }
        fr.close();

        assertTrue(messageBuilder.toString().equals("my test message"));


    }
}