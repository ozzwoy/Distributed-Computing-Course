package C.threads;

import C.RoutesMap;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;

public class BusCorrectorThread extends Thread {
    private final int times;
    private final RoutesMap routesMap;
    private final ReadWriteLock lock;

    public BusCorrectorThread(int times, RoutesMap routesMap, ReadWriteLock lock) {
        this.times = times;
        this.routesMap = routesMap;
        this.lock = lock;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < times; i++) {
            lock.writeLock().lock();

            int mark = random.nextInt(11);
            int from = random.nextInt(routesMap.getNumOfCities());
            List<Integer> adjacent = routesMap.getAdjacentCities(from);

            if (mark < 7 && !routesMap.isFullyCovered()) {
                while (adjacent.size() == routesMap.getNumOfCities() - 1) {
                    from = random.nextInt(routesMap.getNumOfCities());
                    adjacent = routesMap.getAdjacentCities(from);
                }

                int to = 0;
                boolean[] available = new boolean[routesMap.getNumOfCities()];
                for (int j : adjacent) {
                    available[j] = true;
                }
                for (int j = 0; j < available.length; j++) {
                    if (!available[j] && j != from) {
                        to = j;
                    }
                }
                int price = random.nextInt(1000);

                routesMap.addBus(from, to, price);
                System.out.println("Bus \"" + routesMap.getCity(from) + "-" + routesMap.getCity(to) +
                                   "\" was added: $" + price + ".");
            } else if (!routesMap.isEmpty()) {
                while (adjacent.isEmpty()) {
                    from = random.nextInt(routesMap.getNumOfCities());
                    adjacent = routesMap.getAdjacentCities(from);
                }

                int to = adjacent.get(random.nextInt(adjacent.size()));
                routesMap.removeBus(from, to);
                System.out.println("Bus \"" + routesMap.getCity(from) + "-" + routesMap.getCity(to) +
                                   "\" was removed.");
            }

            lock.writeLock().unlock();
        }
    }
}
