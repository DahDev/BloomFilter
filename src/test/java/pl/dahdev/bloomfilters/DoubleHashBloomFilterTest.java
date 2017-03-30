/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.dahdev.bloomfilters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Damian Stygar
 * @see <www.dahdev.pl>
 */
public class DoubleHashBloomFilterTest {

    private static final double PROBABILITY_OF_FALSE_POSITIVES = 0.001;
    private static final int EXPECTED_NUMBER_OF_ELEMENTS = 10;
    private static final int SIZE_OF_BLOOM_FILTER = 100;
    private static final double EPSILON = 0.01;
    private static final String FIRST_ELEMENT = "First element";
    private static final String SECOND_ELEMENT = "Second element";
    private static final String THIRD_ELEMENT = "Third element";

    private BloomFilter<String> filter;

    @Before
    public void initBloomFilterWithTreeElements() {
        this.filter = new DoubleHashBloomFilter<String>(PROBABILITY_OF_FALSE_POSITIVES, EXPECTED_NUMBER_OF_ELEMENTS);
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
        new DoubleHashBloomFilter<Integer>(0, EXPECTED_NUMBER_OF_ELEMENTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithZeroExpectedElementsTest() {
        new DoubleHashBloomFilter<Integer>(SIZE_OF_BLOOM_FILTER, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTheSameHashFunctionsTest() throws NoSuchAlgorithmException {
        MessageDigest hashFunction = MessageDigest.getInstance("SHA-1");
        new DoubleHashBloomFilter<Integer>(SIZE_OF_BLOOM_FILTER, EXPECTED_NUMBER_OF_ELEMENTS, hashFunction, hashFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNullOfHashFunctionsTest() throws NoSuchAlgorithmException {
        MessageDigest hashFunction = MessageDigest.getInstance("SHA-1");
        new DoubleHashBloomFilter<Integer>(PROBABILITY_OF_FALSE_POSITIVES, EXPECTED_NUMBER_OF_ELEMENTS, null, hashFunction);
    }

}
