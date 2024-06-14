package org.cneko.toneko.bukkit.util;

import com.crystalneko.toneko.ToNeko;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.cneko.toneko.common.util.scheduled.ISchedulerPool;
import org.cneko.toneko.common.util.scheduled.ScheduledTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class FoliaSchedulerPoolImpl implements ISchedulerPool {
    @Override
    public ScheduledTask scheduleAsync(@NotNull Runnable task, long delayedTicks) {
        final ScheduledTask wrapped = new ScheduledTask(task,this);
        wrapped.setTaskMarker(Bukkit.getAsyncScheduler().runDelayed(ToNeko.getInstance(), ignored -> wrapped.run(),delayedTicks * 50, TimeUnit.MILLISECONDS));
        return wrapped;
    }

    @Override
    public ScheduledTask executeAsync(@NotNull Runnable task) {
        final ScheduledTask wrapped = new ScheduledTask(task,this);
        wrapped.setTaskMarker(Bukkit.getAsyncScheduler().runNow(ToNeko.getInstance(),ignored -> wrapped.run()));
        return wrapped;
    }

    @Override
    public ScheduledTask scheduleSync(@NotNull Runnable task, long delayedTicks, int chunkX, int chunkZ,Object level) {
        final ScheduledTask wrapped = new ScheduledTask(task,this);

        if (level == null){
            wrapped.setTaskMarker(Bukkit.getGlobalRegionScheduler().runDelayed(ToNeko.getInstance(),ignored -> wrapped.run(),delayedTicks));
            return wrapped;
        }

        wrapped.setTaskMarker(Bukkit.getRegionScheduler().runDelayed(ToNeko.getInstance(), ((World) level),chunkX,chunkZ,ignored -> wrapped.run(),delayedTicks));
        return wrapped;
    }

    @Override
    public void cancelTask(@NotNull Object taskMaker) {
        final io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask = ((io.papermc.paper.threadedregions.scheduler.ScheduledTask) taskMaker);
        scheduledTask.cancel();
    }
}