import tickets.WeatherController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        TicketsController controller = new TicketsController();
//        controller.start();

        WeatherController weatherController = new WeatherController();
        weatherController.start();
    }
}
