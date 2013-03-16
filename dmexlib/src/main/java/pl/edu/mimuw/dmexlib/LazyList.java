/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;

/**
 *
 * @author matek
 */
public class LazyList<Element, Operation extends IGenerateOperation<Element>> implements List<Element>{
    
    public class Iterator implements ListIterator<Element> {

        private Iterator() {
            this(from);
        }
        
        private Iterator(int pos) {
            position = new AtomicInteger(pos);
        }
        

        @Override
        public boolean hasNext() {
            return position.get() < to;
        }

        @Override
        public Element next() {
            if(to <= position.get()) {
                throw new NoSuchElementException();
            }
            
            return generator.invoke(position.getAndIncrement());
        }

        @Override
        public boolean hasPrevious() {
            return from < position.get();
        }

        @Override
        public Element previous() {
            if(position.get() <= from) {
                throw new NoSuchElementException();
            }
            
            return generator.invoke(position.decrementAndGet());
        }

        @Override
        public int nextIndex() {
            final int pos = position.get();
            
            if(pos < to) {
                return pos + 1;
            }
            else {
                return size();
            }
        }

        @Override
        public int previousIndex() {
            final int pos = position.get();
            
            if(from < pos) {
                return pos - 1;
            }
            else {
                return -1;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void set(Element e) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void add(Element e) {
            throw new UnsupportedOperationException("Not supported.");
        }
        
        private AtomicInteger position;
    }

    public LazyList(int count, Operation generator) {
        this(0, count, generator);
    }
    
    private LazyList(int from, int to, Operation generator) {
        this.from = from;
        this.to = to;
        this.generator = generator;
        
    }

    @Override
    public int size() {
        return to-from;
    }

    @Override
    public boolean isEmpty() {
        return 0==size();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return new Iterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for(Iterator it=iterator(); it.hasNext();) {
            result[i++] = it.next();
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = (T[])toArray();
        
        if(size() <= a.length) {
            System.arraycopy(result, 0, a, 0, size());
            result = a;
        }
        
        return result;
    }

    @Override
    public boolean add(Element e) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Element> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends Element> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Element get(int index) {
        if(index < 0 || size() <= index) {
            throw new IndexOutOfBoundsException();
        }
        
        return generator.invoke(index);
    }

    @Override
    public Element set(int index, Element element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void add(int index, Element element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Element remove(int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int indexOf(Object o) {
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return -1;
    }

    @Override
    public Iterator listIterator() {
        return new Iterator();
    }

    @Override
    public Iterator listIterator(int index) {
        return new Iterator(index);
    }

    @Override
    public List<Element> subList(int fromIndex, int toIndex) {
        return new LazyList<Element, Operation>(fromIndex, toIndex, generator);
    }
    
    private final int from;
    private final int to;
    private final Operation generator;
}
