package ru.praktikum.kanban.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.praktikum.kanban.model.entity.Task;

public class HistoryLinkedList {

    static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }
    }

    private Node<Task> head;

    private Node<Task> tail;

    public int size() {
        return hashMap.size();
    }

    private final HashMap<Integer, Node<Task>> hashMap;

    public HistoryLinkedList() {
        this.head = null;
        this.tail = null;
        this.hashMap = new HashMap<>();
    }

    public void add(Task value) {
        Node<Task> node = hashMap.get(value.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(value);
        hashMap.put(value.getId(), tail);
    }

    public void remove(int id) {
        Node<Task> node = hashMap.get(id);
        hashMap.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    public List<Task> values() {
        if (head == null) {
            return List.of();
        }
        ArrayList<Task> values = new ArrayList<>();
        Node<Task> next = head;
        while (next != null) {
            values.add(next.data);
            next = next.next;
        }
        return values;
    }

    public void clear() {
        hashMap.clear();
        tail = null;
        head = null;
    }

    public void putAll(List<Task> values) {
        for (Task value : values) {
            add(value);
        }
    }

    private void removeNode(Node<Task> node) {
        Node<Task> prev = node.prev;
        Node<Task> next = node.next;
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }
    }

    private void linkLast(Task value) {
        final Node<Task> newNode = new Node<>(tail, value, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }
}
