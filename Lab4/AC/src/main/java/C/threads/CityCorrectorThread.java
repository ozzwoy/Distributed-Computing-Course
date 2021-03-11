package C.threads;

import C.RoutesMap;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;

public class CityCorrectorThread extends Thread {
    private final int times;
    private final RoutesMap routesMap;
    private final ReadWriteLock lock;
    private final String[] cities;

    public CityCorrectorThread(int times, RoutesMap routesMap, ReadWriteLock lock) {
        this.times = times;
        this.routesMap = routesMap;
        this.lock = lock;
        this.cities = new String[] {"Brussels", "Rostock", "Lisbon", "Palermo", "Odessa", "Hong Kong"};
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0, j = 0; i < times; i++) {
            lock.writeLock().lock();

            boolean mark = random.nextBoolean();
            if (mark && j < cities.length) {
                System.out.println("City of " + cities[j] + " was added.");
                routesMap.addCity(cities[j]);
                j++;
            } else {
                int city = random.nextInt(routesMap.getNumOfCities());
                System.out.println("City of " + routesMap.getCity(city) + " was removed.");
                routesMap.removeCity(city);
            }

            lock.writeLock().unlock();
        }
    }
}
