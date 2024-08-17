package me.hellishbro.clickergamemod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimedScheduler {
    public static class ScheduledTask {
        int ticks;
        Runnable task;

        public ScheduledTask(int ticks, Runnable task) {
            this.ticks = ticks;
            this.task = task;
        }
    }

    private static final CopyOnWriteArrayList<ScheduledTask> tasks = new CopyOnWriteArrayList<>();

    public static void scheduleTask(ScheduledTask scheduledTask) {
        tasks.add(scheduledTask);
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Iterator<ScheduledTask> it = tasks.iterator();
            while (it.hasNext()) {
                ScheduledTask t = it.next();
                t.ticks--;
                if (t.ticks <= 0) {
                    t.task.run();
                    it.remove();
                }
            }
        });
    }
}
