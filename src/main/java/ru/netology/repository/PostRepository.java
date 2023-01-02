package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
    ConcurrentHashMap<Long, String> pos = new ConcurrentHashMap<>();
    public long counter = 0;

    public ConcurrentHashMap<Long, String> all() {
        return pos;
    }

    public Optional<Post> getById(long id) {
        if (pos.containsKey(id)) {
            return Optional.of(new Post(id, pos.get(id)));
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long count = counter += 1;
            pos.put(count, post.getContent());
        } else if (post.getId() != 0) {
            if (pos.containsKey(post.getId())) {
                pos.put(post.getId(), post.getContent());
                System.out.println("Значение id " + post.getId() + " изменено");
            } else {
                System.out.println("Значение с таким id  отсутствует. Выбирите из списка id от 1 до " + counter);
            }
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
