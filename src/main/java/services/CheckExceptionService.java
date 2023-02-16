package services;


import models.Socks;

public interface CheckExceptionService {


 boolean validate(Socks socks);

 boolean isNegativeBalance(int balance, int number);
}
