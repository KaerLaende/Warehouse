package services.impl;

import models.Socks;
import org.springframework.stereotype.Service;
import services.CheckExceptionService;


@Service
public class CheckExceptionServiceImpl implements CheckExceptionService {

    @Override
    public boolean validate(Socks socks) {
        return socks != null
                && socks.getColor() != null
                && socks.getSize() != null
                && socks.getQuantity() >= 0
                &&socks.getCottonPart() >= 0;
    }
    @Override
    public boolean isNegativeBalance(int balance, int number) {
        return balance < number;
    }

}
