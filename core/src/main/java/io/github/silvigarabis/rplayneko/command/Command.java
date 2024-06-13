package io.github.silvigarabis.rplayneko.command;

import io.github.silvigarabis.rplayneko.RPlayNekoCore;

import java.util.List;

public abstract class Command<Sender, Player> {
    protected RPlayNekoCore<Sender, Player> core;
    public Command(RPlayNekoCore<Sender, Player> core){
        this.core = core;
    }
    public abstract String getLabel();
    public String[] getAliases(){
        return new String[]{ this.getLabel() };
    }
    public abstract boolean executeCommand(Sender sender, String label, List<String> args);
}
