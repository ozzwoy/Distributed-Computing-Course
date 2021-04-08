#include <stdio.h>
#include <time.h>

void initMatrix(double* matrix, int size, bool zeros = false) {
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			matrix[i * size + j] = zeros ? 0 : (double)i * size + (double)j;
		}
	}
}

int main(int* args, char** argv) {
	const int MATRIX_SIZE = 1000;

	double* matrix_a = new double[MATRIX_SIZE * MATRIX_SIZE];
	double* matrix_b = new double[MATRIX_SIZE * MATRIX_SIZE];
	double* matrix_c = new double[MATRIX_SIZE * MATRIX_SIZE];

	int start, finish, duration;

	initMatrix(matrix_a, MATRIX_SIZE);
	initMatrix(matrix_b, MATRIX_SIZE);
	initMatrix(matrix_c, MATRIX_SIZE, true);

	printf_s("Simple matrix multiplication\n");

	start = clock();

	for (int i = 0; i < MATRIX_SIZE; i++) {
		for (int j = 0; j < MATRIX_SIZE; j++) {
			for (int k = 0; k < MATRIX_SIZE; k++) {
				matrix_c[i * MATRIX_SIZE + j] += matrix_a[i * MATRIX_SIZE + k] * matrix_b[k * MATRIX_SIZE + j];
			}
		}
	}

	finish = clock();
	duration = finish - start;
	printf_s("Calculations finished.\nTime: %i ms\n", duration);

	return 0;
}