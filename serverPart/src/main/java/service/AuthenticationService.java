package service;
//TODO шифрование пароля



public class AuthenticationService  {


    public static String authenticationAlgorithm(String login, String pass) {
        return DataBaseService.authentication(login, pass);
    }
}
