package ma.enset.MultiThreadsBlocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiThreadServer extends Thread {
    List<Conversation> conversations= new ArrayList<>();
    int clientsCount;
    public static void main(String[] args) {
        new MultiThreadServer().start();

    }
    @Override
    public void run() {
        System.out.println("the server is waiting..");
        try {
            ServerSocket serverSocket=new ServerSocket(1234);
            while (true){
                Socket socket = serverSocket.accept();
                ++clientsCount;
                Conversation conversation=new Conversation(socket,clientsCount);
                conversations.add(conversation);
                conversation.start();
            }
        } catch ( IOException e) { throw new RuntimeException(e); }
    }
    class Conversation extends Thread
    {
        private Socket socket;
        private int clientId;
        Conversation(Socket socket,int clientId) {
            this.socket = socket;
            this.clientId=clientId;
        }
        @Override
        public void run() {
            try {
                InputStream is=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader bufferedReader=new BufferedReader(isr);
                PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);
                String ip=socket.getRemoteSocketAddress().toString();
                System.out.println("New Client id = "+clientId+" IP= "+ip);
                printWriter.println("Welcome your id = "+clientId);
                printWriter.println("entrer une phrase");
                String request;
                while ((request=bufferedReader.readLine())!=null){
                    List<Integer>  clientsto= new ArrayList<>();
                    String message;
                    if (request.contains("=>")){
                        String[] items=request.split("=>");
                        String clients=items[0];
                         message=items[1];
                        if (clients.contains(",")){
                            String[] listclientsId=clients.split(",");
                            for (String id:listclientsId) {
                                clientsto.add(Integer.parseInt(id));
                            }
                        }else {
                            clientsto.add(Integer.parseInt(clients));
                        }

                    }else{
                    clientsto=conversations.stream().map(c->c.clientId).collect(Collectors.toList());
                     message=request;
                    }
                    broadcastMessage(request,this,clientsto);


                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }}
    public void broadcastMessage(String message,Conversation from,List<Integer> clients){
        try{
        for (Conversation conversation: conversations) {
            if (conversation !=from && clients.contains(conversation.clientId)){
                Socket socket=conversation.socket;
                OutputStream outputStream=socket.getOutputStream();
                PrintWriter printWriter=new PrintWriter(outputStream,true);
                printWriter.println(message);
            }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
