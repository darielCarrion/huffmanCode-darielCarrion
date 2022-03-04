package prj02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import HashTable.*;
import List.*;
import SortedList.*;
import Tree.*;


/**
 * The Huffman Encoding Algorithm
 *
 * This is a data compression algorithm designed by David A. Huffman and published in 1952
 *
 * What it does is it takes a string and by constructing a special binary tree with the frequencies of each character.
 * This tree generates special prefix codes that make the size of each string encoded a lot smaller, thus saving space.
 *
 * @author Fernando J. Bermudez Medina (Template)
 * @author A. ElSaid (Review)
 * @author Dariel J. Carrión Rivera <840-16-1133>
 * @version 2.0
 * @since 10/16/2021
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		String data = load_data("input1.txt"); //You can create other test input files and add them to the inputData Folder

		/*If input string is not empty we can encode the text using our algorithm*/
		if (!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else {
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		}
	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 *
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/* We create a new reader that accepts UTF-8 encoding and extract the input string from the file, and we return it */
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/* If input file is empty just return an empty string, if not just extract the data */
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return line;
	}

	/**
	 * Compute symbol frequency distribution of each character inside input string.
	 *
	 * @param inputString the string to compute the frequency of its characters
	 * @return the frequency distribution HashTable map
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		SimpleHashFunction<String> shf = new SimpleHashFunction<String>();
		Map<String, Integer> fd = new HashTableSC<String, Integer>(shf);

		// construct frequency distribution map
		for (char c : inputString.toCharArray()) {
			String key = c + "";
			Integer val = fd.get(key);
			val = (val != null) ? (val + 1) : 1;
			fd.put(key, val);
		}
		
		return fd;
	}


	/**
	 * Receives the frequency distribution map and constructs the Huffman Tree.
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @return the root of the constructed Huffman Tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {
		BTNode<Integer, String> rootNode = null;
		SortedList<BTNode<Integer, String>> sortedList = new SortedLinkedList<>();
		
		// adds the frequency distributions into the sorted linked list
		for (String s : fD.getKeys()) {
			Integer g = fD.get(s);
			sortedList.add(new BTNode<Integer, String>(fD.get(s), s));
		}
		
		// Construct Huffman Tree
		while (sortedList.size() > 1) {
			// set left node to be the string with only one character
			BTNode<Integer, String> right = sortedList.get(0);
			BTNode<Integer, String> left = sortedList.get(1);
			if (right.getValue().length() == 1) {
				BTNode<Integer, String> temp = left;
				left = right;
				right = temp;
			}
			
			// create new node with the sum and concatenation of the current first two elements of the sorted list
			rootNode = new BTNode<Integer, String>(left.getKey()+right.getKey(), left.getValue()+right.getValue());
			rootNode.setLeftChild(left);
			rootNode.setRightChild(right);
			
			// add to the list the root node which represents the merge of the left and right node
			sortedList.remove(left);
			sortedList.remove(right);
			sortedList.add(rootNode);
		}

		return rootNode;
	}
	
	/**
	 * Receives the root node of the Huffman Tree and constructs the prefix code map.
	 *
	 * @param huffmanRoot the root node of the Huffman Tree
	 * @return the constructed prefix code map
	 */
	public static Map<String, String> huffman_code(BTNode<Integer, String> huffmanRoot) {
		Map<String, String> encodedHuffman = new HashTableSC<String, String>(new SimpleHashFunction<String>());
		constructPrefixCodes(encodedHuffman, huffmanRoot, "");
		return encodedHuffman;
	}
	
	/**
	 * Recursively constructs the prefix code map.
	 * 
	 * @param encodingMap the prefix code map
	 * @param root the node with the symbol/frequency and child nodes
	 * @param accCode the accumulated binary code to add to the key of the leaf nodes
	 */
	private static void constructPrefixCodes(Map<String, String> encodingMap, BTNode<Integer, String> root, String accCode) {
		if (root.getRightChild() == null) {
			encodingMap.put(root.getValue(), accCode);
			return;
		}
		else constructPrefixCodes(encodingMap, root.getRightChild(), accCode+"1");

		if (root.getLeftChild() != null) 
			constructPrefixCodes(encodingMap, root.getLeftChild(), accCode+"0");
	}
	
	/**
	 * Receives the prefix code map and the input string to encode with prefix codes.
	 *
	 * @param encodingMap the prefix code map
	 * @param inputString the text to encode
	 * @return the encoded string with binary codes representing the input string
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		String res = "";
		for (char c : inputString.toCharArray())
			res += encodingMap.get(c + "");

		return res;
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string,
	 * and the output string, and prints the results to the screen (per specifications).
	 *
	 * Output Includes: symbol, frequency and code.
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 *
		 * Here we have to get the bytes the same way we got the bytes for the original one but we divide it by 8,
		 * because 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes.
		 *
		 * This is because we want to calculate how many bytes we saved by counting how many bits we generated with the encoding
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes will give us how much space we "chopped off"
		 *
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%)
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console
		 * with a more visual pleasing version of both our Hash Tables in decreasing order by frequency.
		 *
		 * Notice that when the output is shown, the characters with the highest frequency have the lowest amount of bits.
		 *
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/* To print the table in decreasing order by frequency, we do the same thing we did when we built the tree
		 * We add each key with it's frequency in a node into a SortedList, this way we get the frequencies in ascending order*/
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order,
		 * we just traverse the list backwards and start printing the nodes key (character) and value (frequency)
		 * and find the same key in our prefix code "Lookup Table" we made earlier on in huffman_code().
		 *
		 * That way we get the table in decreasing order by frequency
		 * */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData +"\n");
		System.out.println("Encoded String: \n" + output +"\n");
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space required is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/

	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 *
	 * Used for output Purposes
	 *
	 * @param output - Encoded String
	 * @param lookupTable
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/*looping through output until a prefix code is found on map and
		 * adding the symbol that the code that represents it to result */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { //Found it
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;
	}


}
