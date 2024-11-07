package net.echo.sparky.event.impl.login;

import net.echo.sparky.event.Event;
import net.echo.sparky.player.SparkyPlayer;

public class LoginEvent implements Event {

    private final SparkyPlayer player;
    private LoginResult result;

    public LoginEvent(SparkyPlayer player, LoginResult result) {
        this.player = player;
        this.result = result;
    }

    public SparkyPlayer getPlayer() {
        return player;
    }

    public LoginResult getResult() {
        return result;
    }

    public void setResult(LoginResult result) {
        this.result = result;
    }

    public static class LoginResult {

        private LoginResultType type;
        private String reason;

        public LoginResult(LoginResultType type, String reason) {
            this.type = type;
            this.reason = reason;
        }

        public LoginResultType getType() {
            return type;
        }

        public void setType(LoginResultType type) {
            this.type = type;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public enum LoginResultType {
        ALLOWED, DENIED
    }
}
