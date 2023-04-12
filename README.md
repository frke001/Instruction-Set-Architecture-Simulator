# Instruction Set Architecture Simulator

This is a simulator for a custom Instruction Set Architecture (ISA) written in any programming language. The simulator should function as an interpreter and should support the following features:

- Four general-purpose 64-bit registers
- Loading source assembly code from a file
- Lexical, syntactic, and semantic analysis of the code
- Basic arithmetic operations (ADD, SUB, MUL, DIV)
- Basic bitwise logical operations (AND, OR, NOT, XOR)
- Instruction for moving data between registers (MOV)
- Instruction for inputting data from standard input
- Instruction for outputting data to standard output
- Single-step debugging support with the ability to view register and memory values at breakpoints, and the ability to step to the next instruction or jump to the next breakpoint
- Memory management with a 64-bit address space and support for direct and indirect addressing
- Unconditional and conditional branching (JMP, CMP, JE, JNE, JGE, JL)
- Translation of assembly instructions to bytecode stored in the guest's memory
- Definition and use of a program counter (instruction pointer)
- Self-modifying assembly code

## Requirements

- Programming language of choice
- 64-bit arithmetic support
- File I/O support
- Console I/O support
- Debugging support
