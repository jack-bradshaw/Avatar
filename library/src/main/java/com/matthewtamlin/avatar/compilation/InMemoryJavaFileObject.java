package com.matthewtamlin.avatar.compilation;

import com.google.common.io.ByteSource;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A Java file object which stores data in memory.
 */
public class InMemoryJavaFileObject extends SimpleJavaFileObject {
	/**
	 * The encoding used by the file.
	 */
	private final Charset charset = Charset.defaultCharset();
	
	/**
	 * The contents of the file.
	 */
	private ByteSource data;
	
	/**
	 * The time this file was last modified, measured in milliseconds since the epoch date.
	 */
	private long lastModifiedMsFromEpoch = 0L;
	
	/**
	 * Constructs a new InMemoryJavaFileObject for the file located at the supplied URI.
	 *
	 * @param uri
	 * 		a URI pointing at the file, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code uri} is null
	 */
	public InMemoryJavaFileObject(final URI uri) {
		super(checkNotNull(uri, "Argument \'uri\' cannot be null."), getKindFromExtension(uri));
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
	
	/**
	 * Extracts the {@link Kind} from a file.
	 *
	 * @param uri
	 * 		points at the file to get the kind of, not null
	 *
	 * @return the kind of the file, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code uri} is null
	 */
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