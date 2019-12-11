package lobbymanager

class Client {
    static final PORT = 5150

    static void main(String[] args) {
        def post = new URL("http://localhost:$PORT/players").openConnection()

        // Learn how to send something...
    }

}
