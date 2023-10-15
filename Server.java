import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class MessageListener extends Thread {
    private BufferedReader bufferedReader;
    private BlockingQueue<String> messageQueue;

    MessageListener(BufferedReader bufferedReader, BlockingQueue<String> messageQueue) {
        this.bufferedReader = bufferedReader;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msgFromClient = bufferedReader.readLine();
                if (msgFromClient == null) {
                    break;
                }
                System.out.println("Client: " + msgFromClient);
                messageQueue.put(msgFromClient);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MessageResponder extends Thread {
    private BufferedWriter bufferedWriter;
    private BlockingQueue<String> messageQueue;

    MessageResponder(BufferedWriter bufferedWriter, BlockingQueue<String> messageQueue) {
        this.bufferedWriter = bufferedWriter;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msgToClient = messageQueue.take();

                if (!msgToClient.equalsIgnoreCase("BYE")) {

                    bufferedWriter.write("Server: Message received: " + msgToClient);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

                if (msgToClient.equalsIgnoreCase("BYE")) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        Scanner scanner = null;

        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

        System.out.println("Initiating Server");
        System.out.println("Waiting for Clients");
        try {
            serverSocket = new ServerSocket(1234);
            socket = serverSocket.accept();
            System.out.println("Connection Established");

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            scanner = new Scanner(System.in);

            MessageListener messageListener = new MessageListener(bufferedReader, messageQueue);
            MessageResponder messageResponder = new MessageResponder(bufferedWriter, messageQueue);

            messageListener.start();
            messageResponder.start();

            while (true) {
                String msgToClient = scanner.nextLine();
                bufferedWriter.write(msgToClient);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if (msgToClient.equalsIgnoreCase("BYE")) {
                    break;
                }
            }

            messageResponder.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
