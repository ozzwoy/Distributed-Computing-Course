package main

import (
	"fmt"
	"strconv"
	"sync"
	"time"
)

func barber(queue chan int, hairdoDone chan bool) {
	fmt.Println("Barber opens barbershop.")
	for id := range queue {
		fmt.Println("\nBarber invites client " + strconv.Itoa(id) + ".")
		time.Sleep(time.Second)
		fmt.Println("Barber finishes hairdo and sees client #" + strconv.Itoa(id) + " off.")
		hairdoDone <- true
		time.Sleep(time.Millisecond * 100)
	}
	fmt.Println("Barber closes barbershop.")
}

func client(id int, queue chan int, hairdoDone chan bool, waitGroup* sync.WaitGroup)  {
	fmt.Println("Client #" + strconv.Itoa(id) + " takes place in queue to barbershop.")
	queue <- id
	time.Sleep(time.Millisecond * 100)
	fmt.Println("Client #" + strconv.Itoa(id) + " sits in chair and falls asleep.")
	<- hairdoDone
	fmt.Println("Client #" + strconv.Itoa(id) + " wakes up and leaves barbershop.")
	waitGroup.Done()
}

func main() {
	numOfClients := 6
	var waitGroup sync.WaitGroup
	waitGroup.Add(numOfClients)
	queue := make(chan int)
	hairdoDone := make(chan bool)

	go barber(queue, hairdoDone)

	for i := 0; i < numOfClients / 2; i++ {
		go client(i, queue, hairdoDone, &waitGroup)
	}

	time.Sleep(time.Second * 3)

	for i := numOfClients / 2; i < numOfClients; i++ {
		go client(i, queue, hairdoDone, &waitGroup)
	}

	waitGroup.Wait()
	close(queue)
	time.Sleep(time.Millisecond * 100)
}
