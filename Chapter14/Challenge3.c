/*
Allocators store metadata before memory blocks.
They track size and allocation status.
Free blocks use lists or bins.
Allocation finds and splits blocks.
Free merges adjacent blocks.
This reduces fragmentation over time.
Custom allocators simplify control.
They reduce flexibility and efficiency.
*/

// The following code is for assignment submission only.
// It is not standalone or execution ready.

// memory.c - hardcore custom allocator
/* 
INDUSTRY MODEL (dlmalloc/jemalloc style)

Each allocation has a hidden header:
[ metadata | user memory ]

metadata includes:
- size of block
- allocation status
- links (for free lists)

Free blocks stored in segregated lists (bins) by size.
Allocation = find best-fit block → split if needed.
Free = mark block free → coalesce neighbors.
Efficiency = O(1)/O(log n) lookup via bins.
Fragmentation reduced via coalescing + size classes.
*/

#include <stdlib.h>
#include <string.h>

#define HEAP_SIZE (1024 * 1024)

// Global heap region
static uint8_t* heap = NULL;
static size_t used = 0;

// Initialize once at VM startup
void initHeap() {
  heap = (uint8_t*)malloc(HEAP_SIZE);
  if (heap == NULL) exit(1);
}

// Custom reallocate (bump allocator model)
void* reallocate(void* pointer, size_t oldSize, size_t newSize) {

  // Free case (no-op for bump allocator)
  if (newSize == 0) {
    return NULL;
  }

  // Fresh allocation
  if (pointer == NULL) {
    if (used + newSize > HEAP_SIZE) exit(1);

    void* result = heap + used;
    used += newSize;
    return result;
  }

  // Resize: allocate new block + copy data
  void* newBlock = reallocate(NULL, 0, newSize);

  // Copy min(old, new) bytes
  size_t copySize = oldSize < newSize ? oldSize : newSize;
  memcpy(newBlock, pointer, copySize);

  return newBlock;
}
