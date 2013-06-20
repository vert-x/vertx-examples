package deploymod

println "Deploying module"

conf = ["some-var" : "hello"]

container.deployModule("io.vertx~my-mod~1.0", conf, 1) { asyncResult ->
  if (asyncResult.succeeded()) {
    println "This gets called when deployment is complete, deployment id is $asyncResult.result()"
  } else {
    println "Failed to deploy ${asyncResult.cause()}"
  }
}