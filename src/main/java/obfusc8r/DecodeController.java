package obfusc8r;

import net.f5.Extract;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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
                ByteArrayOutputStream baos = null;
                try {
                    baos = new ByteArrayOutputStream();
                    Extract.extract(file.getInputStream(), file.getBytes().length, baos, password);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return "Decoded message: " + baos.toString();
            } catch (Exception e) {
                return "You failed to decode message from Steganogram => " + e.getMessage();
            }
        } else {
            return "You failed to decode message because the file was empty.";
        }
    }


}
