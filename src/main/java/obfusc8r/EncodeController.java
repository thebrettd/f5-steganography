package obfusc8r;

import net.f5.Embed;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Controller
public class EncodeController {

    @RequestMapping(value = "/encode", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideEncodeInfo() {
        return "You can encode a message into a file by posting to this same URL.";
    }

    @RequestMapping(value = "/encode", method = RequestMethod.POST)
    public HttpEntity<byte[]> handleEncodeFileUpload(@RequestParam("message") String message,
                                                     @RequestParam("file") MultipartFile file,
                                                     @RequestParam("password") String password) throws IOException {
        if (!file.isEmpty()) {

            //Write message to a file.
            //Todo: just pass a string
            String hiddenMessage = Utils.dateFormat.format(new Date()) + "-hiddenMessage.txt";
            BufferedOutputStream hiddenStream = new BufferedOutputStream(new FileOutputStream(new File(hiddenMessage)));
            hiddenStream.write(message.getBytes());
            hiddenStream.close();


            //Write uploaded file to disk.
            byte[] bytes = file.getBytes();
            String steganogramFileName = Utils.dateFormat.format(new Date()) + "-steganogram.jpg";
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(steganogramFileName)));
            stream.write(bytes);
            stream.close();

            //Write encoded file to disk..
            String encodedFileName = System.getProperty("user.dir") + File.separator + Utils.dateFormat.format(new Date()) + "-encoded.jpg";

            Embed.embed(encodedFileName, steganogramFileName, password, 75, hiddenMessage);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            File encodedFile = new File(encodedFileName);

            headers.setContentLength(encodedFile.length());
            Path path = Paths.get(encodedFileName);
            return new HttpEntity<>(Files.readAllBytes(path), headers);

        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
