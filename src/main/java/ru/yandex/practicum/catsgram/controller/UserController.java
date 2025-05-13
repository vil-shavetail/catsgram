package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (!users.isEmpty()) {
            users.values().stream().filter(newUser -> user.getEmail().equals(newUser.getEmail())).forEach(newUser -> {
                throw new DuplicatedDataException("Этот имейл уже используется");
            });
        }

        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;

    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        User oldUser = users.get(newUser.getId());
        if (users.containsKey(newUser.getId())) {
            // если пользователь найден и все условия соблюдены, обновляем данные пользователя
            if (newUser.getUsername() != null) {
                oldUser.setUsername(newUser.getUsername());
                return oldUser;
            }
            if (newUser.getPassword() != null) {
                oldUser.setPassword(newUser.getPassword());
                return oldUser;
            }
            if (newUser.getEmail() != null) {
                users.values().stream().filter(user -> !user.getId().equals(newUser.getId()) && user.getEmail().equals(newUser.getEmail())).forEach(user -> {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                });
                oldUser.setEmail(newUser.getEmail());
                return oldUser;
            }

        }

        return oldUser;
//        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }


    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;

    }

}
