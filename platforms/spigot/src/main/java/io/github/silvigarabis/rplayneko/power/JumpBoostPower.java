package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpBoostPower extends RPlayNekoPower<Player> {
    public JumpBoostPower(RPlayNekoPowerType type, RPlayNekoPlayer<Player> player){
        super(type, player);
    }
    private static final PotionEffect StatusEffect = new PotionEffect(PotionEffectType.NIGHT_VISION,
            12 * 20, 2, false, false, false);
    @Override
    public void tick(){
        StatusEffect.apply(this.getPlayer().getOrigin());
    }
}
