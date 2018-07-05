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

import java.io.Serializable;
import java.util.Collection;

/**
 * Interface of Bloom Filter.
 *
 * A Bloom filter is a space-efficient probabilistic data structure
 * that is used to test whether an element is a member of a set.
 * A query returns either "possibly in set" or "definitely not in set".
 * Elements can be added to the set, but not removed.
 *
 * @param <E> - the type of elements in this Bloom Filter.
 * @author Damian Stygar
 */
public interface BloomFilter<E> extends Serializable {

    /**
     * The add method enables you to insert element to Bloom Filter.
     *
     * @param element - an element to be inserted to Bloom Filter.
     */
    void add(E element);

    /**
     * The addAll method enables you to insert each element from collection to Bloom Filter.
     *
     * @param collection - a collection with elements to be inserted to Bloom Filter.
     */
    void addAll(Collection<? extends E> collection);

    /**
     * The mightContains method enables you to check if Bloom Filter may contains element.
     *
     * @param element - an element to be checked.
     * @return - True if Bloom Filter can contains element (Remember that can be false positive result).
     * False if Bloom Filter cannot contains element.
     */
    boolean mightContains(E element);

    /**
     * The mightContainsAll method enables you to check if Bloom Filter may contains each element from collection.
     *
     * @param collection - a collection with elements to be checked.
     * @return - True if Bloom Filter can contains each element (Remember that can be false positive result).
     * False if Bloom Filter cannot contains each element.
     */
    boolean mightContainsAll(Collection<? extends E> collection);

    /**
     * The getExpectedProbabilityOfFalsePositives method enables you to get expected probability of false positives.
     *
     * @return - expected probability of false positives.
     */
    double getExpectedProbabilityOfFalsePositives();

    /**
     * The getCurrentProbabilityOfFalsePositives method enables you to get actual probability of false positives.
     *
     * @return - actual probability of false positives.
     */
    double getCurrentProbabilityOfFalsePositives();

    /**
     * The getProbabilityOfFalsePositives method enables you to get probability of false positives based on parameter.
     *
     * @param numberOfElements - a number of elements in Bloom Filter.
     * @return - probability of false positives based on parameter.
     */
    double getProbabilityOfFalsePositives(int numberOfElements);

    /**
     * The getSize method enables you to get size of Bloom Filter.
     *
     * @return - size of Bloom Filter.
     */
    int getSize();

    /**
     * The clear method enables you to delete all elements from Bloom Filter.
     */
    void clear();

    /**
     * The isEmpty method enables you to check if Bloom Filter is empty.
     *
     * @return True, if Bloom Filter is empty.
     * False, if Bloom Filter is not empty.
     */
    boolean isEmpty();

    /**
     * The getNumberOfElements method enables you to get number of inserted elements.
     *
     * @return - number of inserted elements.
     */
    int getNumberOfElements();

    /**
     * The getExpectedNumberOfElements method enables you to get expected number of inserted elements.
     *
     * @return - expected number of inserted elements.
     */
    int getExpectedNumberOfElements();

    /**
     * The getExpectedBitsPerElement method enables you to get expected bits per element.
     *
     * @return - expected bits per element.
     */
    double getExpectedBitsPerElement();

    /**
     * The getBitsPerElement method enables you to get actual bits per element.
     *
     * @return - actual bits per element.
     * @throws Exception - when actual number of inserted element = 0.
     */
    double getBitsPerElement() throws Exception;

    /**
     * The getNumberOfHash method enables you to get number of hash functions.
     *
     * @return - number of hash functions.
     */
    int getNumberOfHash();

}
