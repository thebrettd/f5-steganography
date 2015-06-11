package obfusc8r;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

@Controller
public class DecodeController {

    @RequestMapping(value="/decode", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can decode a file by posting to this same URL.";
    }

    @RequestMapping(value="/decode", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("encodedFile") MultipartFile file,
                                                 @RequestParam("password") String password)
    {
        if (!file.isEmpty()) {
            try {

                String fileName = new Date() + "-toDecode.jpg";
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));

                byte[] bytes = file.getBytes();
                stream.write(bytes);
                stream.close();

                String message = "A test message";

                return "Decoded message: " + message ;
            } catch (Exception e) {
                return "You failed to decode message from Steganogram => " + e.getMessage();
            }
        } else {
            return "You failed to decode message because the file was empty.";
        }
    }


}
