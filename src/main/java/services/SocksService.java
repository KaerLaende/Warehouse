package services;

import models.Color;
import models.Size;
import models.Socks;

import java.util.List;

public interface SocksService {
    Socks addSocks(Socks Socks);

    int getAllQuantitySocks(Color color, Size size, int cottonPartMin, int cottonPartMax);

    List<Socks> socksList();

    Socks decreaseBalance(Color color, Size size, int cottonPart, int quantity);

    void saveToFile();
}
