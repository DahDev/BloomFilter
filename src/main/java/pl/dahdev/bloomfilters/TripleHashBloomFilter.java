/**
 * MIT License
 * <p/>
 * Copyright (c) 2017 Damian Stygar
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package pl.dahdev.bloomfilters;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of Bloom Filter using Triple Hashing.
 * <p/>
 * A Bloom filter is a space-efficient probabilistic data structure
 * that is used to test whether an element is a member of a set.
 * A query returns either "possibly in set" or "definitely not in set".
 * Elements can be added to the set, but not removed.
 *
 * @param <E> - the type of elements in this Bloom Filter.
 * @author Damian Stygar
 */
public class TripleHashBloomFilter<E> extends AbstractBloomFilter<E> {

    private static final String DEFAULT_FIRST_HASH_FUNCTION = "SHA-1";
    private static final String DEFAULT_SECOND_HASH_FUNCTION = "MD5";
    private static final String DEFAULT_THIRD_HASH_FUNCTION = "SHA-512";

    private MessageDigest firstHash;
    private MessageDigest secondHash;
    private MessageDigest thirdHash;

    /**
     * Triple Hashing Bloom filter constructor.
     * <p/>
     * Size of Bloom Filter is estimated from:
     * m = (-n*ln(p))/(ln(2))^2,
     * where m is size of Bloom Filter, n is number of expected elements, p is probability of false positives.
     *
     * @param probabilityOfFalsePositives probability of false positives.
     * @param expectedNumberOfElements    expected number of elements to be inserted to Bloom Filter.
     */
    public TripleHashBloomFilter(double probabilityOfFalsePositives, int expectedNumberOfElements) {
        super(probabilityOfFalsePositives, expectedNumberOfElements);
        try {
            firstHash = MessageDigest.getInstance(DEFAULT_FIRST_HASH_FUNCTION);
            secondHash = MessageDigest.getInstance(DEFAULT_SECOND_HASH_FUNCTION);
            thirdHash = MessageDigest.getInstance(DEFAULT_THIRD_HASH_FUNCTION);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Cannot get instance of hash functions!");
        }
    }

    /**
     * Triple Hashing Bloom filter constructor.
     * <p/>
     * Size of Bloom Filter is estimated from:
     * m = (-n*ln(p))/(ln(2))^2,
     * where m is size of Bloom Filter, n is number of expected elements, p is probability of false positives.
     *
     * @param probabilityOfFalsePositives probability of false positives.
     * @param expectedNumberOfElements    expected number of elements to be inserted to Bloom Filter.
     * @param firstHashFunction           first hash function.
     * @param secondHashFunction          second hash function.
     * @param thirdHashFunction           third hash function.
     */
    public TripleHashBloomFilter(double probabilityOfFalsePositives, int expectedNumberOfElements,
                                 MessageDigest firstHashFunction, MessageDigest secondHashFunction,
                                 MessageDigest thirdHashFunction) {
        super(probabilityOfFalsePositives, expectedNumberOfElements);
        if ((firstHashFunction == null) || (secondHashFunction == null) || (thirdHashFunction == null)) {
            throw new IllegalArgumentException("Instance of hash function cannot be null!");
        }
        if (firstHashFunction.equals(secondHashFunction) || secondHashFunction.equals(thirdHashFunction)
                || firstHashFunction.equals(thirdHashFunction)) {
            throw new IllegalArgumentException("Hash functions cannot be the same!");
        }
        firstHash = firstHashFunction;
        secondHash = secondHashFunction;
        thirdHash = thirdHashFunction;
    }

    /**
     * Triple Hashing Bloom filter constructor.
     * <p/>
     * Number of hash functions is estimated from:
     * k = (m/n)ln(2),
     * where k is number of hash functions, m is size of Bloom Filter, n is number of expected elements.
     * <p/>
     * Bits per element are estimated from:
     * pbs = m/n,
     * where m is size of Bloom Filter, n is number of expected elements.
     * <p/>
     * Size of Bloom Filter and number of expected elements should be greater than 0.
     *
     * @param size                     size of Bloom Filter.
     * @param expectedNumberOfElements expected number of elements to be inserted to Bloom Filter.
     */
    public TripleHashBloomFilter(int size, int expectedNumberOfElements) {
        super(size, expectedNumberOfElements);
        try {
            firstHash = MessageDigest.getInstance(DEFAULT_FIRST_HASH_FUNCTION);
            secondHash = MessageDigest.getInstance(DEFAULT_SECOND_HASH_FUNCTION);
            thirdHash = MessageDigest.getInstance(DEFAULT_THIRD_HASH_FUNCTION);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Cannot get instance of hash functions!");
        }
    }

    /**
     * Triple Hashing Bloom filter constructor.
     * <p/>
     * Number of hash functions is estimated from:
     * k = (m/n)ln(2),
     * where k is number of hash functions, m is size of Bloom Filter, n is number of expected elements.
     * <p/>
     * Bits per element are estimated from:
     * pbs = m/n,
     * where m is size of Bloom Filter, n is number of expected elements.
     * <p/>
     * Size of Bloom Filter and number of expected elements should be greater than 0.
     *
     * @param size                     size of Bloom Filter.
     * @param expectedNumberOfElements expected number of elements to be inserted to Bloom Filter.
     * @param firstHashFunction        first hash function.
     * @param secondHashFunction       second hash function.
     * @param thirdHashFunction        third hash function.
     */
    public TripleHashBloomFilter(int size, int expectedNumberOfElements,
                                 MessageDigest firstHashFunction, MessageDigest secondHashFunction,
                                 MessageDigest thirdHashFunction) {
        super(size, expectedNumberOfElements);
        if ((firstHashFunction == null) || (secondHashFunction == null) || (thirdHashFunction == null)) {
            throw new IllegalArgumentException("Instance of hash function cannot be null!");
        }
        if (firstHashFunction.equals(secondHashFunction) || secondHashFunction.equals(thirdHashFunction)
                || firstHashFunction.equals(thirdHashFunction)) {
            throw new IllegalArgumentException("Hash functions cannot be the same!");
        }
        firstHash = firstHashFunction;
        secondHash = secondHashFunction;
        thirdHash = thirdHashFunction;
    }

    /**
     * The createHashes method enables you to create hash functions.
     *
     * @param bytes        the byte array contains data.
     * @param numberOfHash number of hash function.
     * @return int array with result hashes.
     */
    @Override
    protected int[] createHashes(byte[] bytes, int numberOfHash) {
        int valueA, valueB, valueC;
        int[] hashes = new int[numberOfHash];

        valueA = getValueFromGeneratedHash(bytes, firstHash);
        valueB = getValueFromGeneratedHash(bytes, secondHash);
        valueC = getValueFromGeneratedHash(bytes, thirdHash);

        hashes[0] = valueA;
        for (int i = 0; i < numberOfHash; i++) {
            valueA = (valueA + valueB) % size;
            valueB = (valueB + valueC) % size;
            hashes[i] = valueA;
        }
        return hashes;
    }

}
