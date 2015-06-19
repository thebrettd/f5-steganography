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
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class EncodeController {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

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
            String hiddenMessage = dateFormat.format(new Date()) + "-hiddenMessage.txt";
            BufferedOutputStream hiddenStream = new BufferedOutputStream(new FileOutputStream(new File(hiddenMessage)));
            hiddenStream.write(message.getBytes());
            hiddenStream.close();

            String[] args = new String[8];
            args[0] = "-e";
            args[1] = hiddenMessage;
            args[2] = "-p";
            args[3] = password;
            args[4] = "-q";
            args[5] = "75";

            //Write uploaded file to disk.
            byte[] bytes = file.getBytes();
            String fileName = dateFormat.format(new Date()) + "-steganogram.jpg";
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
            stream.write(bytes);
            stream.close();
            args[6] = fileName;

            //Write encoded file to disk..
            String encodedFileName = System.getProperty("user.dir") + File.separator + dateFormat.format(new Date()) + "-encoded.jpg";
            args[7] = encodedFileName;

            Embed.embedMain(args);

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
