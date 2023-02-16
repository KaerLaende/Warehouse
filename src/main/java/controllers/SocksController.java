package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.FileService;
import services.SocksService;


import java.util.List;

@RestController
@RequestMapping("/socks")
@Tag(name = "API склада носков", description = "CRUD - операции.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешно!"),
        @ApiResponse(responseCode = "400", description = "Не правильные параметры"),
        @ApiResponse(responseCode = "404", description = "Результ запроса не найден!"),
        @ApiResponse(responseCode = "500", description = "Noob'as накодил...")
})
public class SocksController {
    private final SocksService socksService;
    private final FileService fileService;

    public SocksController(SocksService socksService, FileService fileService) {
        this.socksService = socksService;
        this.fileService = fileService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление носков на склад"
    )
    public ResponseEntity<Socks> addSocks(@RequestBody Socks socks) {
        return ResponseEntity.ok(socksService.addSocks(socks));
    }

    @GetMapping("/param")
    @Operation(
            summary = "Получение общего остатка носков по их параметрам "
    )
    @Parameters(value = {
            @Parameter(name = "color", schema = @Schema(implementation = Color.class)),
            @Parameter(name = "size", schema = @Schema(implementation = Size.class)),
            @Parameter(name = "cottonMin", description = "минимальная доля хлопка от (1 до 100)"),
            @Parameter(name = "cottonMax", description = "максимальная доля хлопка от (1 до 100)")
    })

    public ResponseEntity<Integer> getSocks(@RequestParam Color color,
                                            @RequestParam Size size,
                                            @RequestParam(required = false,defaultValue = "1") int cottonMin,
                                            @RequestParam(required = false,defaultValue = "100") int cottonMax) {
        return ResponseEntity.ok(socksService.getAllQuantitySocks(color, size, cottonMin, cottonMax));

    }

    @GetMapping()
    @Operation(
            summary = "Получение всех имеющихся носков"
    )
    public ResponseEntity<List<Socks>> getAllSocks() {
        fileService.init();
        return ResponseEntity.ok(socksService.socksList());
    }


    @DeleteMapping("{id}")
    @Operation(
            summary = "удалить существующие носки",
            description = "введите ID номер носков, которые хотите удалить"
    )
    @Parameters(value = {
            @Parameter(name = "color", schema = @Schema(implementation = Color.class)),
            @Parameter(name = "size", schema = @Schema(implementation = Size.class)),
            @Parameter(name = "cottonPart", description = "доля хлопка от (1 до 100)"),
            @Parameter(name = "quantity", description = "колличество"),
    })

    public ResponseEntity<Void> deleteSocks(@RequestParam Color color,
                                            @RequestParam Size size,
                                            @RequestParam int cottonPart,
                                            @RequestParam int quantity) {
        if (socksService.decreaseBalance(color,size,cottonPart, quantity)!=null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}


