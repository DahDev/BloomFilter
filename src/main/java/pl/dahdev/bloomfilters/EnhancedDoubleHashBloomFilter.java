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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of Bloom Filter using Enhanced Double Hashing.
 * <p>
 * A Bloom filter is a space-efficient probabilistic data structure
 * that is used to test whether an element is a member of a set.
 * A query returns either "possibly in set" or "definitely not in set".
 * Elements can be added to the set, but not removed.
 *
 * @param <E> - the type of elements in this Bloom Filter.
 *
 * @author Damian Stygar
 * @see <www.dahdev.pl>
 */
public class EnhancedDoubleHashBloomFilter<E> extends AbstractBloomFilter<E> {

    private static final String DEFAULT_FIRST_HASH_FUNCTION = "SHA-1";
    private static final String DEFAULT_SECOND_HASH_FUNCTION = "MD5";

    private MessageDigest firstHash;
    private MessageDigest secondHash;

    /**
     * Enhanced Double Hashing Bloom filter constructor.
     * <p>
     * Size of Bloom Filter is estimated from:
     * m = (-n*ln(p))/(ln(2))^2,
     * where m is size of Bloom Filter, n is number of expected elements, p is probability of false positives.
     *
     * @param probabilityOfFalsePositives probability of false positives.
     * @param expectedNumberOfElements expected number of elements to be inserted to Bloom Filter.
     */
    public EnhancedDoubleHashBloomFilter(double probabilityOfFalsePositives, int expectedNumberOfElements) {
        super(probabilityOfFalsePositives, expectedNumberOfElements);
        try {
            firstHash = MessageDigest.getInstance(DEFAULT_FIRST_HASH_FUNCTION);
            secondHash = MessageDigest.getInstance(DEFAULT_SECOND_HASH_FUNCTION);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Cannot get instance of hash functions!");
        }
    }

    /**
     * Enhanced Double Hashing Bloom filter constructor.
     * <p>
     * Size of Bloom Filter is estimated from:
     * m = (-n*ln(p))/(ln(2))^2,
     * where m is size of Bloom Filter, n is number of expected elements, p is probability of false positives.
     *
     * @param probabilityOfFalsePositives probability of false positives.
     * @param expectedNumberOfElements    expected number of elements to be inserted to Bloom Filter.
     * @param firstHashFunction           first hash function.
     * @param secondHashFunction          second hash function.
     */
    public EnhancedDoubleHashBloomFilter(double probabilityOfFalsePositives, int expectedNumberOfElements,
                                         MessageDigest firstHashFunction, MessageDigest secondHashFunction) {
        super(probabilityOfFalsePositives, expectedNumberOfElements);
        if ((firstHashFunction == null) || (secondHashFunction == null)) {
            throw new IllegalArgumentException("Instance of hash function cannot be null!");
        }
        if (firstHashFunction.equals(secondHashFunction)) {
            throw new IllegalArgumentException("Hash functions cannot be the same!");
        }
        firstHash = firstHashFunction;
        secondHash = secondHashFunction;
    }

    /**
     * Enhanced Double Hashing Bloom filter constructor.
     * <p>
     * Number of hash functions is estimated from:
     * k = (m/n)ln(2),
     * where k is number of hash functions, m is size of Bloom Filter, n is number of expected elements.
     * <p>
     * Bits per element are estimated from:
     * pbs = m/n,
     * where m is size of Bloom Filter, n is number of expected elements.
     * <p>
     * Size of Bloom Filter and number of expected elements should be greater than 0.
     *
     * @param size                     size of Bloom Filter.
     * @param expectedNumberOfElements expected number of elements to be inserted to Bloom Filter.
     */
    public EnhancedDoubleHashBloomFilter(int size, int expectedNumberOfElements) {
        super(size, expectedNumberOfElements);
        try {
            firstHash = MessageDigest.getInstance(DEFAULT_FIRST_HASH_FUNCTION);
            secondHash = MessageDigest.getInstance(DEFAULT_SECOND_HASH_FUNCTION);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Cannot get instance of hash functions!");
        }
    }

    /**
     * Enhanced Double Hashing Bloom filter constructor.
     * <p>
     * Number of hash functions is estimated from:
     * k = (m/n)ln(2),
     * where k is number of hash functions, m is size of Bloom Filter, n is number of expected elements.
     * <p>
     * Bits per element are estimated from:
     * pbs = m/n,
     * where m is size of Bloom Filter, n is number of expected elements.
     * <p>
     * Size of Bloom Filter and number of expected elements should be greater than 0.
     *
     * @param size                     size of Bloom Filter.
     * @param expectedNumberOfElements expected number of elements to be inserted to Bloom Filter.
     * @param firstHashFunction        first hash function.
     * @param secondHashFunction       second hash function.
     */
    public EnhancedDoubleHashBloomFilter(int size, int expectedNumberOfElements,
                                         MessageDigest firstHashFunction, MessageDigest secondHashFunction) {
        super(size, expectedNumberOfElements);
        if ((firstHashFunction == null) || (secondHashFunction == null)) {
            throw new IllegalArgumentException("Instance of hash function cannot be null!");
        }
        if (firstHashFunction.equals(secondHashFunction)) {
            throw new IllegalArgumentException("Hash functions cannot be the same!");
        }
        firstHash = firstHashFunction;
        secondHash = secondHashFunction;
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
        int valueA, valueB;
        int[] hashes = new int[numberOfHash];

        valueA = getValueFromGeneratedHash(bytes, firstHash);
        valueB = getValueFromGeneratedHash(bytes, secondHash);

        hashes[0] = valueA;
        for (int i = 0; i < numberOfHash; i++) {
            valueA = (valueA + valueB) % size;
            valueB = (valueB + i) % size;
            hashes[i] = valueA;
        }
        return hashes;
    }
}
