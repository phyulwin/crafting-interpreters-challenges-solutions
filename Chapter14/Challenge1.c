/*
Group consecutive instructions by shared line numbers.
Store line and count per group.
This reduces memory usage significantly.
Most instructions share the same line.
Lookup scans groups cumulatively.
Performance impact is negligible here.
Used only during runtime errors.
 */

// The following code is for assignment submission only.
// It is not standalone or execution ready.

// In chunk.h: 

typedef struct {
  int line;   // source line number
  int count;  // how many consecutive instructions use this line
} LineStart;

typedef struct {
  int count;
  int capacity;
  LineStart* lines;
} LineArray;

typedef struct {
  int count;
  int capacity;
  uint8_t* code;
  LineArray lineInfo;     // compressed line storage (RLE)
  ValueArray constants;
} Chunk;

// In chunk.c: 

// Writes bytecode + compresses line info using run-length encoding
void writeChunk(Chunk* chunk, uint8_t byte, int line) {
  // Ensure capacity for bytecode
  if (chunk->capacity < chunk->count + 1) {
    int old = chunk->capacity;
    chunk->capacity = GROW_CAPACITY(old);
    chunk->code = GROW_ARRAY(uint8_t, chunk->code, old, chunk->capacity);
  }

  // Append instruction byte
  chunk->code[chunk->count++] = byte;

  // RLE compression: extend last run or create new one
  if (chunk->lineInfo.count == 0 ||
      chunk->lineInfo.lines[chunk->lineInfo.count - 1].line != line) {

    // Need new run entry
    if (chunk->lineInfo.capacity < chunk->lineInfo.count + 1) {
      int old = chunk->lineInfo.capacity;
      chunk->lineInfo.capacity = GROW_CAPACITY(old);
      chunk->lineInfo.lines = GROW_ARRAY(LineStart,
        chunk->lineInfo.lines, old, chunk->lineInfo.capacity);
    }

    chunk->lineInfo.lines[chunk->lineInfo.count++] =
      (LineStart){ line, 1 }; // start new run

  } else {
    // Same line → extend run
    chunk->lineInfo.lines[chunk->lineInfo.count - 1].count++;
  }
}

// Returns source line for instruction index (linear scan acceptable)
int getLine(Chunk* chunk, int instruction) {
  int accumulated = 0;

  for (int i = 0; i < chunk->lineInfo.count; i++) {
    accumulated += chunk->lineInfo.lines[i].count;

    if (instruction < accumulated) {
      return chunk->lineInfo.lines[i].line;
    }
  }

  return -1; // fallback (invalid index)
}
