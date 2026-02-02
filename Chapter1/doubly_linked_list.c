/* doubly_linked_list.c
 * Demonstrates manual memory management and pointer discipline in C.
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Node structure holding a heap-allocated string. */
typedef struct Node {
    char *value;
    struct Node *prev;
    struct Node *next;
} Node;

/* Insert a new node at the head of the list. */
Node* insert(Node *head, const char *value) {
    Node *node = malloc(sizeof(Node));
    node->value = strdup(value);
    node->prev = NULL;
    node->next = head;
    if (head) head->prev = node;
    return node;
}

/* Find the first node matching the given string. */
Node* find(Node *head, const char *value) {
    for (Node *cur = head; cur; cur = cur->next)
        if (strcmp(cur->value, value) == 0) return cur;
    return NULL;
}

/* Delete a node and maintain list integrity. */
Node* delete(Node *head, Node *node) {
    if (!node) return head;
    if (node->prev) node->prev->next = node->next;
    if (node->next) node->next->prev = node->prev;
    if (node == head) head = node->next;
    free(node->value);
    free(node);
    return head;
}

/* Basic validation of list operations. */
int main(void) {
    Node *head = NULL;
    head = insert(head, "alpha");
    printf("Inserted: alpha\n");
    head = insert(head, "beta");
    printf("Inserted: beta\n");
    Node *n = find(head, "alpha");
    printf("Found: %s\n", n ? n->value : "NULL");
    head = delete(head, n);
    printf("Deleted: alpha\n");
    printf("Basic validation of list operations completed.\n");
    return 0;
}

// Expected output:
// Inserted: alpha
// Inserted: beta
// Found: alpha
// Deleted: alpha
// Basic validation of list operations completed.