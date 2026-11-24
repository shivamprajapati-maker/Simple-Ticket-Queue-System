# Simple Ticket Queue System

Console application in **Java** for a support ticket queue (PS No. 34, Data Structures and Algorithms).

**Author:** Shivam Prajapati | **ERP ID:** RU-25-11374
**University:** Rungta International Skills University, Bhilai (School of Computer Science and Engineering)

## Features
1. **Raise Ticket** - enter customer name and issue; ID is generated automatically (first ID is `T101`)
2. **Serve Next Ticket** - removes the oldest ticket (strict FIFO); shows a message if the queue is empty
3. **View Queue** - lists all waiting tickets with their position
4. **Search Ticket by ID** - manual linear search (no built-in search/sort methods)
5. **Exit** - saves all waiting tickets to `tickets.txt`

Tickets are loaded from `tickets.txt` when the program starts and saved when it exits.

## Concepts used
- Queue implemented with a **linked list** (`Node` class, `head` and `tail` references)
- Each record stored as an object (`Ticket`)
- Methods, `Scanner` input validation, **file handling** (`BufferedReader` / `BufferedWriter`)

| Operation | Time complexity |
|---|---|
| Raise ticket (enqueue) | O(1) |
| Serve ticket (dequeue) | O(1) |
| View queue | O(n) |
| Search by ID | O(n) |

## How to run
Requires JDK 11 or higher (JDK 21 recommended).

```bash
javac TicketQueueSystem.java
java TicketQueueSystem
```

## Sample run
```
===== Simple Ticket Queue System =====
1. Raise Ticket
2. Serve Next Ticket
3. View Queue
4. Search Ticket by ID
5. Exit
Enter your choice: 1
Customer: Meena
Issue: Login problem
Ticket raised. ID: T101 | Meena | Login problem | Position: 1
```

## File format (`tickets.txt`)
One ticket per line: `ID|Customer|Issue`

```
T101|Meena|Login problem
T102|Sameer|Payment failed
```

## Test cases
| # | Input | Output |
|---|---|---|
| 1 | Option 1, Meena, Login problem | `Ticket raised. ID: T101 \| Meena \| Login problem \| Position: 1` |
| 2 | Queue has only T101, option 2 | `Ticket T101 served. Customer: Meena` then `Tickets waiting: 0` |
| 3 | Empty queue, option 2 | `No tickets in the queue.` |
| 4 | T101 exists; raise Sameer, serve, view | `T102 raised, position 2` -> `T101 served` -> `Waiting: 1 / 1. T102 \| Sameer \| Payment failed` |
