package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Msg;
import util.DbHelper;
import util.GsonHelper;

/**
 * Servlet implementation class Chat
 */
@WebServlet("/chat")
public class Chat extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static ConcurrentHashMap<Long, List<Msg>> pendingMsg;

    public Chat() {
        super();
        pendingMsg = new ConcurrentHashMap<>();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long userId = Long.valueOf(request.getParameter("userId"));
        if (!pendingMsg.containsKey(userId)) {
            registerUser(userId);
        }

        List<Msg> messages = pendingMsg.get(userId);
        String messagesString = new Gson().toJson(messages);
        if (messages.size()!=0) {
            System.out.println("Send " + messages.size() + " messages to " + userId);            
        }
        messages.clear();
        response.getWriter().write(messagesString);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder content = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        Msg msg = new GsonHelper().restoreMsg(content.toString());
        msg.sendToReceive();
        new DbHelper().insertMessage(msg);
        long receiverId = msg.getReceiver().getUserId();
        if (!pendingMsg.containsKey(receiverId)) {
            registerUser(receiverId);
        }
        pendingMsg.get(receiverId).add(msg);
        System.out.println("Receive message from " + msg.getSender().getUserId() + " to " +msg.getReceiver().getUserId());
        response.getWriter().write("SUCCESS");
    }

    private void registerUser(Long userId) {
        List<Msg> messages = new ArrayList<>();
        pendingMsg.put(userId, messages);
    }
    
}
