package shanghai.shu.remoting.http.server;

import org.springframework.util.Base64Utils;
import shanghai.shu.common.serialize.hessian.HessianCodecUtil;
import shanghai.shu.remoting.ResponseClientUtil;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.remoting.exchange.model.Response;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadPoolExecutor;

public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync();
        req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED",true);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) req.getServletContext().getAttribute("executor");
        ServletInputStream inputStream = req.getInputStream();
        inputStream.setReadListener(new ReadListener() {
            @Override
            public void onDataAvailable() throws IOException {

            }

            @Override
            public void onAllDataRead() throws IOException {
                executor.execute(() -> {
                    try {
                        String result = invoke(req);
                        PrintWriter writer = asyncContext.getResponse().getWriter();
                        writer.write(result);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    asyncContext.complete();
                });
            }

            @Override
            public void onError(Throwable t) {
                asyncContext.complete();
            }
        });
    }

    private String invoke(HttpServletRequest req) throws IOException{
        String q = req.getParameter("q");
        byte[] reqBody = Base64Utils.decodeFromString(q);
        Request request = (Request) HessianCodecUtil.decode(reqBody);
        Response response = new Response();
        response = ResponseClientUtil.response(request, response);
        byte[] respBody = HessianCodecUtil.encode(response);
        String result = Base64Utils.encodeToString(respBody);
        return result;
    }
}
