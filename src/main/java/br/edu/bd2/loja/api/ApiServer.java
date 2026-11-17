package br.edu.bd2.loja.api;

import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public final class ApiServer {

    private static final String SWAGGER_UI_WEBJAR = "META-INF/resources/webjars/swagger-ui/3.52.5/";

    private static final String PAGINA_SWAGGER = "<!DOCTYPE html><html lang=\"pt-BR\"><head><meta charset=\"utf-8\">"
            + "<title>Loja de Eletrônicos - API</title>"
            + "<link rel=\"stylesheet\" href=\"/swagger-ui/swagger-ui.css\"></head><body><div id=\"swagger-ui\"></div>"
            + "<script src=\"/swagger-ui/swagger-ui-bundle.js\"></script>"
            + "<script>window.onload=function(){SwaggerUIBundle({url:'/api/openapi.json',dom_id:'#swagger-ui'});};</script>"
            + "</body></html>";

    private ApiServer() { }

    public static void iniciar(int porta) throws Exception {
        URL webjar = ApiServer.class.getClassLoader().getResource(SWAGGER_UI_WEBJAR);
        if (webjar == null) {
            throw new IllegalStateException("Swagger UI nao encontrado no classpath");
        }

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        ctx.setContextPath("/");

        ctx.addServlet(new ServletHolder(new ServletContainer(new ApiConfig())), "/api/*");

        ServletHolder arquivosUi = new ServletHolder("swagger-ui-arquivos", DefaultServlet.class);
        arquivosUi.setInitParameter("resourceBase", webjar.toExternalForm());
        arquivosUi.setInitParameter("dirAllowed", "false");
        ctx.addServlet(arquivosUi, "/swagger-ui/*");

        ctx.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().write(PAGINA_SWAGGER);
            }
        }), "/swagger");

        ctx.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                resp.sendRedirect("/swagger");
            }
        }), "");

        Server server = new Server(porta);
        server.setHandler(ctx);
        server.start();
        System.out.println("API no ar: http://localhost:" + porta + "/swagger  (OpenAPI em /api/openapi.json)");
        server.join();
    }
}
