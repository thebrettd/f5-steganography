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
    /*
    decodeArgs = {java.lang.String[8]@4038}
     6 = {java.lang.String@4040} "150618191023-steganogram.jpg"
     7 = {java.lang.String@4042} "/Users/brett/obfusc8r/150618191023-encoded.jpg"

     */
        String[] args = new String[2];

        String messageFilePath = "/Users/brett/obfusc8r/src/test/resources/unitTestMessage.txt";

        Path path = Paths.get(this.getClass().getResource("/small.jpg").getFile());
        byte[] bytes = Files.readAllBytes(path);
        String steganogramFileName = Utils.dateFormat.format(new Date()) + "-steganogram.jpg";
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(steganogramFileName)));
        stream.write(bytes);
        stream.close();
        args[0] = steganogramFileName;

        //Write encoded file to disk..
        String encodedFileName = System.getProperty("user.dir") + File.separator + Utils.dateFormat.format(new Date()) + "-encoded.jpg";
        args[1] = encodedFileName;

        String unitTestPassword = "unitTestPassword";
        Embed.embedMain(args, steganogramFileName, unitTestPassword, 75, messageFilePath);

        String[] decodeArgs = new String[7];
        decodeArgs[0] = "-e";
        decodeArgs[1] = encodedFileName;
        decodeArgs[2] = "-p";
        decodeArgs[3] = unitTestPassword;
        decodeArgs[4] = "-e";
        String messageFile = Utils.dateFormat.format(new Date()) + "out.txt";
        decodeArgs[5] = messageFile;
        decodeArgs[6] = encodedFileName;

        Extract.extractMain(decodeArgs);

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