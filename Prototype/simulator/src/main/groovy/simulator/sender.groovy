package simulator

import groovy.json.JsonOutput

String fileContents = new File('solution_spl.groovy').text

def start = System.currentTimeMillis()
int count = 1
for (int i = 0; i < count; i++) {
    def post = new URL("http://localhost:1337/simulate").openConnection();
    def jsonSol = JsonOutput.toJson(fileContents)
    String message = "{\"level\":\"simple_plane\", \"solutions\":[" + jsonSol + "," + jsonSol + "]}"
    println(message)

    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    post.getOutputStream().write(message.getBytes("UTF-8"));
    def postRC = post.getResponseCode();
    println(postRC);
    if (postRC.equals(200)) {
        println(post.getInputStream().getText());
    }
    println(i)
}
def end = System.currentTimeMillis()
println("Count: "+count)
println("Total time: " + (end-start).toDouble()/1000)
println("Average time: " + (end-start).toDouble()/(1000*count))
