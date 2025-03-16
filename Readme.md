
JavaSockets is a simple, rudimentary implementation of a cli based chat application with custom implemented message broker.
  
  

A brief overview of the classes used:

Server.java: The server-side code for accepting and processing messages from the client.

Client.java: The client-side code for connecting to the server and exchanging messages.

MessageListener.java (Server): A thread for listening to messages from the client and adding them to the message queue. 

MessageResponder.java (Server): A thread for responding to messages from the queue.

MessageListener.java (Client): A thread for listening to messages from the server and adding them to the client's message queue.

MessageResponder.java (Client): A thread for sending messages to the server.(btw The client can also respond to the server's messages)
