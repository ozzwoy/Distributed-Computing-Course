#include <math.h>
#include <stdio.h>
#include <time.h>
#include "mpi.h"
#include <iostream>

int num_of_processors, processor_rank;
const int MATRIX_SIZE = 4;

void initMatrix(double* matrix, int size) {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            matrix[i * size + j] = (double)i * size + (double)j;
        }
    }
}

void transpose(double* matrix) {
    double* buffer = new double[MATRIX_SIZE * MATRIX_SIZE];
    
    for (int i = 0; i < MATRIX_SIZE; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            buffer[i * MATRIX_SIZE + j] = matrix[i * MATRIX_SIZE + j];
        }
    }
    for (int i = 0; i < MATRIX_SIZE; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            matrix[i * MATRIX_SIZE + j] = buffer[j * MATRIX_SIZE + i];
        }
    }

    delete[] buffer;
}

void simpleMultiplication(double* matrix_a, double* matrix_b, double* matrix_c, int size) {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                matrix_c[i * size + j] += matrix_a[i * size + k] * matrix_b[k * size + j];
            }
        }
    }
}

bool test(double* matrix_a, double* matrix_b, double* result) {
    double* expected;
    double error = 1.e-6;

    if (processor_rank == 0) {
        expected = new double[MATRIX_SIZE * MATRIX_SIZE];

        for (int i = 0; i < MATRIX_SIZE * MATRIX_SIZE; i++) {
            expected[i] = 0;
        }
        simpleMultiplication(matrix_a, matrix_b, expected, MATRIX_SIZE);

        for (int i = 0; i < MATRIX_SIZE * MATRIX_SIZE; i++) {
            if (fabs(expected[i] - result[i]) >= error) {
                return false;
            }
        }
    }

    return true;
}

int main(int* args, char** argv) {
    MPI_Status status;

    double* matrix_a = nullptr;
    double* matrix_b = nullptr;
    double* matrix_c = nullptr;

    int start, finish, duration;

	MPI_Init(args, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &processor_rank);
	MPI_Comm_size(MPI_COMM_WORLD, &num_of_processors);

    if (processor_rank == 0) {
        printf_s("Strip matrix multiplication\n");

        matrix_a = new double[MATRIX_SIZE * MATRIX_SIZE];
        matrix_b = new double[MATRIX_SIZE * MATRIX_SIZE];
        matrix_c = new double[MATRIX_SIZE * MATRIX_SIZE];

        initMatrix(matrix_a, MATRIX_SIZE);
        initMatrix(matrix_b, MATRIX_SIZE);
        transpose(matrix_b);
    }

    int strip_width = MATRIX_SIZE / num_of_processors;
    int strip_length = strip_width * MATRIX_SIZE;

    double* strip_a = new double[strip_length];
    double* strip_b = new double[strip_length];
    double* strip_c = new double[strip_length];

    start = clock();

    MPI_Scatter(matrix_a, strip_length, MPI_DOUBLE, 
                strip_a, strip_length, MPI_DOUBLE, 
                0, MPI_COMM_WORLD);

    MPI_Scatter(matrix_b, strip_length, MPI_DOUBLE,
                strip_b, strip_length, MPI_DOUBLE,
                0, MPI_COMM_WORLD);

    int next = (processor_rank + 1) % num_of_processors;
    int prev = processor_rank != 0 ? processor_rank - 1 : num_of_processors - 1;

    for (int p = 0; p < num_of_processors; p++) {
        for (int i = 0; i < strip_width; i++) {
            for (int j = 0; j < strip_width; j++) {
                double temp = 0;

                for (int k = 0; k < MATRIX_SIZE; k++) {
                    temp += strip_a[i * MATRIX_SIZE + k] * strip_b[j * MATRIX_SIZE + k];
                }

                int current_chunk = (processor_rank < p) ? (num_of_processors - p + processor_rank) : processor_rank - p;
                strip_c[i * MATRIX_SIZE + current_chunk * strip_width + j] = temp;
            }
        }

        MPI_Sendrecv_replace(strip_b, strip_length, MPI_DOUBLE, next, 0, prev, 0, MPI_COMM_WORLD, &status);
    }

    MPI_Gather(strip_c, strip_length, MPI_DOUBLE, matrix_c, strip_length, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    finish = clock();
    duration = finish - start;

    if (processor_rank == 0) {
        printf_s("Calculations finished.\nTime: %i ms\n", duration);
        /*if (test(matrix_a, matrix_b, matrix_c)) {
            printf_s("Result is correct.\n");
        }
        else {
            printf_s("Result is wrong.\n");
        }*/

        delete[] matrix_a;
        delete[] matrix_b;
        delete[] matrix_c;
    }

    delete[]strip_a;
    delete[]strip_b;
    delete[]strip_c;

    MPI_Finalize();
	return 0;
}