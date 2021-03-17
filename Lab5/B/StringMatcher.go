package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"sync"
)

type CyclicBarrier struct {
	sync.Mutex
	parties int
	count   int
	signal  chan struct{}
}

func (b *CyclicBarrier) reset() {
	b.Lock()
	b.count = b.parties
	close(b.signal)
	b.signal = make(chan struct{})
	b.Unlock()
}

func (b *CyclicBarrier) await() {
	b.Lock()
	b.count--
	count := b.count
	signal := b.signal
	b.Unlock()

	if count > 0 {
		<-signal
	} else {
		b.reset()
	}
}

func newCyclicBarrier(numberOfParties int) *CyclicBarrier {
	b := &CyclicBarrier{
		parties: numberOfParties,
		count:   numberOfParties,
		signal:  make(chan struct{}),
	}

	return b
}

func checkMatch(counts []int) bool {
	if (counts[0] == counts[1] && counts[1] == counts[2]) ||
		(counts[0] == counts[1]  && counts[1] == counts[3]) ||
		(counts[0] == counts[2]  && counts[2] == counts[3]) ||
		(counts[1] == counts[2]  && counts[2] == counts[3]) {
		return true
	}
	return false
}

func max(nums []int) (int, bool) {
	var max int
	var multiple bool

	for i, v := range nums {
		if i == 0 || v > max {
			max = v
			multiple = false
		} else if v == max {
			multiple = true
		}
	}

	return max, multiple
}

func shouldAddAB(counts []int, index int) int {
	max, multiple := max(counts)

	if counts[index] == max {
		if multiple {
			return 0
		}
		return -1
	}

	return 1
}

func matchString(strings []string, counts []int, index int, barrier *CyclicBarrier, wg* sync.WaitGroup) {
	mutableStr := []byte(strings[index])

	for !checkMatch(counts) {
		fmt.Println()

		barrier.await()

		switch shouldAddAB(counts, index) {
		case -1:
			for i := 0; i < len(mutableStr); i++ {
				if mutableStr[i] == 'A' {
					mutableStr[i] = 'C'
					break
				} else if mutableStr[i] == 'B' {
					mutableStr[i] = 'D'
					break
				}
			}
			counts[index]--
			break
		case 1:
			for i := 0; i < len(mutableStr); i++ {
				if mutableStr[i] == 'C' {
					mutableStr[i] = 'A'
					break
				} else if mutableStr[i] == 'D' {
					mutableStr[i] = 'B'
					break
				}
			}
			counts[index]++
		}

		strings[index] = string(mutableStr)
		status := strconv.Itoa(index) + ": " + strings[index] + " (" + strconv.Itoa(counts[index]) + ")"
		fmt.Println(status)

		barrier.await()
	}

	wg.Done()
}

func generateRandomString(size int) string {
	mutableStr := make([]byte, size)
	percent := rand.Int() % 101
	var p int
	var letter int

	for i := 0; i < size; i++ {
		p = rand.Int() % 101
		letter = rand.Int() % 2

		if p < percent {
			if letter == 0 {
				mutableStr[i] = 'A'
			} else {
				mutableStr[i] = 'B'
			}
		} else {
			if letter == 0 {
				mutableStr[i] = 'C'
			} else {
				mutableStr[i] = 'D'
			}
		}
	}

	return string(mutableStr)
}

func countAB(str string) int {
	count := 0

	for i := 0; i < len(str); i++ {
		if str[i] == 'A' || str[i] == 'B' {
			count++
		}
	}

	return count
}

func main() {
	size := 50
	barrier := newCyclicBarrier(4)
	strings := make([]string, 4)
	counts := make([]int, 4)

	for i := 0; i < 4; i++ {
		strings[i] = generateRandomString(size)
		counts[i] = countAB(strings[i])
		status := strconv.Itoa(i) + ": " + strings[i] + " (" + strconv.Itoa(counts[i]) + ")"
		fmt.Println(status)
	}

	wg := sync.WaitGroup{}
	wg.Add(4)

	for i := 0; i < 4; i++ {
		go matchString(strings, counts, i, barrier, &wg)
	}

	wg.Wait()
}
