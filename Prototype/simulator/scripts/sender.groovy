import groovy.json.JsonOutput
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


if (args.size() < 1) System.exit(-1)
ArrayList<String> solutions = new ArrayList<>()
if (args.size() == 1) {
    String fileContentsWrong = new File('wrong_solution_spl.groovy').text
    String fileContentsCorrect = new File('solution_spl.groovy').text
    solutions.add(fileContentsWrong)
    solutions.add(fileContentsCorrect)
} else {
    for (int i = 1; i < args.size(); i++) {
        solutions.add(new File(args[i]).text)
    }
}
def start = System.currentTimeMillis()
int count = args[0] as int
String message = "{\"level\":\"simple_plane\", \"solutions\":" + JsonOutput.toJson(solutions) + "}"
def makeReq = { int i ->
    def post = new URL("http://localhost:1337/simulate").openConnection();
    //println(message)
    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    //println()
    post.getOutputStream().write(message.getBytes("UTF-8"));
    println(post.getInputStream().getText());
    println(i)
}

def pool = Executors.newFixedThreadPool(4)
(1..count).each { num ->
    pool.submit({ makeReq(num) })
}
pool.shutdown()
pool.awaitTermination(120, TimeUnit.SECONDS)

def end = System.currentTimeMillis()
println("Count: " + count)
println("Total time: " + (end - start).toDouble() / 1000)
println("Average time: " + (end - start).toDouble() / (1000 * count))
