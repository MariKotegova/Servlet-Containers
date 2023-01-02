package ru.netology.service;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.concurrent.ConcurrentHashMap;

public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public ConcurrentHashMap<Long, String> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id).orElseThrow(NotFoundException::new);
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long count = repository.counter += 1;
            repository.pos.put(count, post.getContent());
            return repository.save(post);
        } else {
            if (repository.pos.containsKey(post.getId())) {
                repository.pos.put(post.getId(), post.getContent());
                System.out.println("Значение id " + post.getId() + " изменено");
                return repository.save(post);
            } else {
                System.out.println("Значение с таким id  отсутствует. Выбирите из списка id от 1 до " + repository.counter);
                getById(post.getId());
                return repository.save(post);
            }
        }
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}

