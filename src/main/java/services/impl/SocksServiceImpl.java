package services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.NegativeBalanceException;
import exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Color;
import models.Size;
import models.Socks;
import org.springframework.stereotype.Service;
import services.FileService;
import services.CheckExceptionService;
import services.SocksService;

import java.util.*;

@Service
@Data
@AllArgsConstructor
public class SocksServiceImpl implements SocksService {
    private static List<Socks> socksList = new ArrayList<>();
    private final CheckExceptionService checkExceptionService;
    private final FileService fileService;

    /**
     *  Добавление новых носков на склад(List) всех носков
     * @param newSocks
     * @return добавленные носки
     */
    @Override
    public Socks addSocks(Socks newSocks) {
        //проверка на правильность заполнения параметров
        if (!checkExceptionService.validate(newSocks)){
            throw new ValidationException(newSocks.toString());
        }
        //временный носок =) что бы укоротить код в случае 'else'
        Socks oldSocks = searchInList(newSocks.getColor(), newSocks.getSize(), newSocks.getCottonPart());
        //если с такими парметрами носков нет - то добавляем новые носки в новую ячейку list
        if(searchInList(newSocks.getColor(), newSocks.getSize(), newSocks.getCottonPart()) == null){
            socksList.add(newSocks);
            return newSocks; // и возвращаем новый
        }else{
            //если носки с такими параметрами уже есть, то просто увеличиваем остаток у имеющихся
            searchInList(newSocks.getColor(), newSocks.getSize(), newSocks.getCottonPart()).setQuantity(oldSocks.getQuantity()+newSocks.getQuantity());
        }
        saveToFile(); //сохраняем в файл
        return searchInList(newSocks.getColor(), newSocks.getSize(), newSocks.getCottonPart());
    }

    /**
     * Поиск на складе по заданным параметрам носков
     * @return общее количество всех насков с этими параметрами
     */
    @Override
    public int getAllQuantitySocks(Color color, Size size, int cottonPartMin, int cottonPartMax) {
        fileService.init();
        int allQuantity = 0;
        for (Socks socks : socksList) {
            if (socks.getColor().equals(color) && socks.getSize().equals(size) && socks.getCottonPart() >= cottonPartMin && socks.getCottonPart() <= cottonPartMax) {
                allQuantity+=socks.getQuantity(); // накапливаем остаток
            }
        }
        return allQuantity;
    }

    /**
     * Вспомогательный метод поиска носков по параметрам среди всех на складе
     * @return
     */
    public Socks searchInList(Color color, Size size, int cottonPart) {
        for (Socks socks : socksList) {
            if(socks.getColor().equals(color) && socks.getSize().equals(size) && socks.getCottonPart() == cottonPart){
                return socks;
            }
        }
        return null;
    }

    /**
     * Показ всего списка
     */
    @Override
    public List<Socks> socksList(){
        return socksList;
    }


    @Override
    public Socks decreaseBalance(Color color, Size size, int cottonPart, int quantity){
        if(searchInList(color, size, cottonPart)!=null){ //если такие носки найдены
            //то делаем проверку на отрицательный баланс:
            if (checkExceptionService.isNegativeBalance((searchInList(color, size, cottonPart).getQuantity()),quantity)){
                throw new NegativeBalanceException();
            }
            searchInList(color, size, cottonPart).setQuantity(searchInList(color, size, cottonPart).getQuantity() - quantity);
            saveToFile();
            return searchInList(color, size, cottonPart);
        }
        return null; //не найдены
    }


    @Override
    public void saveToFile(){
        try {
            String json = new ObjectMapper().writeValueAsString(socksList);
            fileService.saveSocksToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}

