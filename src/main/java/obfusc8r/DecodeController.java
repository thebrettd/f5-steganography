package obfusc8r;

import net.f5.Extract;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

@Controller
public class DecodeController {



    @RequestMapping(value="/decode", method=RequestMethod.GET)
    public @ResponseBody String provideDecodeInfo() {
        return "You can decode a file by posting to this same URL.";
    }

    @RequestMapping(value="/decode", method=RequestMethod.POST)
    public @ResponseBody String handleDecodeFileUpload(@RequestParam("encodedFile") MultipartFile file,
                                                 @RequestParam("password") String password)
    {
        if (!file.isEmpty()) {
            try {

                String encodedFile = Utils.dateFormat.format(new Date()) + "-toDecode.jpg";
                BufferedOutputStream encodedStream = new BufferedOutputStream(new FileOutputStream(new File(encodedFile)));
                byte[] bytes = file.getBytes();
                encodedStream.write(bytes);

                String messageFile = Utils.dateFormat.format(new Date()) + "out.txt";

                try {
                    File encodedFile1 = new File(encodedFile);
                    final FileInputStream fis = new FileInputStream(encodedFile1);
                    FileOutputStream fos = new FileOutputStream(new File(messageFile));
                    Extract.extract(fis, (int) encodedFile1.length(), fos, password);

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

                return "Decoded message: " + messageBuilder.toString();
            } catch (Exception e) {
                return "You failed to decode message from Steganogram => " + e.getMessage();
            }
        } else {
            return "You failed to decode message because the file was empty.";
        }
    }


}
