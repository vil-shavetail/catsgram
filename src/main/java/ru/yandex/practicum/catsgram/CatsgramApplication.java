package ru.yandex.practicum.catsgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class CatsgramApplication {
    public static void main(String[] args) {
//        final Gson gson = new Gson();
//        final Scanner scanner = new Scanner(System.in);
//        System.out.print("Введите JSON => ");
//        final String input = scanner.nextLine();
//        try {
//            gson.fromJson(input, Map.class);
//            System.out.println("Был введён корректный JSON");
//        } catch (JsonSyntaxException exception) {
//            System.out.println("Был введён некорректный JSON");
//        }
        SpringApplication.run(CatsgramApplication.class, args);
    }
}

