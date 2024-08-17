package me.hellishbro.clickergamemod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private static final ArrayList<ScheduledTask> tasks = new ArrayList<>();

    public static void scheduleTask(ScheduledTask scheduledTask) {
        tasks.add(scheduledTask);
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (int i = 0; i < tasks.size(); i++) {
                ScheduledTask t = tasks.get(i);
                t.ticks--;
                if (t.ticks <= 0) {
                    t.task.run();
                    tasks.remove(i);
                    i--;
                }
            }
        });
    }
}
