package prj02;

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import HashTable.*;
import SortedList.*;
import Tree.*;

public class P2Test3 {

	@SuppressWarnings("unchecked")
	BTNode<Integer, String>[] freq = new BTNode[6];
	String[] codes = {"11", "01", "00", "101", "1001", "1000"};
	String input;
	String encoded;
	String decoded;
	int inputBytes;
	double outputBytes;
	String savings;

	Map<String, Integer> fD;
	BTNode<Integer,String> huffmanRoot;
	Map<String,String> encodedHuffman;
	String output;

	@Before
	public void setUp() throws Exception {
		input = "ABBCCCDDDDEEEEEFFFFFF";

		freq[0] = new BTNode<>(6, "F");
		freq[1] = new BTNode<>(5, "E");
		freq[2] = new BTNode<>(4, "D");
		freq[3] = new BTNode<>(3, "C");
		freq[4] = new BTNode<>(2, "B");
		freq[5] = new BTNode<>(1, "A");

		fD = HuffmanCoding.compute_fd(input);
		huffmanRoot = HuffmanCoding.huffman_tree(fD);
		encodedHuffman = HuffmanCoding.huffman_code(huffmanRoot);
		output = HuffmanCoding.encode(encodedHuffman, input);
	}

	@Test
	public void testEncoding() {

		boolean check = output.equals("100010011001101101101000000000101010101111111111111")
				&& HuffmanCoding.decodeHuff(output, encodedHuffman).equals(input);

		assertTrue("Failed to encode correctly input string", check);
	}

	@Test
	public void testBytes() {
		inputBytes = input.getBytes().length;

		DecimalFormat d = new DecimalFormat("##.##");
		outputBytes = Math.round((float) output.getBytes().length / 8);
		savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));

		boolean check = inputBytes == 21 && outputBytes == 6 && savings.equals("71.43");

		assertTrue("Failed to calculate correctly bytes when encoding", check);
	}

	@Test
	public void testFrequencyDistribution() {

		boolean check = true;
		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}


		int i = sortedList.size() - 1;
		int j = 0;
		while(i >= 0 && j < freq.length) {
			BTNode<Integer,String> node = sortedList.get(i);
			if(node.getKey() != freq[j].getKey() && !node.getValue().equals(freq[j].getValue())) {
				check = false;
			}
			i--;
			j++;
		}

		assertTrue("Final Frequency Distribution is Incorrect", check);

	}

	@Test
	public void testCode() {
		boolean check = true;

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		int i = sortedList.size() - 1;
		int j = 0;
		while(i >= 0 && j < codes.length) {
			BTNode<Integer,String> node = sortedList.get(i);
			if(!encodedHuffman.get(node.getValue()).equals(codes[j])) {
				check = false;
			}

			i--;
			j++;
		}

		assertTrue("Failed to encode symbols correctly", check);
	}

}
