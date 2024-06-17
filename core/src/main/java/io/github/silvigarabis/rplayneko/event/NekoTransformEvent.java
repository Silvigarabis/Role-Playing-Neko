package io.github.silvigarabis.rplayneko.event;

import io.github.silvigarabis.rplayneko.data.*;

/**
 * 在玩家从猫娘状态离开或者被转换成猫娘时触发。
 */
public class NekoTransformEvent<Player> extends Event<Player> {

    private final boolean isNekoBefore;
    public boolean isNekoBefore(){
        return isNekoBefore;
    }
    public NekoTransformEvent(RPlayNekoPlayer<Player> player, boolean isNekoBefore){
        super(player);
        this.isNekoBefore = isNekoBefore;
    }

    @Override
    public boolean isCancelable(){
        return true;
    }
}
