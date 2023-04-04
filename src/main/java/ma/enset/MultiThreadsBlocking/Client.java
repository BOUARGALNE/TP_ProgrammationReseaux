package ma.enset.MultiThreadsBlocking;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        Socket socket=new Socket("localhost",1234);
        InputStream is=socket.getInputStream();
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader bufferedReader=new BufferedReader(isr);
        OutputStream os =socket.getOutputStream();
        PrintWriter printWriter=new PrintWriter(os,true);
        new Thread(()->{
            try {
                String response;
                while ( (response=bufferedReader.readLine())!=null){

                    System.out.println(response);
                }
            }catch (Exception E){
                System.out.println(E.getMessage());
            }

        }).start();
        while (true){
            String request =scanner.nextLine();
            printWriter.println(request);
        }

    }

}
