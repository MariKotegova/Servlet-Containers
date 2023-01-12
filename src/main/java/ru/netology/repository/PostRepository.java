package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {
    public static ConcurrentHashMap<Long, String> pos = new ConcurrentHashMap<>();
    public AtomicLong counter = new AtomicLong(1);

    public List<Post> all() {
        List<Post> p = new ArrayList<>();
        for (long s : pos.keySet()) {
            p.add(new Post(s, pos.get(s)));
        }
        return p;
    }

    public Optional<Post> getById(long id) {
        if (pos.containsKey(id)) {
            return Optional.of(new Post(id, pos.get(id)));
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long count = counter.getAndIncrement();
            pos.put(count, post.getContent());
        }
        if (post.getId() != 0) {
            getById(post.getId()).orElseThrow(() -> new NotFoundException("Значение с таким id  отсутствует. "));
            pos.put(post.getId(), post.getContent());
            System.out.println("Значение id " + post.getId() + " изменено");
        }
        return post;
    }

    public void removeById(long id) {
        if (pos.containsKey(id)) {
            //можно выбрать действие в зависимости от потребностей
            pos.put(id, " Позиция не заполнена");// так очищу значение в конкретном id, id будет с пустым значением
            // pos.remove(id);   // так удалю всю позицию и конкретный id
            System.out.println("Позиция id " + id + " удалена");
        } else {
            System.out.println("Такого id нет в списке");
        }
    }
}
