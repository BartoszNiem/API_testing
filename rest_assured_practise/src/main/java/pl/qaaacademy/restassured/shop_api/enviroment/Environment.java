package pl.qaaacademy.restassured.shop_api.enviroment;

public class Environment {
    private String host;

    public Environment(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
