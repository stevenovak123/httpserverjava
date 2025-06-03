# Http Server built in java

building this as practice to learn Computer science concepts. File handiling, networking etc.

---

## How does http server work

Http server is basically a computer that needs to be connected to a network. On this network the server needs to be
listening to atleast one port.

- Encrypted and unencrypted requests.
- Unencrypted at port 80 and encrypted at port 443.

We can make it listen to any port we like but these are the usual ports that are used for traffic. Client and server
need to establish connection before sending and accepting request.
Once connection is established then requests are processed and then sent back to the client; then the request is
completed and then server closes the connections.

## What do I need to do?

- Read configuration files.
- Open a socket to listen at a port.
- Read Request Messages using HTTP protocol.
- Open and read files from the filesystem.
- Write Responses to the client.

## Dependencies

- Added Jackson packages to map json to pojo.
- Added SLF4J API and logback for logging.

## Configuration

the configuration lives under `resources/http.json` & `config/Configuration.java`.

- We define the `port` and `webroot `. Webroot is where the filesystem of the server is present.
- `config/ConfigurationManager` manages all the config related tasks.
- Created a `HttpConfigurationException` class to handle the exceptions.
- Completed the Basic Setup and accessed it via `Main`

## Sockets

Need to open sockets to serve pages using the TCP connection/protocol.

- The sockets need to understand the HTTP protocol.
- Input stream reads input and output stream writes output to the client.
- Multithreaded to accept multiple connections.
- Response contains a `CRLF` - Carriage Return line feed.
- CRLF signifies end of a line. represented as `\r\n`.
- Using Multi-threading, we can open multiple sockets.
- `ServerListenerThread` accepts connections and continues to only accept connection.
- `HttpConnectionWorkerThread` handles taking care of the requests/messages.