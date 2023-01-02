package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    //    @Override
//            protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            resp.getWriter().println("Hello!!!"); // так что то отправим в браузер
//            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);// так я могу сказать чтоб всегда выдавал имнно эту ошибку
//            resp.setHeader("Content-Type", "application/json");// добавить заголовок
//        }
    private PostController controller;

    // метод для создания запроса
    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    //метод лоя обработки запроса
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        try {
            final var path = req.getRequestURI();  // получаем путь
            System.out.println(path);
            final var method = req.getMethod();    // получаем метод

            // primitive routing , /api так пишут чтоб понимать что это бекенд.
            if (method.equals("GET") && path.equals("/api/posts")) { // возвращаем все posts
                controller.all(resp);// запрашиваем обьект респонс и выходим из обработчика
                return;
            }

            // так получаем один конкретный пост по ид
            if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.getById(id, resp);
                return;
            }

            if (method.equals("POST") && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }

            if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.removeById(id, resp);
                return;
            }

            // если не попали ни в один из иф то вернем 404 ошибку
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

