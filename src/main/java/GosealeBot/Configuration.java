package GosealeBot;

import io.github.cdimascio.dotenv.Dotenv;

public class Configuration {

    private static Dotenv dotenv = null;

    public static String get(String key) {
        return System.getenv(key.toUpperCase());
    }

    public static String getVar(String key) {
        String retorno = null;
        try {
            dotenv = Dotenv.load();
            retorno = dotenv.get(key.toUpperCase());
        } catch (Exception e) {
            retorno = System.getenv(key);
        }
        return retorno;
    }

}
