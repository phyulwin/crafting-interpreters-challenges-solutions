// hash_benchmark.c
// Compile: gcc hash_benchmark.c -O2 -o bench

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define N 100000

// Simple linear probing table
typedef struct {
    int key;
    int used;
} Entry;

Entry table[N * 2];

// Hash function
int hash(int k) { return (k * 2654435761u) % (N * 2); }

// Insert
void insert(int k) {
    int i = hash(k);
    while (table[i].used && table[i].key != k)
        i = (i + 1) % (N * 2);
    table[i].key = k;
    table[i].used = 1;
}

// Lookup
int lookup(int k) {
    int i = hash(k);
    while (table[i].used) {
        if (table[i].key == k) return 1;
        i = (i + 1) % (N * 2);
    }
    return 0;
}

int main() {
    clock_t start, end;

    // Sequential keys
    start = clock();
    for (int i = 0; i < N; i++) insert(i);
    for (int i = 0; i < N; i++) lookup(i);
    end = clock();
    printf("Sequential: %f\n", (double)(end - start)/CLOCKS_PER_SEC);

    // Random keys
    start = clock();
    for (int i = 0; i < N; i++) insert(rand());
    for (int i = 0; i < N; i++) lookup(rand());
    end = clock();
    printf("Random: %f\n", (double)(end - start)/CLOCKS_PER_SEC);

    return 0;
}

// Variation summary

// Sequential: fast, minimal collisions
// Random: moderate collisions
// High-load / clustered: worst-case probing

// Test case rationale

// Covers distribution sensitivity, growth behavior, and collision impact.
