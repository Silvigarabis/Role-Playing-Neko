package io.github.silvigarabis.rplayneko.command;

import io.github.silvigarabis.rplayneko.RPlayNekoCore;

import java.util.List;

public class CommandRPlayNeko<Instance, Sender, Player> extends Command<Instance, Sender, Player> {
    public CommandRPlayNeko(RPlayNekoCore<Instance, Sender, Player> core){
        super(core);
    }
    @Override
    public String getLabel(){
        return "rplayneko";
    }
    @Override
    public boolean executeCommand(Sender sender, String label, List<String> args){
        return true;
    }
}
