package services;

import jakarta.annotation.PostConstruct;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileService {


    boolean saveSocksToFile(String json);

    String readSocksFromFile();

    boolean cleanSocksDataFile();

    File getSocksDataFile();

    void importSocksDataFile(MultipartFile file) throws IOException;

    Path createTempFile(String suffix);

    Path createSocksTextFile() throws IOException;

    @PostConstruct
    void init();
}
