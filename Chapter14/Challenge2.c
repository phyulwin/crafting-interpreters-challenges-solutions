/*
Two instruction sizes are introduced.
Small constants use one byte operand.
Large constants use three bytes.
Most programs remain memory efficient.
Supports larger constant pools when needed.
Adds decoding complexity in the VM.
Instruction size becomes less uniform.
*/

// The following code is for assignment submission only.
// It is not standalone or execution ready.

// chunk.h
typedef enum {
  OP_CONSTANT,
  OP_CONSTANT_LONG, // 24-bit operand
  OP_RETURN,
} OpCode;

// chunk.c

// Writes constant using optimal encoding (1-byte or 3-byte index)
void writeConstant(Chunk* chunk, Value value, int line) {
  // Add constant to pool
  int index = addConstant(chunk, value);

  if (index <= 0xFF) {
    // Fast path: single-byte operand
    writeChunk(chunk, OP_CONSTANT, line);
    writeChunk(chunk, (uint8_t)index, line);

  } else {
    // Extended path: 24-bit operand (3 bytes)
    writeChunk(chunk, OP_CONSTANT_LONG, line);

    writeChunk(chunk, (index >> 16) & 0xFF, line); // high byte
    writeChunk(chunk, (index >> 8) & 0xFF, line);  // mid byte
    writeChunk(chunk, index & 0xFF, line);         // low byte
  }
}

// debug.c

// Disassemble OP_CONSTANT_LONG
case OP_CONSTANT_LONG: {
  // Reconstruct 24-bit index
  int index = (chunk->code[offset + 1] << 16) |
              (chunk->code[offset + 2] << 8)  |
               chunk->code[offset + 3];

  printf("%-16s %4d '", "OP_CONSTANT_LONG", index);
  printValue(chunk->constants.values[index]);
  printf("'\n");

  return offset + 4; // opcode + 3 bytes
}

// Trade-offs:
// More opcode complexity
// Larger decoder logic
// Slight instruction size variability impacting predictability
