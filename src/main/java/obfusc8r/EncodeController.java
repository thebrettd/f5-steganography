package obfusc8r;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

@Controller
public class EncodeController {

    @RequestMapping(value="/encode", method=RequestMethod.GET)
    public @ResponseBody String provideEncodeInfo() {
        return "You can encode a message into a file by posting to this same URL.";
    }

    @RequestMapping(value="/encode", method=RequestMethod.POST)
    public @ResponseBody String handleEncodeFileUpload(@RequestParam("message") String message,
                                                 @RequestParam("file") MultipartFile file,
                                                 @RequestParam("password") String password)
    {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileName = new Date() + "-encoded.jpg";
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                return "You successfully encoded "  + message + " into file " + fileName + " with password " + password;
            } catch (Exception e) {
                return "You failed to encode message " + message + " => " + e.getMessage();
            }
        } else {
            return "You failed to encode " + message + " because the file was empty.";
        }
    }


}
