package net.echo.server.bootstrap;

public class Settings {

    private int acceptThreads;
    private int threads;

    public int receiveThreads() {
        return threads;
    }

    public Settings setThreads(int threads) {
        this.threads = threads;
        return this;
    }
}
