package lobbymanager

import com.sun.net.httpserver.HttpServer

class Server {
    static final int PORT = 5150

    static void main(String[] args) {
        HttpServer.create(new InetSocketAddress(PORT), 0)
    }
}
