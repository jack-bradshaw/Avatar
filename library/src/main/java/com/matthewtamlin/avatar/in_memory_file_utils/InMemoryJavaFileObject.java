package com.matthewtamlin.avatar.in_memory_file_utils;

import com.google.common.io.ByteSource;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A Java file object which stores data in memory instead of on disk. This class is not currently part of the public
 * API because if does not fully comply with the {@link JavaFileObject} interface contract.
 */
public class InMemoryJavaFileObject extends SimpleJavaFileObject {
	private final Charset charset = Charset.defaultCharset();
	
	private ByteSource data;
	
	private long lastModifiedMsFromEpoch = 0L;
	
	public InMemoryJavaFileObject(final URI uri) {
		super(uri, getKindFromExtension(uri));
	}
	
	@Override
	public InputStream openInputStream() throws IOException {
		if (data != null) {
			return data.openStream();
		} else {
			throw new FileNotFoundException();
		}
	}
	
	@Override
	public OutputStream openOutputStream() throws IOException {
		return new ByteArrayOutputStream() {
			@Override
			public void close() throws IOException {
				// Save the output stream contents as the file contents
				data = ByteSource.wrap(toByteArray());
				lastModifiedMsFromEpoch = System.currentTimeMillis();
			}
		};
	}
	
	@Override
	public Reader openReader(final boolean ignoreEncodingErrors) throws IOException {
		if (data != null) {
			return data.asCharSource(charset).openStream();
		} else {
			throw new FileNotFoundException();
		}
	}
	
	@Override
	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
		if (data != null) {
			return data.asCharSource(charset).read();
		} else {
			throw new FileNotFoundException();
		}
	}
	
	@Override
	public Writer openWriter() throws IOException {
		return new StringWriter() {
			@Override
			public void close() throws IOException {
				// Save the writer contents as the file contents
				data = ByteSource.wrap(toString().getBytes(charset));
				lastModifiedMsFromEpoch = System.currentTimeMillis();
			}
		};
	}
	
	@Override
	public long getLastModified() {
		return lastModifiedMsFromEpoch;
	}
	
	@Override
	public boolean delete() {
		this.data = null;
		this.lastModifiedMsFromEpoch = 0L;
		
		return true;
	}
	
	private static JavaFileObject.Kind getKindFromExtension(final URI uri) {
		checkNotNull(uri, "Argument \'uri\' cannot be null.");
		
		final String path = uri.getPath();
		
		for (final JavaFileObject.Kind kind : JavaFileObject.Kind.values()) {
			if (path.endsWith(kind.extension)) {
				return kind;
			}
		}
		
		return JavaFileObject.Kind.OTHER;
	}
}