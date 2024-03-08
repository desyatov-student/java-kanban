package ru.praktikum.kanban.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;

public class HistoryLinkedList {

    class Node<E> {
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

    private Node<BaseTaskDto> head;

    private Node<BaseTaskDto> tail;

    public int size() {
        return hashMap.size();
    }

    private final HashMap<Integer, Node<BaseTaskDto>> hashMap;

    public HistoryLinkedList() {
        this.head = null;
        this.tail = null;
        this.hashMap = new HashMap<>();
    }

    public void add(BaseTaskDto value) {
        Node<BaseTaskDto> node = hashMap.get(value.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(value);
        hashMap.put(value.getId(), tail);
    }

    public void remove(int id) {
        Node<BaseTaskDto> node = hashMap.get(id);
        hashMap.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    public List<BaseTaskDto> values() {
        if (head == null) {
            return List.of();
        }
        ArrayList<BaseTaskDto> values = new ArrayList<>();
        Node<BaseTaskDto> next = head;
        while (next != null) {
            values.add(next.data);
            next = next.next;
        }
        return values;
    }

    private void removeNode(Node<BaseTaskDto> node) {
        Node<BaseTaskDto> prev = node.prev;
        Node<BaseTaskDto> next = node.next;
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

    private void linkLast(BaseTaskDto value) {
        final Node<BaseTaskDto> newNode = new Node<>(tail, value, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }
}
