package C;

import C.threads.BusCorrectorThread;
import C.threads.CityCorrectorThread;
import C.threads.PriceChangerThread;
import C.threads.RouteFounderThread;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadsLauncher {

    private static RoutesMap initRoutesMap() {
        RoutesMap routesMap = new RoutesMap();

        routesMap.addCity("Kyiv");
        routesMap.addCity("Berlin");
        routesMap.addCity("Vienna");
        routesMap.addCity("Minsk");
        routesMap.addCity("Paris");
        routesMap.addCity("Murmansk");

        routesMap.addBus(0, 1, 200);
        routesMap.addBus(1, 2, 50);
        routesMap.addBus(1, 4, 100);
        routesMap.addBus(2, 3, 200);
        routesMap.addBus(2, 4, 150);
        routesMap.addBus(3, 5, 800);

        return routesMap;
    }

    public static void main(String[] args) {
        RoutesMap routesMap = initRoutesMap();
        System.out.println(routesMap.toString());
        ReadWriteLock lock = new ReentrantReadWriteLock();

        Thread busCorrectorThread = new BusCorrectorThread(5, routesMap, lock);
        Thread cityCorrectorThread = new CityCorrectorThread(5, routesMap, lock);
        Thread priceChangerThread = new PriceChangerThread(5, routesMap, lock);
        Thread routeFounderThread1 = new RouteFounderThread(3, routesMap, lock);
        Thread routeFounderThread2 = new RouteFounderThread(3, routesMap, lock);

        cityCorrectorThread.start();
        priceChangerThread.start();
        routeFounderThread1.start();
        routeFounderThread2.start();
        busCorrectorThread.start();
    }
}
