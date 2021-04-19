package com.eomcs.util;

//
public class CommandRequest {
    private String commandPath;
    private String remoteAddr;
    private int remotePort;

    public CommandRequest(String commandPath, String remoteAddr, int remotePort) {
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
}