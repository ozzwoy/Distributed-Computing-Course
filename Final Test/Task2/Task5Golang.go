package main

import (
	"fmt"
	"strconv"
	"sync"
	"time"
)

type BusStop struct {
	// аналог семафора, буферизований канал
	channel chan struct{}
}

func (stop* BusStop) park() {
	stop.channel <- struct{}{}
}

func (stop* BusStop) leave()  {
	<- stop.channel
}

func newBusStop(maxNumOfBuses int) *BusStop {
	stop := &BusStop{make(chan struct{}, maxNumOfBuses)}
	return stop
}

func bus(stops []*BusStop, number int, wg* sync.WaitGroup) {
	for i := 0; i < len(stops); i++ {
		fmt.Println("Bus #" + strconv.Itoa(number) + " arrives at bus-stop #" + strconv.Itoa(i))
		stops[i].park()
		fmt.Println("Bus #" + strconv.Itoa(number) + " parks at bus-stop #" + strconv.Itoa(i))
		time.Sleep(time.Second)
		stops[i].leave()
		fmt.Println("Bus #" + strconv.Itoa(number) + " leaves from bus-stop #" + strconv.Itoa(i))
	}
	wg.Done()
}

func main() {
	numOfBuses := 5
	numOfStops := 5
	stops := make([]*BusStop, numOfStops)
	for i := 0; i < numOfStops; i++ {
		stops[i] = newBusStop(i + 1)
		fmt.Println("Stop #" + strconv.Itoa(i) + ": max " + strconv.Itoa(i + 1) + " buses")
	}

	wg := sync.WaitGroup{}
	wg.Add(numOfBuses)
	for i := 0; i < numOfBuses; i++ {
		go bus(stops, i, &wg)
	}
	wg.Wait()
}
