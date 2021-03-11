package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"
)

type Garden struct {
	plants [][]bool
}

func check(err error) {
	if err != nil {
		panic(err)
	}
}

func createGarden(size int) Garden {
	garden := Garden{make([][]bool, size)}
	for i := range garden.plants {
		garden.plants[i] = make([]bool, size)
	}
	return garden
}

func (garden Garden) toString() string {
	result := ""
	n := len(garden.plants)
	for i := 0; i < n; i++ {
		for j := 0; j < n; j++ {
			if garden.plants[i][j] {
				result += "1 "
			} else {
				result += "0 "
			}
		}
		result += "\n"
	}
	result += "\n"
	return result
}

func nature(garden Garden, mutex* sync.RWMutex, end* bool) {
	for !*end {
		mutex.Lock()
		n := (rand.Int() % (len(garden.plants) * len(garden.plants))) + 1
		for j := 0; j < n; j++ {
			garden.plants[rand.Int() % len(garden.plants)][rand.Int() % len(garden.plants)] = true
		}
		mutex.Unlock()
		time.Sleep(time.Millisecond * 300)
	}
}

func gardener(garden Garden, mutex* sync.RWMutex, end* bool) {
	for !*end {
		mutex.Lock()
		for j := 0; j < len(garden.plants); j++ {
			for k := 0; k < len(garden.plants); k++ {
				garden.plants[j][k] = false
			}
		}
		mutex.Unlock()
		time.Sleep(time.Millisecond * 500)
	}
}

func printStatusToConsole(garden Garden, mutex* sync.RWMutex, end* bool) {
	for !*end {
		mutex.RLock()
		fmt.Println(garden.toString())
		mutex.RUnlock()
		time.Sleep(time.Millisecond * 100)
	}
}

func printStatusToFile(file* os.File, garden Garden, mutex* sync.RWMutex, end* bool) {
	for !*end {
		mutex.RLock()
		_, err := file.WriteString(garden.toString())
		check(err)
		mutex.RUnlock()
		time.Sleep(time.Millisecond * 100)
	}
}

func main() {
	garden := createGarden(20)
	mutex := sync.RWMutex{}
	end := false
	file, err := os.Create("B//log.txt")
	check(err)

	go printStatusToConsole(garden, &mutex, &end)
	go printStatusToFile(file, garden, &mutex, &end)
	go nature(garden, &mutex, &end)
	go gardener(garden, &mutex, &end)

	time.Sleep(time.Second * 3)
	end = true
}
