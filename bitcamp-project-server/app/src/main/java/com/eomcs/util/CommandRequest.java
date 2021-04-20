package com.eomcs.util;

//
public class CommandRequest {
    private String commandPath;
    private String remoteAddr;
    private int remotePort;
    private Prompt prompt;

    public CommandRequest(String commandPath, String remoteAddr, int remotePort, Prompt prompt) {
        this.prompt = prompt;
        this.remotePort = remotePort;
        this.commandPath = commandPath;
        this.remoteAddr = remoteAddr;
    }

    public String getCommandPath() {
        return commandPath;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public Prompt getPrompt() {
        return prompt;
    }
}