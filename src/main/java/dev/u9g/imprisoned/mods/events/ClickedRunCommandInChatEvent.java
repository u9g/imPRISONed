package dev.u9g.imprisoned.mods.events;


public class ClickedRunCommandInChatEvent {
    private final String commandToRun;
    private boolean sendToServer = true;

    public ClickedRunCommandInChatEvent(String commandToRun) {
        this.commandToRun = commandToRun;
    }

    public String getCommandToRun() {
        return commandToRun;
    }

    public void dontSendToServer() {
        sendToServer = false;
    }

    public boolean shouldSendToServer() {
        return sendToServer;
    }
}
