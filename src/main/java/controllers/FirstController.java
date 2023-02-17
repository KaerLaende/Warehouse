package controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name= "Информация", description = "о программе")
public class FirstController {
@GetMapping("/")
    public String printHello(){
        return "Привет! Приложение запущено";
    }
    @GetMapping("/info")
    public String printInfo(){
        return
                "<h1> style=\"text-align: center\" Складской учет </h1>"+
                        "<h4> на примере носков </h4>"+
                        "<ol><li>Список операций:" +
                        "<li>Добавить новый товар на склад."+
                        "<li>Узнать количество на складе."+
                        "<li>Осуществить списание или приход на склад" +
                        "<li>Списать брак или испорченный товар"+
                        "<li>Выгрузить остаток в файл" +

                '•'+" имя ученика: Вохминов Кирилл<br>" +'•'+
                " название проекта: Warehouse<br>" +'•'+
                " дата создания проекта: 15.02.2023г.<br>" +'•';
    }
}
