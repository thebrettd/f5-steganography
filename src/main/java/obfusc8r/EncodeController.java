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

            String encodedFileName = System.getProperty("user.dir") + File.separator + Utils.dateFormat.format(new Date()) + "-encoded.jpg";

            Embed.embed(encodedFileName, file.getName(), new BufferedInputStream(new ByteArrayInputStream(file.getBytes())), password, 75, new ByteArrayInputStream(message.getBytes()));

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
