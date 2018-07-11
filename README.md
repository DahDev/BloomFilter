# BloomFilter ![](https://travis-ci.org/DahDev/BloomFilter-Java.svg?branch=master)

A Bloom Filter is a space-efficient probabilistic data structure. 
Presented solution is implemented in Java. Python implementation is available [here](https://github.com/DahDev/BloomFilter-Python).
It contains few methods for generating  independent hash functions:

- Double Hashing
- Triple Hashing 
- Enhanced Double Hashing

All the approaches are described in "Bloom Filters in Probabilistic Verification" by Peter C. Dillinger and Panagiotis Manolios. The paper is available [here](http://www.ccs.neu.edu/home/pete/pub/bloom-filters-verification.pdf).


## Build

Just run the following command:

```
mvn install
```

## Example

Using Bloom Filter with Double Hashing method:

```
BloomFilter<String> filter = new DoubleHashBloomFilter<String>(0.001, 10);
filter.add("Test");
filter.mightContains("Test");
```
