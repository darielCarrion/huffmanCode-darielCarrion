package prj02;

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import HashTable.*;
import SortedList.*;
import Tree.*;

public class P2Test2 {

	@SuppressWarnings("unchecked")
	BTNode<Integer, String>[] freq = new BTNode[23];
	String[] codes = {
			"110",
			"011",
			"000",
			"1110",
			"1010",
			"1001",
			"1000",
			"0101",
			"0100",
			"0010",
			"11110",
			"10110",
			"00111",
			"101111",
			"101110",
			"001101",
			"1111111",
			"1111110",
			"1111100",
			"0011001",
			"0011000",
			"11111011",
			"11111010"
	};
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
		input = "AAAAAAABBBBBBBCCCCCCCDDDDDDDDDEEEEEEFFFFFFGGGGGGGTTTTTTTSSSSSCCCCCCCAAGGGSGSGDGDGGDGEEEMOKOKKKKKKOKOOKOKJJUYGTTFRRDDESWAWVHNJKMLKKJGDRDFJVVCCCCCCCCCCCCCPPPPOOOIIT";

		freq[0] = new BTNode<>(27, "C");
		freq[1] = new BTNode<>(18, "G");
		freq[2] = new BTNode<>(16, "D");
		freq[3] = new BTNode<>(13, "K");
		freq[4] = new BTNode<>(10, "T");
		freq[5] = new BTNode<>(10, "E");
		freq[6] = new BTNode<>(10, "A");
		freq[7] = new BTNode<>(9, "O");
		freq[8] = new BTNode<>(8, "S");
		freq[9] = new BTNode<>(8, "F");
		freq[10] = new BTNode<>(7, "B");
		freq[11] = new BTNode<>(5, "J");
		freq[12] = new BTNode<>(4, "P");
		freq[13] = new BTNode<>(3, "V");
		freq[14] = new BTNode<>(3, "R");
		freq[15] = new BTNode<>(2, "W");
		freq[16] = new BTNode<>(2, "M");
		freq[17] = new BTNode<>(2, "I");
		freq[18] = new BTNode<>(1, "Y");
		freq[19] = new BTNode<>(1, "U");
		freq[20] = new BTNode<>(1, "N");
		freq[21] = new BTNode<>(1, "L");
		freq[22] = new BTNode<>(1, "H");

		fD = HuffmanCoding.compute_fd(input);
		huffmanRoot = HuffmanCoding.huffman_tree(fD);
		encodedHuffman = HuffmanCoding.huffman_code(huffmanRoot);
		output = HuffmanCoding.encode(encodedHuffman, input);
	}

	@Test
	public void testEncoding() {

		boolean check = output.equals("100010001000100010001000100011110111101111011110111101111011110110110110110110110110000000000000000000000000000100110011001100110011001001000100010001000100010011011011011011011011101010101010101010101010101001000100010001000100110110110110110110110100010000110110110100011010001100001100001101100001110011001100111111110101111001011110111011101110111011100101111001010101111001011110101101011000110011111100011101010100010101110101110000000100101000011011000001101101111111110100011000101101110111111111111011111011101011001100010111000000101011010111110111111011011011011011011011011011011011011000111001110011100111010101010101111111011111101010")
				&& HuffmanCoding.decodeHuff(output, encodedHuffman).equals(input);

		assertTrue("Failed to encode correctly input string", check);
	}

	@Test
	public void testBytes() {
		inputBytes = input.getBytes().length;

		DecimalFormat d = new DecimalFormat("##.##");
		outputBytes = Math.round((float) output.getBytes().length / 8);
		savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));

		boolean check = inputBytes == 162 && outputBytes == 81 && savings.equals("50");

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
