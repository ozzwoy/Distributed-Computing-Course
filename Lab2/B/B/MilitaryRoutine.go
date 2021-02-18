package main

import (
	"fmt"
	"strconv"
)

func ivanov(goods []int, count int, ipChannel chan int) {
	for i := 0; i < count; i++ {
		fmt.Println("Ivanov passed to petrov " + strconv.Itoa(goods[i]) + " items")
		ipChannel <- goods[i]
	}
	close(ipChannel)
}

func petrov(ipChannel chan int, pnChannel chan int) {
	for i := range ipChannel {
		fmt.Println("Petrov loads truck with " + strconv.Itoa(i) + " items")
		pnChannel <- i
	}
	close(pnChannel)
}

func nechyporchuk(price int, pnChannel chan int, endChannel chan bool) {
	total := 0
	for i := range pnChannel {
		total += i * price
		fmt.Println("Nechyporchuk counts " + strconv.Itoa(i) + " items, total price: " + strconv.Itoa(total))
	}
	endChannel <- true
}

func getGoods(count int) []int {
	result := make([]int, count)
	for i := 0; i < count; i++ {
		result[i] = i + 1
	}
	return result
}

func main() {
	count := 20
	price := 3
	goods := getGoods(count)
	ipChannel := make(chan int)
	pnChannel := make(chan int)
	endChannel := make(chan bool)

	go ivanov(goods, count, ipChannel)
	go petrov(ipChannel, pnChannel)
	go nechyporchuk(price, pnChannel, endChannel)

	<-endChannel
	fmt.Println("По коням!")
}
