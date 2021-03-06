package pl.superjaba.servlet;

import org.apache.commons.lang3.StringUtils;
import pl.sda.file.FileOperations;
import pl.sda.file.User;
import pl.superjaba.controller.Controller;
import pl.superjaba.controller.CookieController;
import pl.superjaba.controller.LoginController;
import pl.superjaba.controller.UserController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RENT on 2017-03-01.
 */
public class HelloServlet extends HttpServlet {
    File file = new File("C:\\Users\\RENT\\Desktop\\test.txt");

    private Map<String, Controller> controllerMap;

    @Override
    public void init() throws ServletException { //dispatcher servlet
        this.controllerMap = new HashMap<String, Controller>();
        this.controllerMap.put("users", new UserController());
        this.controllerMap.put("login", new LoginController());
        this.controllerMap.put("cookie", new CookieController());
        this.controllerMap.put("default", (request, response) ->
                response.getWriter().write("<h1>Hello from default controller</h1>"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String requestURI = req.getRequestURI();
        String relativePath = requestURI.substring(StringUtils.ordinalIndexOf(requestURI, "/", 2) + 1); //StringUtills import in pom.xml
        Controller controller = controllerMap.get(relativePath);
        if (controller == null) {
            controller = controllerMap.get("default");
        }
        controller.handleGet(req, resp);

        PrintWriter writer = resp.getWriter();
        writer.write("<h1>Hello World</h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        String body = reader.readLine();
        String[] split = body.split(" ");

        User user = new User();
        user.setFirstName(split[0]);
        user.setLastName(split[1]);
        user.setAge(new Integer(split[2]));

        FileOperations.addUserToFile(user, file);

        resp.getWriter().write("Wszystko OK");
        resp.setStatus(201);
    }
}
