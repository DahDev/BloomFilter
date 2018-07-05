/**
 * MIT License
 *
 * Copyright (c) 2017 Damian Stygar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package pl.dahdev.bloomfilters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Damian Stygar
 */
public class TripleHashBloomFilterTest {
    private static final double PROBABILITY_OF_FALSE_POSITIVES = 0.001;
    private static final int EXPECTED_NUMBER_OF_ELEMENTS = 10;
    private static final int SIZE_OF_BLOOM_FILTER = 100;
    private static final double EPSILON = 0.001;
    private static final String FIRST_ELEMENT = "First element";
    private static final String SECOND_ELEMENT = "Second element";
    private static final String THIRD_ELEMENT = "Third element";

    private BloomFilter<String> filter;

    @Before
    public void initBloomFilterWithTreeElements() {
        this.filter = new TripleHashBloomFilter<String>(PROBABILITY_OF_FALSE_POSITIVES, EXPECTED_NUMBER_OF_ELEMENTS);
        filter.add(FIRST_ELEMENT);
        filter.add(SECOND_ELEMENT);
        filter.add(THIRD_ELEMENT);
    }

    @Test
    public void clearMethodTest() {
        filter.clear();
        Assert.assertEquals(0, filter.getNumberOfElements());
    }

    @Test(expected = Exception.class)
    public void getBitsPerElementWhenFilterIsEmpty() throws Exception {
        filter.clear();
        filter.getBitsPerElement();
    }

    @Test
    public void getBitsPerElementMethodTest() throws Exception {
        Assert.assertEquals(filter.getSize() / (double) filter.getNumberOfElements(), filter.getBitsPerElement(), EPSILON);
    }

    @Test
    public void getExpectedBitsPerElementMethodTest() throws Exception {
        Assert.assertEquals(filter.getSize() / (double) filter.getExpectedNumberOfElements(),
                filter.getExpectedBitsPerElement(), EPSILON);
    }

    @Test
    public void mightContainsMethodTest() {
        Assert.assertTrue(filter.mightContains(FIRST_ELEMENT));
        Assert.assertTrue(filter.mightContains(SECOND_ELEMENT));
        Assert.assertTrue(filter.mightContains(THIRD_ELEMENT));
    }

    @Test
    public void mightContainsAllMethodTest() {
        List<String> elementList = new ArrayList<String>();
        elementList.add(FIRST_ELEMENT);
        elementList.add(SECOND_ELEMENT);
        elementList.add(THIRD_ELEMENT);
        Assert.assertTrue(filter.mightContainsAll(elementList));
    }

    @Test
    public void addMethodTest() {
        int elementsInFilter = filter.getNumberOfElements();
        filter.add("New element.");
        Assert.assertEquals(elementsInFilter + 1, filter.getNumberOfElements());
    }

    @Test
    public void addMethodAllTest() {
        int elementsInFilter = filter.getNumberOfElements();
        List<String> elementList = new ArrayList<String>();
        elementList.add("First new element.");
        elementList.add("Second new element.");
        filter.addAll(elementList);
        Assert.assertEquals(elementsInFilter + elementList.size(), filter.getNumberOfElements());
    }

    @Test
    public void getExpectedProbabilityOfFalsePositivesMethodTest() throws Exception {
        double expectedProbabilityOfFalsePositives =
                Math.pow((1 - Math.exp(-(filter.getNumberOfHash() * filter.getExpectedNumberOfElements())
                        / (double) filter.getSize())), filter.getNumberOfHash());
        Assert.assertEquals(expectedProbabilityOfFalsePositives, filter.getExpectedProbabilityOfFalsePositives(), EPSILON);
    }

    @Test
    public void getCurrentProbabilityOfFalsePositivesMethodTest() throws Exception {
        double currentProbabilityOfFalsePositives =
                Math.pow((1 - Math.exp(-(filter.getNumberOfHash() * filter.getNumberOfElements())
                        / (double) filter.getSize())), filter.getNumberOfHash());
        Assert.assertEquals(currentProbabilityOfFalsePositives, filter.getCurrentProbabilityOfFalsePositives(), EPSILON);
    }

    @Test
    public void isEmptyMethodTest() {
        Assert.assertFalse(filter.isEmpty());
        filter.clear();
        Assert.assertTrue(filter.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithZeroSizeTest() {
        new TripleHashBloomFilter<Integer>(0, EXPECTED_NUMBER_OF_ELEMENTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithZeroExpectedElementsTest() {
        new TripleHashBloomFilter<Integer>(SIZE_OF_BLOOM_FILTER, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTheSameHashFunctionsTest() throws NoSuchAlgorithmException {
        MessageDigest firstHashFunction = MessageDigest.getInstance("SHA-1");
        MessageDigest secondHashFunction = MessageDigest.getInstance("MD5");
        new TripleHashBloomFilter<Integer>(SIZE_OF_BLOOM_FILTER, EXPECTED_NUMBER_OF_ELEMENTS,
                firstHashFunction, secondHashFunction, firstHashFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNullOfHashFunctionsTest() throws NoSuchAlgorithmException {
        MessageDigest firstHashFunction = MessageDigest.getInstance("SHA-1");
        MessageDigest secondHashFunction = MessageDigest.getInstance("MD5");
        new TripleHashBloomFilter<Integer>(PROBABILITY_OF_FALSE_POSITIVES, EXPECTED_NUMBER_OF_ELEMENTS,
                firstHashFunction, secondHashFunction, null);
    }
}
