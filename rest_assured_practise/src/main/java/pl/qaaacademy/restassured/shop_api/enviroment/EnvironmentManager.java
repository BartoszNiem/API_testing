package pl.qaaacademy.restassured.shop_api.enviroment;

import java.util.HashMap;

public class EnvironmentManager {

    private static HashMap<String, Environment> envs = new HashMap<>();
    private static Environment defaultEnvironment = new Environment("http://localhost:3000");

    static{
        envs.put("dev", defaultEnvironment);
    }

    public static Environment getEnvironment(String env) {
        System.out.println("Environment parameter - " + env);
        return envs.getOrDefault(env, defaultEnvironment);
    }
}
