import groovy.json.JsonOutput

String fileContentsCorrect = new File('solution_spl.groovy').text
String fileContentsWrong = new File('wrong_solution_spl.groovy').text

def start = System.currentTimeMillis()
int count = 1
for (int i = 0; i < count; i++) {
    def post = new URL("http://localhost:1337/simulate").openConnection();
    def jsonSol = JsonOutput.toJson(fileContentsCorrect)
    def jsonWrong = JsonOutput.toJson(fileContentsWrong)
    String message = "{\"level\":\"simple_plane\", \"solutions\":[" + jsonSol + "," + jsonWrong + "]}"
    println(message)

    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    println()
    post.getOutputStream().write(message.getBytes("UTF-8"));
    println(post.getInputStream().getText());
    //println(i)
}
def end = System.currentTimeMillis()
println("Count: " + count)
println("Total time: " + (end - start).toDouble() / 1000)
println("Average time: " + (end - start).toDouble() / (1000 * count))
