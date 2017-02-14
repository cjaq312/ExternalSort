package com.jagan.ExternalSort.utils;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class Sort {

	public static File externalSort(String inputFilePath, String outputFilePath) throws Exception {

		File inputFile = new File(inputFilePath);
		File outputFile = null;
		Comparator<String> comparator = new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		};
		FileUtils util = new FileUtils();
		List<File> sortedBatchFiles = util.createSortedBatches(inputFile, comparator);

		outputFile = util.mergeSortedBatches(sortedBatchFiles, comparator, outputFilePath);

		return outputFile;
	}

}
