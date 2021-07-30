//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;


import service.DataBaseService;

import java.util.ArrayList;
import java.util.List;

/*
для первого запска сервера
1. создаем БД с пользовалтелями
*/
public class InstallServerCloud {
  //  private static final Logger LOGGER = LogManager.getLogger(InstallChat.class.getName());
    public static void main(String[] args) {
     //   LOGGER.debug("Creating DB");
        DataBaseService.createUserTable();

        List<String[]> users = new ArrayList <>();
        users.add(new String[]{"A", "A", "Alex"});
        users.add(new String[]{"B", "B", "Bul"});
        users.add(new String[]{"C", "C", "Carl"});
        for (int i = 1; i < 1000; i++) {
            users.add(new String[]{"A" + i, "A" + i, "Alex_" + i});
        }

        DataBaseService.createUsers(users);
     //   LOGGER.debug("DB created");
    }
}
