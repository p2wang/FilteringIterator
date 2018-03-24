package com.pimco.iterator;

import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Iterator} which will return elements based on a supplied
 * {@link IObjectTest} filtering criteria.
 * 
 * @author Paul Wang (paul.y.wang@gmail.com)
 * @param <E>
 *            the type of the object being iterated over
 * @see Iterator
 * @see IObjectTest
 */
public class FilteringIterator<E> implements Iterator<E> {

	/**
	 * Filtering test criteria.
	 *
	 * @param <E>
	 *            the type of the object being tested
	 */
	@FunctionalInterface
	public interface IObjectTest<E> {

		/**
		 * Test if an object satisfies the test criteria.
		 * 
		 * @param object
		 *            the object
		 * @return true if the object satisfies the criteria, false otherwise
		 */
		boolean test(E object);
	}

	private final Iterator<E> iterator;
	private final IObjectTest<E> filter;

	private E data;

	/**
	 * Filtering iterator constructor which takes an {@link Iterator} and a
	 * {@link IObjectTest}. This will throw a {@link NullPointerException} of either
	 * the input iterator or the filter is null.
	 * 
	 * @param iterator
	 *            the iterator
	 * @param filter
	 *            the filter or test criteria
	 * @see Iterator
	 * @see IObjectTest
	 */
	public FilteringIterator(Iterator<E> iterator, IObjectTest<E> filter) {
		this.iterator = Objects.requireNonNull(iterator, "iterator must not be null");
		this.filter = Objects.requireNonNull(filter, "filter must not be null");
		this.data = fetchNextValue();
	}

	/**
	 * Checks if there exists the next element in the {@link Iterator}
	 * 
	 * @return true if there is an element which matches the test criteria, false
	 *         otherwise
	 */
	@Override
	public boolean hasNext() {
		return data != null;
	}

	/**
	 * Return the next element in the {@link Iterator}
	 * 
	 * @return the next element, or null if none exists
	 */
	@Override
	public E next() {
		E temp = data;
		data = fetchNextValue();
		return temp;
	}

	/**
	 * Iterates over the base input iterator and fetches the next value which
	 * satisfies the filtering criteria
	 * 
	 * @return the next value or null if either no values available or no values
	 *         match the criteria
	 */
	private E fetchNextValue() {
		E retval = null;
		E temp = null;
		while (iterator.hasNext()) {
			temp = iterator.next();
			if (filter.test(temp)) {
				retval = temp;
				break;
			}
		}
		return retval;
	}

}
