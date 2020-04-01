import groovy.json.JsonOutput
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


String lvlSrc = new File("level_examples/simple_plane_lvl.groovy").text
String name1 = "simple_plane"
String name2 = "another_plane"
String res1 = "resource1.txt"
String res2 = "resource2.txt"

def addLvl = { String lvlName, String source ->
    def post = new URL("http://localhost:1337/addLevel/" + lvlName).openConnection();
    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    post.getOutputStream().write(source.getBytes("UTF-8"))
    println(post.getInputStream().getText());

}
def addRes = { String lvlName, String resName, byte[] content ->
    def post = new URL("http://localhost:1337/addResource/" + lvlName + "/" + resName).openConnection();
    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "")
    post.getOutputStream().write(content)
    println(post.getInputStream().getText());
}
def delLvl = { String lvlName ->
    def post = new URL("http://localhost:1337/removeLevel/" + lvlName).openConnection();
    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    post.getOutputStream().write()
    println(post.getInputStream().getText());

}

addLvl(name1, lvlSrc)
addRes(name1, res1, lvlSrc.getBytes("UTF-8"))
addLvl(name2, lvlSrc)
addRes(name2, res2, lvlSrc.getBytes("UTF-8"))
sleep(2000)
delLvl(name2)
