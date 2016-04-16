/*
 * Copyright 2012 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.rapidware.queues;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * <ul>
 * <li>Lock free, observing single writer principal.
 * </ul>
 */
public final class P1C1QueueOriginal1<T_ElementType> implements Queue<T_ElementType> {
    private final T_ElementType[] buffer_;

    private volatile long tail_ = 0;
    private volatile long head_ = 0;

    @SuppressWarnings("unchecked")
    public P1C1QueueOriginal1(final int capacity) {
        buffer_ = (T_ElementType[]) new Object[capacity];
    }

    public boolean add(final T_ElementType e) {
        if (offer(e)) {
            return true;
        }

        throw new IllegalStateException("Queue is full");
    }

    public boolean offer(final T_ElementType e) {
        if (null == e) {
            throw new NullPointerException("Null is not a valid element");
        }

        final long currentTail = tail_;
        final long wrapPoint = currentTail - buffer_.length;
        if (head_ <= wrapPoint) {
            return false;
        }

        buffer_[(int) (currentTail & (buffer_.length - 1))] = e;
        tail_ = currentTail + 1;

        return true;
    }

    public T_ElementType poll() {
        final long currentHead = head_;
        if (currentHead >= tail_) {
            return null;
        }

        final int index = (int) (currentHead % buffer_.length);
        final T_ElementType e = buffer_[index];
        buffer_[index] = null;
        head_ = currentHead + 1;

        return e;
    }

    public T_ElementType remove() {
        final T_ElementType e = poll();
        if (null == e) {
            throw new NoSuchElementException("Queue is empty");
        }

        return e;
    }

    public T_ElementType element() {
        final T_ElementType e = peek();
        if (null == e) {
            throw new NoSuchElementException("Queue is empty");
        }

        return e;
    }

    public T_ElementType peek() {
        return buffer_[(int) (head_ % buffer_.length)];
    }

    public int size() {
        return (int) (tail_ - head_);
    }

    public boolean isEmpty() {
        return tail_ == head_;
    }

    public boolean contains(final Object o) {
        if (null == o) {
            return false;
        }

        for (long i = head_, limit = tail_; i < limit; i++) {
            final T_ElementType e = buffer_[(int) (i % buffer_.length)];
            if (o.equals(e)) {
                return true;
            }
        }

        return false;
    }

    public Iterator<T_ElementType> iterator() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(final T[] a) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    public boolean addAll(final Collection<? extends T_ElementType> c) {
        for (final T_ElementType e : c) {
            add(e);
        }

        return true;
    }

    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        Object value;
        do {
            value = poll();
        } while (null != value);
    }
}