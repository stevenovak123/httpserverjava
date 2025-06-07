# HTTP Server built in java

building this as practice to learn Computer science concepts. File handiling, networking, multithreading etc.
Following a few YouTube videos and [RFC 7230](https://datatracker.ietf.org/doc/html/rfc7230#autoid-16),[RFC 7231](https://datatracker.ietf.org/doc/html/rfc7231#autoid-27),[RFC 7232](https://datatracker.ietf.org/doc/html/rfc7232) 
For now only working to accept HTTP/1.1. 
Below is my learnings / log of what I am doing.
---

## How does http server work

Http server is basically a computer that needs to be connected to a network. On this network the server needs to be
listening to at least one port. We can make it listen to any port we like but these are the usual ports that are used for traffic. Client and server
need to establish connection before sending and accepting request.
Once connection is established then requests are processed and then sent back to the client; then the request is
completed and then server closes the connections.

- Encrypted and unencrypted requests.
- Unencrypted at port 80 and encrypted at port 443.


---
## What do I need to do?

- Read configuration files.
- Open a socket to listen at a port.
- Read Request Messages using HTTP protocol.
- Open and read files from the filesystem.
- Write Responses to the client.
---

## Dependencies

- Added Jackson packages to map json to pojo.
- Added SLF4J API and logback for logging.
- Junit jupiter to test the parser.
---
## Configuration
the configuration lives under `resources/http.json` & `config/Configuration.java`.
- We define the `port` and `webroot `. Webroot is where the filesystem of the server is present.
- `config/ConfigurationManager` manages all the config related tasks.
- Created a `HttpConfigurationException` class to handle the exceptions.
- Completed the Basic Setup and accessed it via `Main`
---
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
---
## Parsers
- There are two types of parsers. Lexer and Lexerless.
- Lexers scan the request with a tokenizer and then parse the request. 
- The Stream is first converted to tokens then converted to the request.
- Lexerless parser take the data as it comes and parses straight-away.
- It helps detect error sooner and can be instantly dropped.
- Built out a JUnit case. To test whether parser will work correctly or not. 
---

## HTTP package
- Made an `HttpMessage` class which is an abstract class as both Request and Responses are technically a message.
- `HttpStatusCode` and `HttpMethod` enums to take care of the valid requests that can be made. 
- `HttpParsingExceptions` to have custom exception handling.
- `HttpRequest` to handle requests.
---