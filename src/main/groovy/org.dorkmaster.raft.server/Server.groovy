package org.dorkmaster.raft.server

import io.grpc.ServerBuilder

import java.util.concurrent.TimeUnit

class Server {
    io.grpc.Server server
    int port = 10001;

    static void main(String[] args) {
        new Server().start(args)
    }

    def start(String[] args) {
        // .addService(servicenamehere)
        server = ServerBuilder.forPort(port)
            .build()
            .start()

        // add health check ideally

        Runtime.getRuntime().addShutdownHook {
            Thread.start {
                server.shutdown()
                try {
                    if (!server.awaitTermination(30, TimeUnit.SECONDS)) {
                        server.shutdownNow()
                        server.awaitTermination(5, TimeUnit.SECONDS)
                    }
                }
                catch(InterruptedException e) {
                    server.shutdownNow()
                }
            }
        }

        server.awaitTermination()
    }
}