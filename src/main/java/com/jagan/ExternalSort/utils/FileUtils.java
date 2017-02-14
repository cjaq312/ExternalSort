package com.jagan.ExternalSort.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.jagan.ExternalSort.models.*;

public class FileUtils {

	public long estimateBlockSize(File file) throws Exception {
		if (file == null)
			return 0;

		int nuberOfFiles = 1024;
		long freeMemory = Runtime.getRuntime().freeMemory();
		long fileSize = file.length();
		long blockSize = fileSize / nuberOfFiles;

		if (freeMemory <= blockSize)
			throw new Exception("Not Possible");
		else {
			if (freeMemory / 2 > blockSize)
				blockSize = freeMemory / 2;
		}
		return blockSize;
	}

	public List<File> createSortedBatches(File inputFile, Comparator<String> comparator) throws Exception {
		if (inputFile == null)
			return null;

		List<File> outputList = new ArrayList<File>();
		long estimatedBlockSize = estimateBlockSize(inputFile);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String cache = null;
		int currentBatchSize = 0;
		List<String> batch = new ArrayList<String>();

		try {
			while ((cache = reader.readLine()) != null) {
				currentBatchSize += cache.length();
				if (currentBatchSize < estimatedBlockSize) {
					batch.add(cache);
				} else {
					outputList.add(sortBatch(batch, comparator));
					batch.clear();
					currentBatchSize = cache.length();
					batch.add(cache);
				}
			}

			if (batch.size() > 0) {
				outputList.add(sortBatch(batch, comparator));
				batch.clear();
			}

		} catch (EOFException ex) {
			if (batch.size() > 0) {
				outputList.add(sortBatch(batch, comparator));
				batch.clear();
			}
		} finally {
			reader.close();
		}
		return outputList;
	}

	public File sortBatch(List<String> temp, Comparator<String> comparator) throws IOException {

		Collections.sort(temp, comparator);

		File tempFile = File.createTempFile("sortedTemp", "txt");
		tempFile.deleteOnExit();
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		try {
			for (String line : temp) {
				writer.write(line);
				writer.newLine();
			}
		} finally {
			writer.close();
		}
		return tempFile;
	}

	public File mergeSortedBatches(List<File> sortedFileList, final Comparator<String> comparator,
			String outputFileName) throws IOException {
		File outputFile = new File(outputFileName);
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));

		PriorityQueue<FileBuffer> sortedQueue = new PriorityQueue<FileBuffer>(new Comparator<FileBuffer>() {
			public int compare(FileBuffer file1, FileBuffer file2) {
				return comparator.compare(file1.peek(), file2.peek());
			}
		});

		for (File i : sortedFileList)
			sortedQueue.add(new FileBuffer(i));
		try {
			while (sortedQueue.size() > 0) {
				FileBuffer current = sortedQueue.poll();
				if (current.peek() != null) {
					outputWriter.write(current.pop());
					outputWriter.newLine();
				}
				if (current.isEmpty()) {
					current.close();
				} else {
					sortedQueue.add(current);
				}

			}
		} finally {
			outputWriter.close();
		}
		return outputFile;
	}

}
