package lobbymanager

import com.sun.net.httpserver.HttpServer

class Server {
    static final int PORT = 5150

    static void main(String[] args) {
        LobbyManager lobbyManager = new LobbyManagerClass()

        HttpServer.create(new InetSocketAddress(PORT), 0).with {
            println "Server is listening on port $PORT"

            createContext("/players") { http ->

                switch (http.requestMethod) {
                    case "NEW_LOBBY":
                        // call new lobby
                        break
                    case "ADD_PLAYER":
                        // call add player
                        break
                    default:
                        break
                }

                http.close()
            }
        }
    }

    private List<Level> levels
    private List<Player> players
}
