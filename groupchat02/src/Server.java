import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket severSocket) {
        this.serverSocket = severSocket;
    }

    public void startServer(){
        try{

            while (!serverSocket.isClosed()){
               Socket socket = serverSocket.accept();
               System.out.println("A new client has joined");
               ClientHandler clientHandler;
                clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
               thread.start();
            }

        } catch (IOException e){

        }
    }
    public void closeServerSocket(){
        if (serverSocket != null){
            closeServerSocket();
        }
    }
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket= new ServerSocket(5555);
         Server server = new Server(serverSocket);
         server.startServer();
    }
}
