package com.infinities.keystone4j.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressionUtils {

	private final static Logger logger = LoggerFactory.getLogger(CompressionUtils.class);


	private CompressionUtils() {

	}

	public static byte[] compress(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream(data.length);

			deflater.finish();
			byte[] buffer = new byte[1024];

			while (!deflater.finished()) {
				int count = deflater.deflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			byte[] output = outputStream.toByteArray();
			logger.debug("Original: " + data.length / 1024 + " Kb");
			logger.debug("Compressed: " + output.length / 1024 + " Kb");
			return output;

		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.warn("close ByteArrayOutputStream failed", e);
				}
			}
		}
	}

	public static byte[] decompress(byte[] data) throws DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream(data.length);
			byte[] buffer = new byte[1024];

			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			byte[] output = outputStream.toByteArray();
			logger.debug("Original: " + data.length / 1024 + " Kb");
			logger.debug("Decompressed: " + output.length / 1024 + " Kb");
			return output;

		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.warn("close ByteArrayOutputStream failed", e);
				}
			}
		}
	}

}
