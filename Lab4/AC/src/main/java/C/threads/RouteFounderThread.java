package C.threads;

import C.RoutesMap;

import java.util.AbstractMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;

public class RouteFounderThread extends Thread {
    private final int times;
    private final RoutesMap routesMap;
    private final ReadWriteLock lock;

    public RouteFounderThread(int times, RoutesMap routesMap, ReadWriteLock lock) {
        this.times = times;
        this.routesMap = routesMap;
        this.lock = lock;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < times; i++) {
            lock.readLock().lock();

            StringBuilder result = new StringBuilder();
            result
                    .append("\n")
                    .append(routesMap.toString())
                    .append("\n");

            int from = random.nextInt(routesMap.getNumOfCities());
            int to = random.nextInt(routesMap.getNumOfCities());
            while (to == from) {
                to = random.nextInt(routesMap.getNumOfCities());
            }

            result
                    .append(Thread.currentThread().getId())
                    .append("    Route from ")
                    .append(routesMap.getCity(from))
                    .append(" to ")
                    .append(routesMap.getCity(to))
                    .append(": ");

            AbstractMap.SimpleEntry<List<String>, Integer> entry = routesMap.findRoute(from, to);
            List<String> path = entry.getKey();
            if (path.isEmpty()) {
                result.append("no route.\n");
            } else {
                for (int j = 0; j < path.size() - 1; j++) {
                    result
                            .append(path.get(j))
                            .append(" -> ");
                }
                result
                        .append(path.get(path.size() - 1))
                        .append("; total price: $")
                        .append(entry.getValue())
                        .append(".\n");
            }
            System.out.println(result);

            lock.readLock().unlock();
        }
    }
}
