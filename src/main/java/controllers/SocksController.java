package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
        @ApiResponse(responseCode = "200", description = "Успешный запрос"),
        @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
        @ApiResponse(responseCode = "404", description = "Результат запроса не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка программы")
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
    public ResponseEntity<Socks> addSocks(@Valid @RequestBody Socks socks) {
        return ResponseEntity.ok(socksService.addSocks(socks));
    }

    @GetMapping()
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
    @PutMapping()
    @Operation(
            summary = "Отпуск носков со склада",
            description = "введите параметры отпускаемых носков и их количество"
    )
    @Parameters(value = {
            @Parameter(name = "color", schema = @Schema(implementation = Color.class)),
            @Parameter(name = "size", schema = @Schema(implementation = Size.class)),
            @Parameter(name = "cottonPart", description = "доля хлопка от (1 до 100)"),
            @Parameter(name = "quantity", description = "колличество"),
    })

    public ResponseEntity<Void> decreaseSocks(@RequestParam Color color,
                                            @RequestParam Size size,
                                            @RequestParam int cottonPart,
                                            @RequestParam int quantity) {
        if (socksService.decreaseBalance(color,size,cottonPart, quantity)!=null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping()
    @Operation(
            summary = "Получение всех имеющихся носков"
    )
    public ResponseEntity<List<Socks>> getAllSocks() {
        return ResponseEntity.ok(socksService.socksList());
    }


    @DeleteMapping()
    @Operation(
            summary = "Списание брака со склада",
            description = "введите параметры испорченых носков и их количество"
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


