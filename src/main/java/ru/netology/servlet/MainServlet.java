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
    private final String METOD_GET = "GET";
    private final String METOD_POST = "POST";
    private final String METOD_DELETE = "DELETE";
    private final String PATH = "/api/posts";

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
            if (method.equals(METOD_GET) && path.equals(PATH)) { // возвращаем все posts
                controller.all(resp);// запрашиваем обьект респонс и выходим из обработчика
                return;
            }

            if (method.equals(METOD_GET) && path.matches(PATH + "/\\d+")) {
                final var id = idGet(path);
                controller.getById(id, resp);
                return;
            }

            if (method.equals(METOD_POST) && path.equals(PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }

            if (method.equals(METOD_DELETE) && path.matches(PATH + "/\\d+")) {
                final var id = idGet(path);
                controller.removeById(id, resp);
                return;
            }

            // если не попали ни в один из иф то вернем 404 ошибку
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public static long idGet(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}

