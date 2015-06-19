package net.f5;

import obfusc8r.Utils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.URL;
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
     0 = {java.lang.String@4062} "-e"
     1 = {java.lang.String@4036} "150618191023-hiddenMessage.txt"
     2 = {java.lang.String@4063} "-p"
     3 = {java.lang.String@4035} "123"
     4 = {java.lang.String@4064} "-q"
     5 = {java.lang.String@4066} "75"
     6 = {java.lang.String@4040} "150618191023-steganogram.jpg"
     7 = {java.lang.String@4042} "/Users/brett/obfusc8r/150618191023-encoded.jpg"

     */
        String[] args = new String[6];
        args[0] = "-e";
        args[1] = "/Users/brett/obfusc8r/src/test/resources/unitTestMessage.txt";
        args[2] = "-p";
        String unitTestPassword = "unitTestPassword";
        args[3] = unitTestPassword;

        URL url = this.getClass().getResource("/small.jpg");
        FileInputStream fileInputStream = new FileInputStream(url.getFile());
        //Write uploaded file to disk.
        byte[] bytes = IOUtils.toByteArray(fileInputStream);
        String fileName = Utils.dateFormat.format(new Date()) + "-steganogram.jpg";
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
        stream.write(bytes);
        stream.close();
        args[4] = fileName;

        //Write encoded file to disk..
        String encodedFileName = System.getProperty("user.dir") + File.separator + Utils.dateFormat.format(new Date()) + "-encoded.jpg";
        args[5] = encodedFileName;

        Embed.embedMain(args, 75);

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