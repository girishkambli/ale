# Markov Chain algorithm Implementation

This project is an attempt to implement Markov Chain algoritm to transfrom the input text. 

## To run backend tests:
```
cd backend
./mvnw clean test 
```
## To run backend:
```
cd backend
./mvnw spring-boot:run
```
###### URL: http://localhost:9001/v1/transform

## To run frontend:
```
cd frontend
npm install
npm start
```
###### Starts at http://localhost:3000/


This project was time-bound to 4 hours. The work includes:
1) Research and understand Markov Chains
2) Design and implement the application to transform input text into new text using Markov Chains algorithm
3) Add unit tests
4) Design and implement a simple UI to upload text files
5) Add this README.md 

Being a time-bound work, there are things that can be improved and these have been listed at the end.

## Task description
#### 1) Research and understand Markov Chains
Put simply, Markov chains are mathematical systems that transition from one state to another and they also tell the probability of transitioning from one state to any other state.
In this implementation, the input text should be transformed into a new text using Markov Chains algorithm. A few concepts such Prefix/Suffix need to be understood here. Prefix is a sequence of words which is followed by a Suffix and these together can form a chain to output a long length text.

#### 2) Design and implement the application
This a simple web application built using SpringBoot. A HTTP endpoint is exposed for an user to uplaod a text file and specify the prefix word count as well maximum allowed size of the output. This triggers the algorithm to build a dictionary using the input text and then use this dictionary to generate a new text.
##### Build dictionary
The dictionary is a mapping of prefix words to a list of suffixes. A prefix contains sequential words from input text. The number of words in the prefix is supplied by user for each request. The list of suffixes should also mainatin the occurence count for each suffix for the given preifx.
The algorithm goes like:
```
Initialize Prefix[1].. Prefix[N] = [EMPTY.... EMPTY]
while(text is available) {
  word = get next word
  Add word to group of suffixes for given prefix. Add to occurance count(weightage)
  Prefix[1].. Prefix[N] = Prefix[2].. Prefix[N] + word
}
Add EMPTY as suffix to the last prefix left
``` 
The algorithm makes use of a constant "EMPTY" which is used as the firts prefix and last suffix. One of the advantages of this approach is that when the text is generated, we can start with this special prefix(EMPTY) and end at suffix EMPTY. This helps in generating somewhat meaningful sentences instead of selecting a random prefix. Also, algorithm knows when to stop.
###### Data structures:
The collection holding prefix words should be able to perform FIFO operations on its elements. So, Queue is the default choice. Since, it is modified for each iteration, LinkedList implementation is a good fit.

The suffix group is a list of elements which should also hold occurence count of each suffix. One of the implementations that fit this requirement is Guava's Multiset. It provides an API to get occurence count for each entry. Another choice could be a PriorityQueue, which can sort the elements based on sorting criteria.

##### Generate text
We just need to start from our special prefix(EMPTY) to navigate through the chain till special suffix(EMPTY) is encountered. We may also stop if the length of text reaches limit specified by the user(output size). A suffix is selected from the group based on weightage(occurence count).
Algorithm:
```
Prefix[1].. Prefix[N] = [EMPTY.... EMPTY]
Find list of suffixes for this prefix from dictionary
Select a Suffix based on weightage
while(Suffix is not EMPTY) {
  Add Suffix to text
  Prefix[1].. Prefix[N] = Prefix[2].. Prefix[N] + Suffix
  Find list of suffixes for this prefix from dictionary
  Select a Suffix based on weightage
}
```

#### 3) Add unit tests
I followed TDD approach, so JUnit tests were ready along with prodcution code. This also helped in making changes to algorithm logic since code was test covered.

#### 4) Design and implement a simple UI
A simple UI was developed using React. Used Material UI for look and feel.

## Improvements / TODOs
1) Performance improvements to process large data set. The task to build dictionary could be parallelized if possible.
2) UI look and feel can be improved.
3) Experiment with slight variations in algorithm such as random prefix selection for text generation, or using PriorityQueue as a collection to store suffix groups.
4) Reactive http using Webflux.
