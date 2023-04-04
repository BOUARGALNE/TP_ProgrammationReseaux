import socket
import threading

def listen_to_response(socket):
    global listen
    while listen:
        data = socket.recv(1024).decode() # receive response
        print('' + data) # show in terminal

def client_program():
    host = "127.0.0.1" # Host name
    port = 1234 # socket server port number
    client_socket = socket.socket() # instantiate
    client_socket.connect((host, port)) # connect to the server
    thread=threading.Thread(target=listen_to_response, args=(client_socket,))
    thread.start()
    request = ""
    while request.lower().strip() != 'bye':
        request = input(" -> ")
        client_socket.send(request.encode()) # send message
    client_socket.close() # close the connection

listen = 1
if __name__ == '__main__':
    client_program()