package C.threads;

import C.RoutesMap;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;

public class PriceChangerThread extends Thread {
    private final int times;
    private final RoutesMap routesMap;
    private final ReadWriteLock lock;

    public PriceChangerThread(int times, RoutesMap routesMap, ReadWriteLock lock) {
        this.times = times;
        this.routesMap = routesMap;
        this.lock = lock;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < times; i++) {
            lock.writeLock().lock();

            int from = random.nextInt(routesMap.getNumOfCities());
            List<Integer> adjacent = routesMap.getAdjacentCities(from);
            while (adjacent.isEmpty()) {
                from = random.nextInt(routesMap.getNumOfCities());
                adjacent = routesMap.getAdjacentCities(from);
            }
            int to = adjacent.get(random.nextInt(adjacent.size()));
            int price = random.nextInt(1000);

            routesMap.setPrice(from, to, price);
            System.out.println("Price changed for bus \"" + routesMap.getCity(from) + "-" + routesMap.getCity(to) +
                               "\": $" + price + ".");

            lock.writeLock().unlock();
        }
    }
}
