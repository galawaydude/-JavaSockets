import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class MessageListener extends Thread {
    private BufferedReader bufferedReader;

    MessageListener(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String serverMessage = bufferedReader.readLine();
                if (serverMessage == null) {
                    break;
                }
                if (!serverMessage.startsWith("Server: Message received:")) {
                    System.out.println("Server: " + serverMessage);
                }
            }
        } catch (IOException e) {
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
                String userMessage = messageQueue.take();
                bufferedWriter.write(userMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                if (userMessage.equalsIgnoreCase("BYE")) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

        try {
            socket = new Socket("localhost", 1234);
            System.out.println("Connected to Server");

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            MessageListener MessageListener = new MessageListener(bufferedReader);
            MessageResponder MessageResponder = new MessageResponder(bufferedWriter, messageQueue);

            MessageListener.start();
            MessageResponder.start();

            while (true) {
                BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
                String userMessage = userInputReader.readLine();
                messageQueue.put(userMessage);
                if (userMessage.equalsIgnoreCase("BYE")) {
                    break;
                }
            }

            MessageResponder.join();
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
