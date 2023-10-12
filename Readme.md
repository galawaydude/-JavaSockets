
JavaSockets is a simple, multi-threaded terminal based client-server chat application built in Java. This repository provides the source code for both the client and server applications, allowing users to establish a socket connection and exchange messages in a chat-like fashion. I've used concepts of sockets along with multithreading and blocking queues to achieve some additional functionality *(Presently this code, works by having the server and the client hosted on the same system i.e. local host)*.

  
  

A brief overview of the classes used:

Server.java: The server-side code for accepting and processing messages from the client.

Client.java: The client-side code for connecting to the server and exchanging messages.

MessageListener.java (Server): A thread for listening to messages from the client and adding them to the message queue. 

MessageResponder.java (Server): A thread for responding to messages from the queue.

MessageListener.java (Client): A thread for listening to messages from the server and adding them to the client's message queue.

MessageResponder.java (Client): A thread for sending messages to the server.(btw The client can also respond to the server's messages)