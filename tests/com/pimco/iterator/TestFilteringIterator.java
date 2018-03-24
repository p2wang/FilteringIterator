package com.pimco.iterator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.pimco.iterator.FilteringIterator.IObjectTest;

/**
 * JUnit test cases for FilteringIterator
 * 
 * @author Paul Wang (paul.y.wang@gmail.com)
 */
public class TestFilteringIterator {

	private List<Integer> dataStream;
	private IObjectTest<Integer> alwaysTrueFilter;
	private IObjectTest<Integer> alwaysFalseFilter;
	private IObjectTest<Integer> evenIntegerFilter;
	private IObjectTest<Integer> positiveIntegerFilter;
	private IObjectTest<Integer> oddNegativeIntegerFilter;

	@Before
	public void setUp() {
		dataStream = new ArrayList<>(Arrays.asList(7, 6, -2, 5, 4, 10, 1, -14));
		alwaysTrueFilter = (n) -> true;
		alwaysFalseFilter = (n) -> false;
		evenIntegerFilter = (n) -> n % 2 == 0;
		positiveIntegerFilter = (n) -> n >= 0;
		oddNegativeIntegerFilter = (n) -> (n < 0) && (n % 2 != 0);
	}

	/**
	 * Test method for
	 * {@link com.pimco.iterator.FilteringIterator#FilteringIterator(java.util.Iterator, com.pimco.iterator.FilteringIterator.IObjectTest)}.
	 */
	@SuppressWarnings("unused")
	@Test
	public void testFilteringIterator() {
		try {
			// test that a null iterator throws a null pointer exception
			FilteringIterator<Integer> iterator = new FilteringIterator<>(null, alwaysTrueFilter);
			fail("The null iterator should've thrown a NullPointerException");
		} catch (NullPointerException e) {
			// this is a successful test
		}

		try {
			// test that a null filter throws a null pointer exception
			FilteringIterator<Integer> iterator = new FilteringIterator<>(dataStream.iterator(), null);
			fail("The null filter should've thrown a NullPointerException");
		} catch (NullPointerException e) {
			// this is a successful test
		}

		// test that a non-null iterator and filter will result in a successful
		// instantiation of the filter iterator
		FilteringIterator<Integer> iterator = new FilteringIterator<>(dataStream.iterator(), alwaysTrueFilter);
		assertNotNull(iterator);
	}

	/**
	 * Test method for {@link com.pimco.iterator.FilteringIterator#hasNext()}.
	 */
	@Test
	public void testHasNext() {
		FilteringIterator<Integer> iterator = null;

		// test that an empty iterator should have hasNext() return false
		iterator = new FilteringIterator<>(new ArrayList<Integer>().iterator(), alwaysTrueFilter);
		assertFalse(iterator.hasNext());

		// test that an always true filter should have hasNext() return true for any
		// non-empty iterator
		iterator = new FilteringIterator<>(dataStream.iterator(), alwaysTrueFilter);
		assertTrue(iterator.hasNext());

		// test that an always false filter should have hasNext() return false for any
		// non-empty iterator
		iterator = new FilteringIterator<>(dataStream.iterator(), alwaysFalseFilter);
		assertFalse(iterator.hasNext());

		// test that an odd and negative integer filter should have hasNext() return
		// false for a non-empty iterator without negative odd values
		iterator = new FilteringIterator<>(dataStream.iterator(), oddNegativeIntegerFilter);
		assertFalse(iterator.hasNext());

		// test that an even integer filter should have hasNext() return true for a
		// non-empty iterator with even values
		iterator = new FilteringIterator<>(dataStream.iterator(), evenIntegerFilter);
		assertTrue(iterator.hasNext());
	}

	/**
	 * Test method for {@link com.pimco.iterator.FilteringIterator#next()}.
	 */
	@Test
	public void testNext() {
		FilteringIterator<Integer> iterator = null;
		List<Integer> result = new ArrayList<>();

		// test that an always true filter will iterate over every element of the
		// original data stream
		iterator = new FilteringIterator<>(dataStream.iterator(), alwaysTrueFilter);
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}
		assertArrayEquals(dataStream.toArray(), result.toArray());

		result.clear();
		
		// test that an always false filter will not iterate over any element of the
		// original data stream
		iterator = new FilteringIterator<>(dataStream.iterator(), alwaysFalseFilter);
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}
		assertTrue(result.isEmpty());

		result.clear();

		// test that an even integer filter will iterate over only even elements in the
		// iterator
		iterator = new FilteringIterator<>(dataStream.iterator(), evenIntegerFilter);
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}
		assertArrayEquals(new ArrayList<>(Arrays.asList(6, -2, 4, 10, -14)).toArray(), result.toArray());

		result.clear();

		// test that a positive integer filter will iterator over only positive elements
		// in the iterator
		iterator = new FilteringIterator<>(dataStream.iterator(), positiveIntegerFilter);
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}
		assertArrayEquals(new ArrayList<>(Arrays.asList(7, 6, 5, 4, 10, 1)).toArray(), result.toArray());

		result.clear();
	}

}
