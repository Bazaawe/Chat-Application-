import java.io.*;
import java.util.ArrayList;
import java.net.Socket;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientName;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server: "+ clientName + " has entered the chat.");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public ClientHandler() {
        this(null);
    }

    /*public ClientHandler(Socket socket) {

    }*/


    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try {
                if (!clientHandler.clientName.equals(clientName)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: "+ clientName + " has left the chat");
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
