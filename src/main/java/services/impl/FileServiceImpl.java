package services.impl;

import jakarta.annotation.PostConstruct;
import models.Socks;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import services.FileService;
import services.SocksService;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class FileServiceImpl implements FileService {

    //Путь и имя для сохранения файлов с остатками носков
    @Value(value = "${path.to.data.files}")
    private String dataFilePath;
    @Value(value = "${name.of.data.file}")
    private String getDataFileName;

    private final SocksService socksService;

    public FileServiceImpl(SocksService socksService) {
        this.socksService = socksService;
    }

    @Override
    public boolean saveSocksToFile(String json){
        try {
            cleanSocksDataFile();
            Files.writeString(Path.of(dataFilePath, getDataFileName),json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public String readSocksFromFile(){
        try {
            return  Files.readString(Path.of(dataFilePath, getDataFileName));
        } catch (IOException e) {
            e.printStackTrace();;
        }
        return null;
    }
    @Override
    public boolean cleanSocksDataFile(){
        try {
            Files.deleteIfExists(Path.of(dataFilePath, getDataFileName));
            Files.createFile(Path.of(dataFilePath, getDataFileName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }


    @Override
    public File getSocksDataFile(){
        return new File(dataFilePath+ "/"+ getDataFileName);
    }

    @Override
    public void importSocksDataFile(MultipartFile file) throws IOException {
        cleanSocksDataFile();
        File dataFile = getSocksDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
        } catch (IOException e) {
            throw new IOException();
        }
    }

    /**
     * Загрузка данных на старте приложения
     */
    @Override
    @PostConstruct
    public void init() {
        try{
            readSocksFromFile();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
