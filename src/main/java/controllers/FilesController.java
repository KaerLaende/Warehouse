package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import services.FileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/files")
@Tag(name = "Файлы", description = "сохранить/загрузить")
public class FilesController {
    private final FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }


    @GetMapping("/exportSocks")
    @Operation(
            summary = "загрузить файл с носками")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "файл загружен"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "файла нет"
            )
    })
    public ResponseEntity<InputStreamResource> downloadSocks() throws FileNotFoundException {
        File file = fileService.getSocksDataFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Эндпоинт загрузки и замены файла с носками
     *
     * @param file json с носками
     * @return заменяет сохраненный на жестком (локальном) диске файл с носками на новый
     */
    @PostMapping(value = "/importSocks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadSocksFile(@RequestParam MultipartFile file) {
        try {
            fileService.importSocksDataFile(file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
