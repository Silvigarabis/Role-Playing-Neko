package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionPower extends RPlayNekoPower<Player> {
    public NightVisionPower(RPlayNekoPowerType type, RPlayNekoPlayer<Player> player){
        super(type, player);
    }
    private static final PotionEffect StatusEffect = new PotionEffect(PotionEffectType.NIGHT_VISION,
            21 * 20, 0, false, false, false);
    @Override
    public void tick(){
        StatusEffect.apply(this.getPlayer().getOrigin());
    }
}
