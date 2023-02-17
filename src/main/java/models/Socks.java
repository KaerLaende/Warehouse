package models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;




@Data
@AllArgsConstructor
public class Socks {
    private Color color;
    private Size size;
    @Min(value = 1, message = "Процент содержания хлопка не может быть меньше 1")
    @Max(value = 100, message = "Процент содержания хлопка не может быть больше 100")
    private int cottonPart;
    private int quantity;


    @Override
    public String toString() {

        return "Носки: " + color + " размером " +
                size + "Содержание хлопка" + cottonPart + "\n" +
                "КОЛИЧЕСТВО: " + quantity + " пар." + "\n" +
                "Инструкция по приготовлению:\n";
    }
}
