package com.jagan.ExternalSort.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileBuffer {

	private BufferedReader reader;
	private boolean empty;
	private String cache;
	private String temp;
	private File inputFile;

	public FileBuffer(File file) throws IOException {
		inputFile = file;
		reader = new BufferedReader(new FileReader(inputFile));
		empty = false;
		cache = null;
		temp = null;
		refresh();
	}

	public String peek() {
		if (isEmpty())
			return null;
		else
			return this.cache;
	}

	public boolean isEmpty() {
		return this.empty;
	}

	public String pop() throws IOException {
		if (isEmpty())
			return null;
		else {
			temp = this.cache;
			refresh();
		}
		return temp;
	}

	public void close() throws IOException {
		reader.close();
		deleteOriginalFile();
	}

	public void refresh() throws IOException {
		this.cache = reader.readLine();

		if (this.cache == null)
			this.empty = true;
		else
			this.empty = false;
	}

	public void deleteOriginalFile() {
		inputFile.delete();
	}
}
