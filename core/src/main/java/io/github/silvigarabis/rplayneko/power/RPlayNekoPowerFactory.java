package io.github.silvigarabis.rplayneko.power;

import java.util.*;
import io.github.silvigarabis.rplayneko.data.*;

@FunctionalInterface
public interface RPlayNekoPowerFactory<Player> {
    RPlayNekoPower<Player> newPowerInstance(RPlayNekoPlayer<Player> player);
}
