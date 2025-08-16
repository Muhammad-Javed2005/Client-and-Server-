import java.io.*;
import java.net.*;

public class ChatServer {
    private ServerSocket serverSocket;

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Javed's Server started on port " + port);
    }

    public void start() throws IOException {
        System.out.println("Waiting for clients to connect...");
        while (true) { // infinite loop for multiple clients
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    // Each client handled in its own thread
    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // First message from client = name
                out.println("Enter your name:");
                clientName = in.readLine();
                System.out.println(clientName + " connected.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("quit")) {
                        System.out.println(clientName + " disconnected.");
                        break;
                    }
                    System.out.println(clientName + " says: " + message);
                    out.println("<---Message received by Javed's server--->");
                }

                socket.close();
            } catch (IOException e) {
                System.out.println("Error with " + clientName + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer(9090);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
