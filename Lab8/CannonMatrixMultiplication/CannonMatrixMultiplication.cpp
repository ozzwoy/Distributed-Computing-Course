#include <math.h>
#include <mpi.h>
#include <stdio.h>
#include <time.h>

const int DIMENSION_OF_GRID = 2;
const int MATRIX_SIZE = 1000;

int num_of_processors;
int processor_rank;
int grid_size;
int process_coordinates[2];
int block_size;

MPI_Comm grid_communicator;
MPI_Comm column_communicator;
MPI_Comm row_communicator;


void initMatrix(double* matrix, int size) {
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			matrix[i * size + j] = (double)i * size + (double)j;
		}
	}
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

void createGridCommunicators() {
	int grid_dimensions[] = { grid_size, grid_size };
	int is_periodic[] = { 0, 0 };
	int subdimensions[2];
	int allow_reorder = 1;

	MPI_Cart_create(MPI_COMM_WORLD, DIMENSION_OF_GRID, grid_dimensions, is_periodic, allow_reorder, &grid_communicator);
	MPI_Cart_coords(grid_communicator, processor_rank, DIMENSION_OF_GRID, process_coordinates);

	// creating communicators for rows
	subdimensions[0] = 0;
	subdimensions[1] = 1;
	MPI_Cart_sub(grid_communicator, subdimensions, &row_communicator);

	// creating communicators for columns
	subdimensions[0] = 1;
	subdimensions[1] = 0;
	MPI_Cart_sub(grid_communicator, subdimensions, &column_communicator);
}

// function for distributing matrix between blocks
void initBlock(double* matrix, double* block) {
	double* strip = new double[block_size * MATRIX_SIZE];

	if (process_coordinates[1] == 0) {
		MPI_Scatter(matrix, block_size * MATRIX_SIZE, MPI_DOUBLE,
			strip, block_size * MATRIX_SIZE, MPI_DOUBLE,
			0, column_communicator);
	}

	for (int i = 0; i < block_size; i++) {
		MPI_Scatter(&strip[i * MATRIX_SIZE], block_size, MPI_DOUBLE,
			&(block[i * block_size]), block_size, MPI_DOUBLE,
			0, row_communicator);
	}

	delete[] strip;
}

// cyclic left shift of matrix A blocks in the process grid rows
void shiftBlockA(double* block, int delta) {
	MPI_Status status;
	int next = (process_coordinates[1] < delta) ? (grid_size - delta + process_coordinates[1]) : (process_coordinates[1] - delta);
	int prev = (process_coordinates[1] + delta) % grid_size;

	MPI_Sendrecv_replace(block, block_size * block_size, MPI_DOUBLE, next, 0, prev, 0, row_communicator, &status);
}

// cyclic upward shift of matrix B blocks in the process grid columns
void shiftBlockB(double* block_b, int delta) {
	MPI_Status status;
	int next = (process_coordinates[0] < delta) ? (grid_size - delta + process_coordinates[0]) : (process_coordinates[0] - delta);
	int prev = (process_coordinates[0] + delta) % grid_size;

	MPI_Sendrecv_replace(block_b, block_size * block_size, MPI_DOUBLE, next, 0, prev, 0, column_communicator, &status);
}

void collectResult(double* matrix_c, double* block_c) {
	double* strip = new double[MATRIX_SIZE * block_size];

	for (int i = 0; i < block_size; i++) {
		MPI_Gather(&block_c[i * block_size], block_size, MPI_DOUBLE,
			&strip[i * MATRIX_SIZE], block_size, MPI_DOUBLE,
			0, row_communicator);
	}

	if (process_coordinates[1] == 0) {
		MPI_Gather(strip, block_size * MATRIX_SIZE, MPI_DOUBLE,
			matrix_c, block_size * MATRIX_SIZE, MPI_DOUBLE,
			0, column_communicator);
	}

	delete[] strip;
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


int main(int argc, char* argv[]) {
	double* matrix_a = nullptr;
	double* matrix_b = nullptr;
	double* matrix_c = nullptr;

	double* block_a;
	double* block_b;
	double* block_c;

	int start, finish, duration;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &num_of_processors);
	MPI_Comm_rank(MPI_COMM_WORLD, &processor_rank);

	grid_size = floor(sqrt((double)num_of_processors));
	if (num_of_processors != grid_size * grid_size) {
		if (processor_rank == 0) {
			printf_s("Number of processes must be a square\n");
		}
	}
	else {
		if (processor_rank == 0) {
			printf_s("Cannon's matrix multiplication\n");

			matrix_a = new double[MATRIX_SIZE * MATRIX_SIZE];
			matrix_b = new double[MATRIX_SIZE * MATRIX_SIZE];
			matrix_c = new double[MATRIX_SIZE * MATRIX_SIZE];

			initMatrix(matrix_a, MATRIX_SIZE);
			initMatrix(matrix_b, MATRIX_SIZE);
		}

		MPI_Barrier(MPI_COMM_WORLD);

		block_size = MATRIX_SIZE / grid_size;

		block_a = new double[block_size * block_size];
		block_b = new double[block_size * block_size];
		block_c = new double[block_size * block_size];

		for (int i = 0; i < block_size * block_size; i++) {
			block_c[i] = 0;
		}

		start = clock();

		createGridCommunicators();
		initBlock(matrix_a, block_a);
		initBlock(matrix_b, block_b);

		shiftBlockA(block_a, process_coordinates[0]);
		shiftBlockB(block_b, process_coordinates[1]);

		for (int i = 0; i < grid_size; i++) {
			simpleMultiplication(block_a, block_b, block_c, block_size);
			shiftBlockA(block_a, 1);
			shiftBlockB(block_b, 1);
		}

		collectResult(matrix_c, block_c);

		finish = clock();
		duration = finish - start;

		delete[] block_a;
		delete[] block_b;
		delete[] block_c;

		if (processor_rank == 0) {
			printf_s("Calculations finished.\nTime: %i ms\n", duration);
			if (test(matrix_a, matrix_b, matrix_c)) {
				printf_s("Result is correct.\n");
			}
			else {
				printf_s("Result is wrong.\n");
			}

			delete[] matrix_a;
			delete[] matrix_b;
			delete[] matrix_c;
		}
	}

	MPI_Finalize();
	return 0;
}