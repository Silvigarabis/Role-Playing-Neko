package io.github.silvigarabis.rplayneko.command;

import io.github.silvigarabis.rplayneko.RPlayNekoCore;

import java.util.List;

public class CommandRPlayNeko<Sender, Player> extends Command<Sender, Player> {
    public CommandRPlayNeko(RPlayNekoCore<Sender, Player> core){
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
