import com.sun.net.httpserver.HttpServer


int PORT = 1337
def levelMap = [:]

def checkLevel(String key, levelMp) {
    if (!levelMp.containsKey(key)) {
        println "Creating new level"
        Random rnd = new Random()
        int g_x = rnd.nextInt(101) - 50
        int g_y = rnd.nextInt(101) - 50
        println "Generated x:$g_x y:$g_y"
        levelMp[key] = new test(g_x, g_y)
    }
}

HttpServer.create(new InetSocketAddress(PORT), /*max backlog*/ 0).with {
    println "Server is listening on ${PORT}, hit Ctrl+C to exit."
    createContext("/debug") { http ->
        http.responseHeaders.add("Content-type", "text/plain")
        http.sendResponseHeaders(200, 0)
        checkLevel(http.remoteAddress.hostName, levelMap)
        http.responseBody.withWriter { out ->
            out << "${http.remoteAddress.hostName}\n"
            out << "${http.requestMethod}\n"
            out << "${http.requestBody}\n"
        }
        http.close()
        def level = levelMap[http.remoteAddress.hostName]
        int x = level.get_x()
        int y = level.get_y()
        println " ${http.remoteAddress.hostName}info: $x $y"
    }
    createContext("/get") { http ->
        checkLevel(http.remoteAddress.hostName, levelMap)
        def level = levelMap[http.remoteAddress.hostName]
        int x = level.get_x()
        int y = level.get_y()
        http.responseHeaders.add("Content-type", "text/plain")
        http.sendResponseHeaders(200, 0)
        http.responseBody.withWriter { out ->
            out << "$x $y"
        }
        http.close()
        println "${http.remoteAddress.hostName} info: $x $y"
    }
    createContext("/goal") { http ->
        checkLevel(http.remoteAddress.hostName, levelMap)
        def level = levelMap[http.remoteAddress.hostName]
        int g_x = level.get_g_x()
        int g_y = level.get_g_y()
        http.responseHeaders.add("Content-type", "text/plain")
        http.sendResponseHeaders(200, 0)
        http.responseBody.withWriter { out ->
            out << "$g_x $g_y"
        }
        http.close()
        println "${http.remoteAddress.hostName} info: $g_x $g_y"
    }
    createContext("/move") { http ->
        checkLevel(http.remoteAddress.hostName, levelMap)
        def level = levelMap[http.remoteAddress.hostName]
        http.responseHeaders.add("Content-type", "text/plain")
        if (http.requestMethod != "POST") {
            println "${http.requestMethod}\n"
            http.sendResponseHeaders(404, 0)
            http.close()
            return
        }
        http.sendResponseHeaders(200, 0)
        level.move("${http.requestBody}")
        http.close()
        int x = level.get_x()
        int y = level.get_y()
        println "${http.remoteAddress.hostName} info: $x $y"
    }
    createContext("/solution") { http ->
        try {
            checkLevel(http.remoteAddress.hostName, levelMap)
            def level = levelMap[http.remoteAddress.hostName]
            http.responseHeaders.add("Content-type", "text/plain")
            if (http.requestMethod != "POST") {
                println "Wrong method"
                http.sendResponseHeaders(404, 0)
                http.close()
                return
            }
            http.sendResponseHeaders(200, 0)
            println "Testing solution:"
            String sol = "${http.requestBody}"
            print sol
            def sharedData = new Binding(level: levelMap[http.remoteAddress.hostName])
            println "Created binding"
            def shell = new GroovyShell()
            println "Created shell"
            println "Parsing solution"
            def script = shell.parse(sol)
            println "Binding shared data"
            script.binding = sharedData
            println "Running shell"
            script.run()
            println "Extracting level"
            def lvl = script.getProperty('level')
            println "Checking results"
            if (lvl.get_x() == lvl.get_g_x() && lvl.get_y() == lvl.get_g_y()) {
                http.responseBody.withWriter { out ->
                    out << "Passed"
                    println "Success"
                }
            } else {
                http.responseBody.withWriter { out ->
                    out << "Failed"
                    println "Test failed"
                }
            }
            http.close()
        } catch (Exception e) {
            println(e)
        }
        println "${http.remoteAddress.hostName} info: $x $y"
    }
    createContext("/reset") { http ->
        if (http.requestMethod != "POST") {
            println "Wrong method"
            http.sendResponseHeaders(404, 0)
            http.close()
            return
        }
        Random rnd = new Random()
        int g_x = rnd.nextInt(101) - 50
        int g_y = rnd.nextInt(101) - 50
        levelMap[http.remoteAddress.hostName] = new test(_x, _y)
        http.responseHeaders.add("Content-type", "text/plain")
        http.sendResponseHeaders(200, 0)
        http.close()
        println "${http.remoteAddress.hostName} info: $g_x $g_y"
    }

    start()
}