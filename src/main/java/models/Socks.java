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
    @Min(1)
    @Max(100)
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
