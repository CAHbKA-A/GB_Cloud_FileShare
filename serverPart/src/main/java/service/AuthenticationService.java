package service;
//TODO шифрование пароля


import interfaces.AuthenticationInt;

public class AuthenticationService implements AuthenticationInt {

    @Override
    public String authenticationAlgorithm(String login, String pass) {
        return DataBaseService.authentication(login, pass);
    }
}
