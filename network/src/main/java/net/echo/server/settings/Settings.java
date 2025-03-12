package net.echo.server.settings;

public class Settings {

    private int threads;

    public int threads() {
        return threads;
    }

    public Settings setThreads(int threads) {
        this.threads = threads;
        return this;
    }
}
