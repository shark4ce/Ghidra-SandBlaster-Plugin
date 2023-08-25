package sandblasterplugin.backgroundtasks.general;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileReaderTask extends AbstractTask<String> {
    private final File file;

    public FileReaderTask(File file, LoggerInterface logger) {
        super(logger);
        this.file = file;
    }

    @Override
    protected String doInBackground() throws Exception {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPORT);
            String content = decoder.decode(ByteBuffer.wrap(bytes)).toString().trim();
            
            // If decoding succeeds, return the content
            return content;
        } catch (IOException e) {
            // The file is probably binary
            return null;
        }
    }
}